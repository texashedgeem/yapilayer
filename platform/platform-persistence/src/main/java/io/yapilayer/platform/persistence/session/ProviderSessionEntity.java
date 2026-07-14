package io.yapilayer.platform.persistence.session;

import io.yapilayer.provider.sdk.ais.ProviderSession;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA mapping for provider sessions (access tokens).
 *
 * <p>Phase 1 limitation: tokens are stored unencrypted (SECURITY.md); encryption-at-rest arrives
 * with platform-security hardening.
 */
@Entity
@Table(name = "provider_sessions")
public class ProviderSessionEntity {

    @Id
    @Column(name = "consent_id")
    private UUID consentId;

    @Column(name = "provider_consent_id", nullable = false)
    private String providerConsentId;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "refresh_token")
    private String refreshToken;

    protected ProviderSessionEntity() {
        // JPA
    }

    public static ProviderSessionEntity of(UUID consentId, ProviderSession session) {
        ProviderSessionEntity entity = new ProviderSessionEntity();
        entity.consentId = consentId;
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
