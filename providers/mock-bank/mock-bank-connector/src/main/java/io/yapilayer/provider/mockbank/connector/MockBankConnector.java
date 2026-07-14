package io.yapilayer.provider.mockbank.connector;

import io.yapilayer.platform.domain.common.ProviderId;
import io.yapilayer.provider.sdk.BankConnector;
import io.yapilayer.provider.sdk.ProviderCapability;
import io.yapilayer.provider.sdk.ProviderDescriptor;
import io.yapilayer.provider.sdk.ais.AisProviderPort;
import io.yapilayer.provider.sdk.pis.PisProviderPort;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

/**
 * Reference bank connector: implements the provider SDK against the standalone
 * mock bank simulator (ADR 0003). Behaviourally modelled on NatWest Group's
 * Open Banking implementation (ADR 0010).
 */
public final class MockBankConnector implements BankConnector {

    public static final ProviderId PROVIDER_ID = new ProviderId("mock-bank");

    private final ProviderDescriptor descriptor;
    private final MockBankAisPort aisPort;
    private final MockBankPisPort pisPort;

    /** Single-URL convenience for local development where both roles coincide. */
    public MockBankConnector(URI baseUrl) {
        this(baseUrl, baseUrl);
    }

    /**
     * @param apiBaseUrl    where the platform reaches the simulator's API
     *                      (e.g. {@code http://mock-bank:8090} inside compose)
     * @param publicBaseUrl where the <em>customer's browser</em> reaches the
     *                      simulator for the authorisation journey
     *                      (e.g. {@code http://localhost:8090})
     */
    public MockBankConnector(URI apiBaseUrl, URI publicBaseUrl) {
        this.descriptor = new ProviderDescriptor(
                PROVIDER_ID,
                "Mock Bank",
                "GB",
                Set.of(ProviderCapability.AIS, ProviderCapability.PIS));
        MockBankHttp http = new MockBankHttp(apiBaseUrl, PROVIDER_ID);
        this.aisPort = new MockBankAisPort(http, publicBaseUrl);
        this.pisPort = new MockBankPisPort(http, publicBaseUrl);
    }

    @Override
    public ProviderDescriptor descriptor() {
        return descriptor;
    }

    @Override
    public Optional<AisProviderPort> aisPort() {
        return Optional.of(aisPort);
    }

    @Override
    public Optional<PisProviderPort> pisPort() {
        return Optional.of(pisPort);
    }
}
