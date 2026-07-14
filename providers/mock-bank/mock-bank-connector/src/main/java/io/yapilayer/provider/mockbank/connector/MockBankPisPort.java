package io.yapilayer.provider.mockbank.connector;

import com.fasterxml.jackson.databind.JsonNode;
import io.yapilayer.platform.domain.payment.PaymentStatus;
import io.yapilayer.provider.sdk.ais.ProviderSession;
import io.yapilayer.provider.sdk.pis.PaymentAuthorisation;
import io.yapilayer.provider.sdk.pis.PaymentRequest;
import io.yapilayer.provider.sdk.pis.PisProviderPort;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

/** PIS port implementation against the mock bank simulator's OB-style API. */
final class MockBankPisPort implements PisProviderPort {

    private static final String PISP = "/open-banking/v3.1/pisp";

    private final MockBankHttp http;
    private final URI publicBaseUrl;

    MockBankPisPort(MockBankHttp http, URI publicBaseUrl) {
        this.http = http;
        this.publicBaseUrl = publicBaseUrl;
    }

    @Override
    public PaymentAuthorisation createPaymentConsent(PaymentRequest request) {
        JsonNode response =
                http.postJson(
                        PISP + "/domestic-payment-consents",
                        Map.of(
                                "Data",
                                Map.of(
                                        "Initiation",
                                        Map.of(
                                                "InstructedAmount",
                                                        Map.of(
                                                                "Amount",
                                                                        request.amount()
                                                                                .amount()
                                                                                .toPlainString(),
                                                                "Currency",
                                                                        request.amount()
                                                                                .currency()
                                                                                .getCurrencyCode()),
                                                "CreditorAccount",
                                                        Map.of(
                                                                "Name", request.creditor().name(),
                                                                "SchemeName",
                                                                        request.creditor()
                                                                                .account()
                                                                                .scheme(),
                                                                "Identification",
                                                                        request.creditor()
                                                                                .account()
                                                                                .identification()),
                                                "Reference", request.reference()))),
                        null);

        String consentId = response.at("/Data/ConsentId").asText();
        URI authoriseUrl =
                publicBaseUrl.resolve(
                        "/oauth/authorize"
                                + "?consent_id="
                                + encode(consentId)
                                + "&redirect_uri="
                                + encode(request.redirectUri().toString())
                                + "&state="
                                + encode(request.state()));
        return new PaymentAuthorisation(consentId, authoriseUrl);
    }

    @Override
    public ProviderSession exchangeAuthorisationCode(
            String providerPaymentConsentId, String authorisationCode) {
        JsonNode response =
                http.postForm(
                        "/oauth/token",
                        Map.of("grant_type", "authorization_code", "code", authorisationCode));
        return new ProviderSession(
                providerPaymentConsentId,
                response.get("access_token").asText(),
                Instant.now().plusSeconds(response.get("expires_in").asLong()),
                Optional.empty());
    }

    @Override
    public String submitPayment(ProviderSession session, String providerPaymentConsentId) {
        JsonNode response =
                http.postJson(
                        PISP + "/domestic-payments",
                        Map.of(
                                "Data",
                                Map.of(
                                        "ConsentId",
                                        providerPaymentConsentId,
                                        "Initiation",
                                        Map.of())),
                        session.accessToken());
        return response.at("/Data/DomesticPaymentId").asText();
    }

    @Override
    public PaymentStatus getPaymentStatus(ProviderSession session, String providerPaymentId) {
        JsonNode response =
                http.getJson(
                        PISP + "/domestic-payments/" + providerPaymentId, session.accessToken());
        return switch (response.at("/Data/Status").asText()) {
            case "AcceptedSettlementInProcess" -> PaymentStatus.AUTHORISED;
            case "AcceptedSettlementCompleted" -> PaymentStatus.COMPLETED;
            case "Rejected" -> PaymentStatus.REJECTED;
            case "Cancelled" -> PaymentStatus.CANCELLED;
            default -> PaymentStatus.PENDING;
        };
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
