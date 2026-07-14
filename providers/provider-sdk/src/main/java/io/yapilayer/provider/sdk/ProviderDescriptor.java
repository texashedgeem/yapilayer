package io.yapilayer.provider.sdk;

import io.yapilayer.platform.domain.common.ProviderId;

import java.util.Objects;
import java.util.Set;

/**
 * Identity and declared capabilities of a bank connector.
 *
 * @param countryCode ISO 3166-1 alpha-2, e.g. {@code GB}
 */
public record ProviderDescriptor(
        ProviderId id,
        String displayName,
        String countryCode,
        Set<ProviderCapability> capabilities) {

    public ProviderDescriptor {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(countryCode, "countryCode");
        if (displayName == null || displayName.isBlank()) {
            throw new IllegalArgumentException("displayName must not be blank");
        }
        if (capabilities == null || capabilities.isEmpty()) {
            throw new IllegalArgumentException("a provider must declare at least one capability");
        }
        capabilities = Set.copyOf(capabilities);
    }

    public boolean supports(ProviderCapability capability) {
        return capabilities.contains(capability);
    }
}
