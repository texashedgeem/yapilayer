package io.yapilayer.platform.domain.payment;

import java.util.Map;
import java.util.Set;

/**
 * Payment lifecycle (PRODUCT_REQUIREMENTS §7): pending → authorised → completed, with rejection and
 * cancellation exits.
 */
public enum PaymentStatus {
    PENDING,
    AUTHORISED,
    REJECTED,
    COMPLETED,
    CANCELLED;

    private static final Map<PaymentStatus, Set<PaymentStatus>> ALLOWED =
            Map.of(
                    PENDING, Set.of(AUTHORISED, REJECTED, CANCELLED),
                    AUTHORISED, Set.of(COMPLETED, REJECTED, CANCELLED),
                    REJECTED, Set.of(),
                    COMPLETED, Set.of(),
                    CANCELLED, Set.of());

    public boolean canTransitionTo(PaymentStatus target) {
        return ALLOWED.get(this).contains(target);
    }

    public boolean isTerminal() {
        return ALLOWED.get(this).isEmpty();
    }
}
