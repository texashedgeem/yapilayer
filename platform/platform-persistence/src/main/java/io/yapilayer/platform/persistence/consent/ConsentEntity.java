package io.yapilayer.platform.persistence.consent;

import io.yapilayer.platform.domain.common.ProviderId;
import io.yapilayer.platform.domain.common.TenantId;
import io.yapilayer.platform.domain.consent.Consent;
import io.yapilayer.platform.domain.consent.ConsentId;
import io.yapilayer.platform.domain.consent.ConsentStatus;
import io.yapilayer.platform.domain.consent.Permission;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JPA mapping for consents. Domain objects stay JPA-free (ADR 0007); this entity converts to/from
 * {@link Consent} at the persistence boundary.
 */
@Entity
@Table(name = "consents")
public class ConsentEntity {

    @Id private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    /** Comma-separated {@link Permission} names. */
    @Column(nullable = false)
    private String permissions;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsentStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "provider_consent_id")
    private String providerConsentId;

    @Column(name = "oauth_state", unique = true)
    private String oauthState;

    @Column(name = "client_redirect_uri")
    private String clientRedirectUri;

    protected ConsentEntity() {
        // JPA
    }

    public static ConsentEntity fromDomain(
            Consent consent, String oauthState, URI clientRedirectUri) {
        ConsentEntity entity = new ConsentEntity();
        entity.id = consent.id().value();
        entity.oauthState = oauthState;
        entity.clientRedirectUri = clientRedirectUri.toString();
        entity.applyDomain(consent);
        return entity;
    }

    /** Updates mutable fields from the domain aggregate. */
    public void applyDomain(Consent consent) {
        this.tenantId = consent.tenantId().value();
        this.providerId = consent.providerId().value();
        this.permissions =
                consent.permissions().stream()
                        .map(Enum::name)
                        .sorted()
                        .collect(Collectors.joining(","));
        this.status = consent.status();
        this.createdAt = consent.createdAt();
        this.expiresAt = consent.expiresAt();
        this.providerConsentId = consent.providerConsentId().orElse(null);
    }

    public Consent toDomain() {
        Set<Permission> perms =
                Arrays.stream(permissions.split(","))
                        .map(Permission::valueOf)
                        .collect(Collectors.toSet());
        return new Consent(
                new ConsentId(id),
                new TenantId(tenantId),
                new ProviderId(providerId),
                perms,
                status,
                createdAt,
                expiresAt,
                Optional.ofNullable(providerConsentId));
    }

    public UUID getId() {
        return id;
    }

    public String getOauthState() {
        return oauthState;
    }

    public URI getClientRedirectUri() {
        return URI.create(clientRedirectUri);
    }
}
