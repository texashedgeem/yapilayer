package io.yapilayer.platform.persistence.payment;

import io.yapilayer.provider.sdk.ais.ProviderSession;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/** JPA mapping for payment provider sessions (same Phase 1 plaintext-token limitation as AIS). */
@Entity
@Table(name = "payment_sessions")
public class PaymentSessionEntity {

    @Id
    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "provider_consent_id", nullable = false)
    private String providerConsentId;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "refresh_token")
    private String refreshToken;

    protected PaymentSessionEntity() {
        // JPA
    }

    public static PaymentSessionEntity of(UUID paymentId, ProviderSession session) {
        PaymentSessionEntity entity = new PaymentSessionEntity();
        entity.paymentId = paymentId;
        entity.providerConsentId = session.providerConsentId();
        entity.accessToken = session.accessToken();
        entity.expiresAt = session.expiresAt();
        entity.refreshToken = session.refreshToken().orElse(null);
        return entity;
    }

    public ProviderSession toSession() {
        return new ProviderSession(
                providerConsentId, accessToken, expiresAt, Optional.ofNullable(refreshToken));
    }
}
