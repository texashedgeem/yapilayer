package io.yapilayer.platform.bootstrap;

import io.yapilayer.platform.application.ais.AisService;
import io.yapilayer.platform.application.ais.ConsentRepositoryPort;
import io.yapilayer.platform.application.ais.SessionStorePort;
import io.yapilayer.provider.sdk.ProviderRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.time.Clock;

/** Wires the framework-free application services as Spring beans. */
@Configuration
public class AisConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public AisService aisService(ProviderRegistry providers,
                                 ConsentRepositoryPort consents,
                                 SessionStorePort sessions,
                                 @Value("${yapilayer.callback-url}") URI callbackUrl,
                                 Clock clock) {
        return new AisService(providers, consents, sessions, callbackUrl, clock);
    }
}
