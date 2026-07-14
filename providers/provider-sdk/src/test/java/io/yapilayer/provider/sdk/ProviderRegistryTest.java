package io.yapilayer.provider.sdk;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import io.yapilayer.platform.domain.common.ProviderId;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProviderRegistryTest {

    private static BankConnector connector(String id) {
        ProviderDescriptor descriptor =
                new ProviderDescriptor(
                        new ProviderId(id), "Test Bank", "GB", Set.of(ProviderCapability.AIS));
        return () -> descriptor;
    }

    @Test
    void registersAndResolvesById() {
        ProviderRegistry registry = new ProviderRegistry();
        BankConnector connector = connector("mock-bank");
        registry.register(connector);

        assertThat(registry.byId(new ProviderId("mock-bank"))).contains(connector);
        assertThat(registry.all()).containsExactly(connector);
    }

    @Test
    void unknownIdResolvesEmpty() {
        assertThat(new ProviderRegistry().byId(new ProviderId("nope"))).isEmpty();
    }

    @Test
    void duplicateRegistrationFails() {
        ProviderRegistry registry = new ProviderRegistry();
        registry.register(connector("mock-bank"));
        assertThatIllegalStateException()
                .isThrownBy(() -> registry.register(connector("mock-bank")));
    }

    @Test
    void portsDefaultToEmptyUnlessImplemented() {
        BankConnector connector = connector("mock-bank");
        assertThat(connector.aisPort()).isEmpty();
        assertThat(connector.pisPort()).isEmpty();
    }
}
