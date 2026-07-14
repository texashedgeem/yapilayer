package io.yapilayer.platform.api.v1;

import io.yapilayer.platform.application.pis.PisService;
import io.yapilayer.platform.domain.account.AccountIdentifier;
import io.yapilayer.platform.domain.common.Money;
import io.yapilayer.platform.domain.common.ProviderId;
import io.yapilayer.platform.domain.payment.Creditor;
import io.yapilayer.platform.domain.payment.Payment;
import io.yapilayer.platform.domain.payment.PaymentId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

/** PIS payment endpoints. */
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentsController {

    public record CreatePaymentRequest(String providerId, String amount, String currency,
                                       CreditorDto creditor, String reference,
                                       String redirectUri) {}

    public record CreditorDto(String name, String scheme, String identification) {}

    public record PaymentDto(UUID paymentId, String providerId, String status,
                             String amount, String currency, String creditorName,
                             String reference, String createdAt, String updatedAt,
                             String authorisationUrl) {}

    private final PisService pis;

    public PaymentsController(PisService pis) {
        this.pis = pis;
    }

    @PostMapping
    public ResponseEntity<PaymentDto> create(@RequestBody CreatePaymentRequest request) {
        PisService.CreatedPayment created = pis.createPayment(
                new ProviderId(request.providerId()),
                Money.of(request.amount(), request.currency()),
                new Creditor(request.creditor().name(),
                        new AccountIdentifier(request.creditor().scheme(),
                                request.creditor().identification())),
                request.reference(),
                URI.create(request.redirectUri()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toDto(created.payment(), created.authorisationUrl()));
    }

    @GetMapping("/{paymentId}")
    public PaymentDto get(@PathVariable UUID paymentId) {
        return toDto(pis.getPayment(new PaymentId(paymentId)), null);
    }

    private static PaymentDto toDto(Payment payment, URI authorisationUrl) {
        return new PaymentDto(
                payment.id().value(),
                payment.providerId().value(),
                payment.status().name(),
                payment.amount().amount().toPlainString(),
                payment.amount().currency().getCurrencyCode(),
                payment.creditor().name(),
                payment.reference(),
                payment.createdAt().toString(),
                payment.updatedAt().toString(),
                authorisationUrl != null ? authorisationUrl.toString() : null);
    }
}
