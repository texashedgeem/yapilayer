package io.yapilayer.provider.sdk;

import io.yapilayer.provider.sdk.ais.AisProviderPort;
import io.yapilayer.provider.sdk.pis.PisProviderPort;

import java.util.Optional;

/**
 * The provider plugin root (ADR 0004). One implementation per bank/group.
 *
 * <p>A connector returns a port only for capabilities it declares in its
 * {@link ProviderDescriptor}; the platform discovers capabilities rather than
 * assuming them. Implementations must be stateless with respect to consent —
 * all consent/session state is threaded through port method parameters.
 */
public interface BankConnector {

    ProviderDescriptor descriptor();

    /** Present if and only if the descriptor declares {@link ProviderCapability#AIS}. */
    default Optional<AisProviderPort> aisPort() {
        return Optional.empty();
    }

    /** Present if and only if the descriptor declares {@link ProviderCapability#PIS}. */
    default Optional<PisProviderPort> pisPort() {
        return Optional.empty();
    }
}
