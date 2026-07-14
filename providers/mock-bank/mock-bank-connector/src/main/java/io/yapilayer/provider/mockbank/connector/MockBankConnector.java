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

    /** @param baseUrl the mock-bank-simulator root, e.g. {@code http://localhost:8090} */
    public MockBankConnector(URI baseUrl) {
        this.descriptor = new ProviderDescriptor(
                PROVIDER_ID,
                "Mock Bank",
                "GB",
                Set.of(ProviderCapability.AIS, ProviderCapability.PIS));
        MockBankHttp http = new MockBankHttp(baseUrl, PROVIDER_ID);
        this.aisPort = new MockBankAisPort(http);
        this.pisPort = new MockBankPisPort(http);
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
