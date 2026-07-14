package io.yapilayer.provider.mockbank.simulator.payment;

import io.yapilayer.provider.mockbank.simulator.consent.AccountAccessConsentController;
import io.yapilayer.provider.mockbank.simulator.consent.ConsentStore;
import io.yapilayer.provider.mockbank.simulator.security.BearerAuth;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OB Read/Write-style PIS endpoints ({@code /open-banking/v3.1/pisp/domestic-payment-consents} and
 * {@code /domestic-payments}).
 */
@RestController
@RequestMapping("/open-banking/v3.1/pisp")
public class PaymentController {

    private final ConsentStore consents;
    private final PaymentStore payments;
    private final BearerAuth auth;

    public PaymentController(ConsentStore consents, PaymentStore payments, BearerAuth auth) {
        this.consents = consents;
        this.payments = payments;
        this.auth = auth;
    }

    @PostMapping("/domestic-payment-consents")
    public ResponseEntity<Map<String, Object>> createConsent(
            @RequestBody Map<String, Object> body) {
        ConsentStore.SimConsent consent = consents.createPis(Instant.now().plusSeconds(24 * 3600));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        Map.of(
                                "Data",
                                Map.of(
                                        "ConsentId", consent.id(),
                                        "Status",
                                                AccountAccessConsentController.obStatus(
                                                        consent.status()))));
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/domestic-payments")
    public ResponseEntity<Map<String, Object>> submit(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestBody Map<String, Object> body) {
        ConsentStore.SimConsent consent = auth.requireAuthorisedConsent(authorization);

        Map<String, Object> data = (Map<String, Object>) body.getOrDefault("Data", Map.of());
        Map<String, Object> initiation =
                (Map<String, Object>) data.getOrDefault("Initiation", Map.of());
        Map<String, Object> amount =
                (Map<String, Object>) initiation.getOrDefault("InstructedAmount", Map.of());
        Map<String, Object> creditor =
                (Map<String, Object>) initiation.getOrDefault("CreditorAccount", Map.of());

        PaymentStore.SimPayment payment =
                payments.submit(
                        consent.id(),
                        (String) amount.getOrDefault("Amount", "0.00"),
                        (String) amount.getOrDefault("Currency", "GBP"),
                        (String) creditor.getOrDefault("Name", "Unknown"),
                        (String) initiation.getOrDefault("Reference", ""));

        return ResponseEntity.status(HttpStatus.CREATED).body(render(payment));
    }

    @GetMapping("/domestic-payments/{paymentId}")
    public ResponseEntity<Map<String, Object>> status(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable String paymentId) {
        auth.requireAuthorisedConsent(authorization);
        return payments.pollAndAdvance(paymentId)
                .map(p -> ResponseEntity.ok(render(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    private static Map<String, Object> render(PaymentStore.SimPayment payment) {
        return Map.of(
                "Data",
                Map.of(
                        "DomesticPaymentId", payment.id(),
                        "ConsentId", payment.consentId(),
                        "Status", payment.status(),
                        "Initiation",
                                Map.of(
                                        "InstructedAmount",
                                                Map.of(
                                                        "Amount", payment.amount(),
                                                        "Currency", payment.currency()),
                                        "CreditorAccount", Map.of("Name", payment.creditorName()),
                                        "Reference", payment.reference())));
    }
}
