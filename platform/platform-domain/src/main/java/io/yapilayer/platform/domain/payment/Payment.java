package io.yapilayer.platform.domain.payment;

import io.yapilayer.platform.domain.common.Money;
import io.yapilayer.platform.domain.common.ProviderId;
import io.yapilayer.platform.domain.common.TenantId;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * A single domestic payment initiated through a provider.
 *
 * <p>Immutable — {@link #transitionTo} returns a new instance and rejects moves
 * the {@link PaymentStatus} state machine does not allow.
 *
 * @param providerPaymentId provider-side payment reference, present once the
 *                          provider has accepted the payment for processing
 * @param reference         payment reference visible to payer and payee
 */
public record Payment(
        PaymentId id,
        TenantId tenantId,
        ProviderId providerId,
        Money amount,
        Creditor creditor,
        String reference,
        PaymentStatus status,
        Instant createdAt,
        Instant updatedAt,
        Optional<String> providerPaymentId) {

    public Payment {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(tenantId, "tenantId");
        Objects.requireNonNull(providerId, "providerId");
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(creditor, "creditor");
        Objects.requireNonNull(status, "status");
        Objects.requireNonNull(createdAt, "createdAt");
        Objects.requireNonNull(updatedAt, "updatedAt");
        Objects.requireNonNull(providerPaymentId, "providerPaymentId");
        if (reference == null || reference.isBlank()) {
            throw new IllegalArgumentException("payment reference must not be blank");
        }
        if (!amount.isPositive()) {
            throw new IllegalArgumentException("payment amount must be positive");
        }
    }

    /** Creates a new payment in {@link PaymentStatus#PENDING}. */
    public static Payment create(
            TenantId tenantId,
            ProviderId providerId,
            Money amount,
            Creditor creditor,
            String reference,
            Instant now) {
        return new Payment(PaymentId.random(), tenantId, providerId, amount, creditor,
                reference, PaymentStatus.PENDING, now, now, Optional.empty());
    }

    /** Records the provider-side payment reference. */
    public Payment withProviderPaymentId(String providerReference, Instant now) {
        return new Payment(id, tenantId, providerId, amount, creditor, reference,
                status, createdAt, now, Optional.of(providerReference));
    }

    public Payment transitionTo(PaymentStatus target, Instant now) {
        if (!status.canTransitionTo(target)) {
            throw new IllegalStateException(
                    "illegal payment transition " + status + " -> " + target);
        }
        return new Payment(id, tenantId, providerId, amount, creditor, reference,
                target, createdAt, now, providerPaymentId);
    }
}
