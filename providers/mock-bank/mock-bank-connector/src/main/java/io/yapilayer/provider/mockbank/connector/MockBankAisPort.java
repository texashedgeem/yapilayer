package io.yapilayer.provider.mockbank.connector;

import com.fasterxml.jackson.databind.JsonNode;
import io.yapilayer.platform.domain.account.Account;
import io.yapilayer.platform.domain.account.AccountId;
import io.yapilayer.platform.domain.account.AccountIdentifier;
import io.yapilayer.platform.domain.account.Balance;
import io.yapilayer.platform.domain.account.Transaction;
import io.yapilayer.platform.domain.common.Money;
import io.yapilayer.platform.domain.consent.ConsentStatus;
import io.yapilayer.provider.sdk.ais.AisConsentRequest;
import io.yapilayer.provider.sdk.ais.AisProviderPort;
import io.yapilayer.provider.sdk.ais.ConsentAuthorisation;
import io.yapilayer.provider.sdk.ais.PageRequest;
import io.yapilayer.provider.sdk.ais.ProviderSession;
import io.yapilayer.provider.sdk.ais.TransactionPage;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** AIS port implementation against the mock bank simulator's OB-style API. */
final class MockBankAisPort implements AisProviderPort {

    private static final String AISP = "/open-banking/v3.1/aisp";

    private final MockBankHttp http;
    private final URI publicBaseUrl;

    MockBankAisPort(MockBankHttp http, URI publicBaseUrl) {
        this.http = http;
        this.publicBaseUrl = publicBaseUrl;
    }

    @Override
    public ConsentAuthorisation createConsent(AisConsentRequest request) {
        List<String> permissions = request.permissions().stream()
                .map(p -> switch (p) {
                    case READ_ACCOUNTS -> "ReadAccountsDetail";
                    case READ_BALANCES -> "ReadBalances";
                    case READ_TRANSACTIONS -> "ReadTransactionsDetail";
                    case READ_IDENTITY -> "ReadParty";
                })
                .toList();

        JsonNode response = http.postJson(AISP + "/account-access-consents",
                Map.of("Data", Map.of(
                        "Permissions", permissions,
                        "ExpirationDateTime", request.expiresAt().toString())),
                null);

        String consentId = response.at("/Data/ConsentId").asText();
        URI authoriseUrl = publicBaseUrl.resolve("/oauth/authorize"
                + "?consent_id=" + encode(consentId)
                + "&redirect_uri=" + encode(request.redirectUri().toString())
                + "&state=" + encode(request.state()));
        return new ConsentAuthorisation(consentId, authoriseUrl);
    }

    @Override
    public ConsentStatus getConsentStatus(String providerConsentId) {
        JsonNode response = http.getJsonNoAuth(
                AISP + "/account-access-consents/" + providerConsentId);
        return switch (response.at("/Data/Status").asText()) {
            case "Authorised" -> ConsentStatus.AUTHORISED;
            case "Rejected" -> ConsentStatus.REJECTED;
            default -> ConsentStatus.AWAITING_AUTHORISATION;
        };
    }

    @Override
    public ProviderSession exchangeAuthorisationCode(String providerConsentId, String authorisationCode) {
        JsonNode response = http.postForm("/oauth/token", Map.of(
                "grant_type", "authorization_code",
                "code", authorisationCode));
        return new ProviderSession(
                providerConsentId,
                response.get("access_token").asText(),
                Instant.now().plusSeconds(response.get("expires_in").asLong()),
                Optional.empty());
    }

    @Override
    public List<Account> listAccounts(ProviderSession session) {
        JsonNode response = http.getJson(AISP + "/accounts", session.accessToken());
        List<Account> accounts = new ArrayList<>();
        for (JsonNode node : response.at("/Data/Account")) {
            List<AccountIdentifier> identifiers = new ArrayList<>();
            for (JsonNode id : node.path("Account")) {
                identifiers.add(new AccountIdentifier(
                        id.get("SchemeName").asText(), id.get("Identification").asText()));
            }
            accounts.add(new Account(
                    new AccountId(node.get("AccountId").asText()),
                    MockBankConnector.PROVIDER_ID,
                    node.get("Nickname").asText(),
                    Currency.getInstance(node.get("Currency").asText()),
                    "Savings".equals(node.get("AccountSubType").asText())
                            ? Account.AccountType.SAVINGS
                            : Account.AccountType.CURRENT,
                    identifiers));
        }
        return accounts;
    }

    @Override
    public List<Balance> getBalances(ProviderSession session, AccountId accountId) {
        JsonNode response = http.getJson(
                AISP + "/accounts/" + accountId.value() + "/balances", session.accessToken());
        List<Balance> balances = new ArrayList<>();
        for (JsonNode node : response.at("/Data/Balance")) {
            balances.add(new Balance(
                    accountId,
                    "InterimAvailable".equals(node.get("Type").asText())
                            ? Balance.BalanceType.AVAILABLE
                            : Balance.BalanceType.CURRENT,
                    money(node.get("Amount")),
                    Instant.parse(node.get("DateTime").asText())));
        }
        return balances;
    }

    @Override
    public TransactionPage getTransactions(ProviderSession session, AccountId accountId,
                                           PageRequest page) {
        String path = AISP + "/accounts/" + accountId.value() + "/transactions"
                + "?page=" + page.pageKey().orElse("0");
        JsonNode response = http.getJson(path, session.accessToken());

        List<Transaction> transactions = new ArrayList<>();
        for (JsonNode node : response.at("/Data/Transaction")) {
            transactions.add(new Transaction(
                    node.get("TransactionId").asText(),
                    accountId,
                    money(node.get("Amount")),
                    "Pending".equals(node.get("Status").asText())
                            ? Transaction.Status.PENDING
                            : Transaction.Status.BOOKED,
                    LocalDate.parse(node.get("BookingDateTime").asText().substring(0, 10)),
                    Optional.ofNullable(node.path("TransactionInformation").asText(null)),
                    node.has("MerchantDetails")
                            ? Optional.of(node.at("/MerchantDetails/MerchantName").asText())
                            : Optional.empty()));
        }

        Optional<String> nextPage = response.has("Links")
                ? Optional.of(response.at("/Links/Next").asText())
                : Optional.empty();
        return new TransactionPage(transactions, nextPage);
    }

    private static Money money(JsonNode amountNode) {
        return Money.of(amountNode.get("Amount").asText(), amountNode.get("Currency").asText());
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
