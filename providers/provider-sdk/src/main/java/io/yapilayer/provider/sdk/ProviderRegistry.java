package io.yapilayer.provider.sdk;

import io.yapilayer.platform.domain.common.ProviderId;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Runtime registry of bank connectors. Connectors self-register at startup (each connector module
 * ships a Spring configuration that calls {@link #register}); the platform resolves connectors only
 * through this registry, never by direct reference.
 */
public final class ProviderRegistry {

    private final Map<ProviderId, BankConnector> connectors = new ConcurrentHashMap<>();

    /**
     * @throws IllegalStateException if a connector with the same id is already registered
     */
    public void register(BankConnector connector) {
        ProviderId id = connector.descriptor().id();
        BankConnector previous = connectors.putIfAbsent(id, connector);
        if (previous != null) {
            throw new IllegalStateException("provider already registered: " + id.value());
        }
    }

    public Optional<BankConnector> byId(ProviderId id) {
        return Optional.ofNullable(connectors.get(id));
    }

    public Collection<BankConnector> all() {
        return List.copyOf(connectors.values());
    }
}
