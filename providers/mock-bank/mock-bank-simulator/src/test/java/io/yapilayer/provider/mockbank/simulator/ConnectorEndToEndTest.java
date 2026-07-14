package io.yapilayer.provider.mockbank.simulator;

import io.yapilayer.platform.domain.account.Account;
import io.yapilayer.platform.domain.account.AccountIdentifier;
import io.yapilayer.platform.domain.account.Balance;
import io.yapilayer.platform.domain.common.Money;
import io.yapilayer.platform.domain.consent.ConsentStatus;
import io.yapilayer.platform.domain.consent.Permission;
import io.yapilayer.platform.domain.payment.Creditor;
import io.yapilayer.platform.domain.payment.PaymentStatus;
import io.yapilayer.provider.mockbank.connector.MockBankConnector;
import io.yapilayer.provider.sdk.ais.AisConsentRequest;
import io.yapilayer.provider.sdk.ais.AisProviderPort;
import io.yapilayer.provider.sdk.ais.ConsentAuthorisation;
import io.yapilayer.provider.sdk.ais.PageRequest;
import io.yapilayer.provider.sdk.ais.ProviderSession;
import io.yapilayer.provider.sdk.ais.TransactionPage;
import io.yapilayer.provider.sdk.pis.PaymentAuthorisation;
import io.yapilayer.provider.sdk.pis.PaymentRequest;
import io.yapilayer.provider.sdk.pis.PisProviderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The Milestone 3 acceptance test: the real MockBankConnector driven through
 * complete AIS and PIS journeys against a live simulator instance — the mock
 * equivalent of "one CMA9 sandbox integration works" (DECISIONS.md D-1).
 *
 * <p>The customer's browser step (visiting the authorisation URL and approving)
 * is simulated by posting the consent decision form directly.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConnectorEndToEndTest {

    private static final Pattern CODE_IN_LOCATION = Pattern.compile("[?&]code=([^&]+)");

    @LocalServerPort
    private int port;

    private MockBankConnector connector;
    private final RestTemplate browser = new RestTemplate();

    @BeforeEach
    void setUp() {
        connector = new MockBankConnector(URI.create("http://localhost:" + port));
    }

    @Test
    void fullAisJourney() {
        AisProviderPort ais = connector.aisPort().orElseThrow();

        // 1. Create consent — returns the bank authorisation URL
        ConsentAuthorisation authorisation = ais.createConsent(new AisConsentRequest(
                Set.of(Permission.READ_ACCOUNTS, Permission.READ_BALANCES, Permission.READ_TRANSACTIONS),
                Instant.now().plusSeconds(90L * 24 * 3600),
                URI.create("http://localhost:9999/callback"),
                "state-123"));
        assertThat(authorisation.providerConsentId()).startsWith("aisc-");
        assertThat(ais.getConsentStatus(authorisation.providerConsentId()))
                .isEqualTo(ConsentStatus.AWAITING_AUTHORISATION);

        // 2. Customer approves at the bank (form post in place of the browser)
        String code = approveAtBank(authorisation.providerConsentId(),
                "http://localhost:9999/callback", "state-123");
        assertThat(ais.getConsentStatus(authorisation.providerConsentId()))
                .isEqualTo(ConsentStatus.AUTHORISED);

        // 3. Exchange the code for a session
        ProviderSession session =
                ais.exchangeAuthorisationCode(authorisation.providerConsentId(), code);

        // 4. Accounts, balances, transactions
        var accounts = ais.listAccounts(session);
        assertThat(accounts).hasSize(2);
        Account current = accounts.stream()
                .filter(a -> a.type() == Account.AccountType.CURRENT).findFirst().orElseThrow();
        assertThat(current.identifiers()).isNotEmpty();

        var balances = ais.getBalances(session, current.id());
        assertThat(balances).extracting(Balance::type)
                .containsExactlyInAnyOrder(Balance.BalanceType.CURRENT, Balance.BalanceType.AVAILABLE);

        TransactionPage firstPage = ais.getTransactions(session, current.id(), PageRequest.firstPage(5));
        assertThat(firstPage.transactions()).hasSize(5);
        assertThat(firstPage.nextPageKey()).isPresent();

        TransactionPage secondPage = ais.getTransactions(session, current.id(),
                new PageRequest(firstPage.nextPageKey(), 5));
        assertThat(secondPage.transactions()).hasSize(2);
        assertThat(secondPage.nextPageKey()).isEmpty();
    }

    @Test
    void fullPisJourney() {
        PisProviderPort pis = connector.pisPort().orElseThrow();

        // 1. Create payment consent
        PaymentAuthorisation authorisation = pis.createPaymentConsent(new PaymentRequest(
                Money.of("25.00", "GBP"),
                new Creditor("Acme Ltd",
                        new AccountIdentifier("UK.OBIE.SortCodeAccountNumber", "20000012345678")),
                "INV-001",
                URI.create("http://localhost:9999/callback"),
                "state-456"));
        assertThat(authorisation.providerPaymentConsentId()).startsWith("pisc-");

        // 2. Customer authorises the payment
        String code = approveAtBank(authorisation.providerPaymentConsentId(),
                "http://localhost:9999/callback", "state-456");

        // 3. Exchange and submit
        ProviderSession session = pis.exchangeAuthorisationCode(
                authorisation.providerPaymentConsentId(), code);
        String paymentId = pis.submitPayment(session, authorisation.providerPaymentConsentId());
        assertThat(paymentId).startsWith("pay-");

        // 4. Poll to terminal status (simulator advances one step per poll)
        assertThat(pis.getPaymentStatus(session, paymentId)).isEqualTo(PaymentStatus.AUTHORISED);
        assertThat(pis.getPaymentStatus(session, paymentId)).isEqualTo(PaymentStatus.COMPLETED);
    }

    /** Posts the consent decision form the way the customer's browser would, returning the code. */
    private String approveAtBank(String consentId, String redirectUri, String state) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("consent_id", consentId);
        form.add("redirect_uri", redirectUri);
        form.add("state", state);
        form.add("decision", "approve");

        ResponseEntity<Void> response = browser.postForEntity(
                "http://localhost:" + port + "/oauth/authorize/decision",
                new HttpEntity<>(form, headers), Void.class);

        String location = response.getHeaders().getFirst(HttpHeaders.LOCATION);
        assertThat(location).contains("state=" + state);
        Matcher matcher = CODE_IN_LOCATION.matcher(location);
        assertThat(matcher.find()).isTrue();
        return URLDecoder.decode(matcher.group(1), StandardCharsets.UTF_8);
    }
}
