package io.yapilayer.provider.sdk.ais;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * An authorised session against a provider: the access credential obtained by
 * exchanging the customer's authorisation code, tied to a consent.
 *
 * <p>Held by the platform (platform-security manages storage/refresh), passed
 * into port calls — connectors never store it.
 */
public record ProviderSession(
        String providerConsentId,
        String accessToken,
        Instant expiresAt,
        Optional<String> refreshToken) {

    public ProviderSession {
        Objects.requireNonNull(providerConsentId, "providerConsentId");
        Objects.requireNonNull(accessToken, "accessToken");
        Objects.requireNonNull(expiresAt, "expiresAt");
        Objects.requireNonNull(refreshToken, "refreshToken");
    }
}
