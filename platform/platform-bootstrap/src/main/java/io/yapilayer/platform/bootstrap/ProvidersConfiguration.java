package io.yapilayer.platform.bootstrap;

import io.yapilayer.provider.mockbank.connector.MockBankConnector;
import io.yapilayer.provider.sdk.ProviderRegistry;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wires available bank connectors into the {@link ProviderRegistry}.
 *
 * <p>Phase 1 registers connectors explicitly here; per-module auto-configuration is tracked in
 * TECH_DEBT.md.
 */
@Configuration
public class ProvidersConfiguration {

    @Bean
    public ProviderRegistry providerRegistry(
            @Value("${yapilayer.providers.mock-bank.base-url:http://localhost:8090}")
                    URI mockBankBaseUrl,
            @Value(
                            "${yapilayer.providers.mock-bank.public-base-url:${yapilayer.providers.mock-bank.base-url:http://localhost:8090}}")
                    URI mockBankPublicBaseUrl) {
        ProviderRegistry registry = new ProviderRegistry();
        registry.register(new MockBankConnector(mockBankBaseUrl, mockBankPublicBaseUrl));
        return registry;
    }
}
