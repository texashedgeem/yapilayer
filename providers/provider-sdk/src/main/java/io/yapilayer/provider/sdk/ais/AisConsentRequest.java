package io.yapilayer.provider.sdk.ais;

import io.yapilayer.platform.domain.consent.Permission;

import java.net.URI;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

/**
 * Request to create a provider-side AIS consent.
 *
 * @param redirectUri where the bank sends the customer after authorisation
 * @param state       opaque anti-CSRF value the bank must echo back on redirect
 */
public record AisConsentRequest(
        Set<Permission> permissions,
        Instant expiresAt,
        URI redirectUri,
        String state) {

    public AisConsentRequest {
        Objects.requireNonNull(expiresAt, "expiresAt");
        Objects.requireNonNull(redirectUri, "redirectUri");
        Objects.requireNonNull(state, "state");
        if (permissions == null || permissions.isEmpty()) {
            throw new IllegalArgumentException("at least one permission required");
        }
        permissions = Set.copyOf(permissions);
    }
}
