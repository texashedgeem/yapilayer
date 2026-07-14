package io.yapilayer.platform.domain.consent;

import io.yapilayer.platform.domain.common.ProviderId;
import io.yapilayer.platform.domain.common.TenantId;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * An AIS consent: the customer's permission for the platform to access account
 * data at a specific provider.
 *
 * <p>Immutable — lifecycle transitions return a new instance and reject illegal
 * moves (e.g. authorising an expired consent).
 *
 * @param providerConsentId the provider-side consent reference, present once the
 *                          provider has created its consent resource
 */
public record Consent(
        ConsentId id,
        TenantId tenantId,
        ProviderId providerId,
        Set<Permission> permissions,
        ConsentStatus status,
        Instant createdAt,
        Instant expiresAt,
        Optional<String> providerConsentId) {

    public Consent {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(tenantId, "tenantId");
        Objects.requireNonNull(providerId, "providerId");
        Objects.requireNonNull(status, "status");
        Objects.requireNonNull(createdAt, "createdAt");
        Objects.requireNonNull(expiresAt, "expiresAt");
        Objects.requireNonNull(providerConsentId, "providerConsentId");
        if (permissions == null || permissions.isEmpty()) {
            throw new IllegalArgumentException("consent requires at least one permission");
        }
        if (!expiresAt.isAfter(createdAt)) {
            throw new IllegalArgumentException("expiresAt must be after createdAt");
        }
        permissions = Set.copyOf(permissions);
    }

    /** Creates a new consent awaiting customer authorisation. */
    public static Consent create(
            TenantId tenantId,
            ProviderId providerId,
            Set<Permission> permissions,
            Instant now,
            Instant expiresAt) {
        return new Consent(
                ConsentId.random(),
                tenantId,
                providerId,
                permissions,
                ConsentStatus.AWAITING_AUTHORISATION,
                now,
                expiresAt,
                Optional.empty());
    }

    /** Records the provider-side consent reference once the provider resource exists. */
    public Consent withProviderConsentId(String reference) {
        return new Consent(id, tenantId, providerId, permissions, status,
                createdAt, expiresAt, Optional.of(reference));
    }

    public Consent authorise(Instant now) {
        requireStatus(ConsentStatus.AWAITING_AUTHORISATION, "authorise");
        requireNotExpired(now);
        return withStatus(ConsentStatus.AUTHORISED);
    }

    public Consent reject() {
        requireStatus(ConsentStatus.AWAITING_AUTHORISATION, "reject");
        return withStatus(ConsentStatus.REJECTED);
    }

    /** Customer withdraws an authorised consent. */
    public Consent revoke() {
        requireStatus(ConsentStatus.AUTHORISED, "revoke");
        return withStatus(ConsentStatus.REVOKED);
    }

    /** Marks the consent expired; legal from any non-terminal state. */
    public Consent expire() {
        if (status.isTerminal()) {
            throw new IllegalStateException("cannot expire a consent in terminal state " + status);
        }
        return withStatus(ConsentStatus.EXPIRED);
    }

    public boolean isExpired(Instant now) {
        return !now.isBefore(expiresAt);
    }

    private void requireStatus(ConsentStatus required, String action) {
        if (status != required) {
            throw new IllegalStateException(
                    "cannot " + action + " a consent in status " + status);
        }
    }

    private void requireNotExpired(Instant now) {
        if (isExpired(now)) {
            throw new IllegalStateException("consent expired at " + expiresAt);
        }
    }

    private Consent withStatus(ConsentStatus newStatus) {
        return new Consent(id, tenantId, providerId, permissions, newStatus,
                createdAt, expiresAt, providerConsentId);
    }
}
