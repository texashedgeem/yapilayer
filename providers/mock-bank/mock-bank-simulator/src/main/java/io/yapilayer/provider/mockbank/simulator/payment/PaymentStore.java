package io.yapilayer.provider.mockbank.simulator.payment;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory payments. Status auto-advances one step per poll —
 * Pending → AcceptedSettlementInProcess → AcceptedSettlementCompleted —
 * simulating a real payment lifecycle without needing a clock.
 */
@Component
public class PaymentStore {

    public record SimPayment(String id, String consentId, String amount, String currency,
                             String creditorName, String reference, String status) {}

    private final Map<String, SimPayment> payments = new ConcurrentHashMap<>();

    public SimPayment submit(String consentId, String amount, String currency,
                             String creditorName, String reference) {
        SimPayment payment = new SimPayment("pay-" + UUID.randomUUID(), consentId,
                amount, currency, creditorName, reference, "Pending");
        payments.put(payment.id(), payment);
        return payment;
    }

    /** Returns the payment, advancing its status one lifecycle step per call. */
    public Optional<SimPayment> pollAndAdvance(String paymentId) {
        return Optional.ofNullable(payments.computeIfPresent(paymentId, (k, p) ->
                new SimPayment(p.id(), p.consentId(), p.amount(), p.currency(),
                        p.creditorName(), p.reference(), next(p.status()))));
    }

    private static String next(String status) {
        return switch (status) {
            case "Pending" -> "AcceptedSettlementInProcess";
            case "AcceptedSettlementInProcess" -> "AcceptedSettlementCompleted";
            default -> status;
        };
    }
}
