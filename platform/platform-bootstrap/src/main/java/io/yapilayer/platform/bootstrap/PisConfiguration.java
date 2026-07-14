package io.yapilayer.platform.bootstrap;

import io.yapilayer.platform.application.pis.PaymentEventPublisher;
import io.yapilayer.platform.application.pis.PaymentRepositoryPort;
import io.yapilayer.platform.application.pis.PaymentSessionStorePort;
import io.yapilayer.platform.application.pis.PisService;
import io.yapilayer.platform.webhooks.SubscriptionStorePort;
import io.yapilayer.platform.webhooks.WebhookDispatcher;
import io.yapilayer.provider.sdk.ProviderRegistry;
import java.net.URI;
import java.time.Clock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Wires the PIS application service and the webhook dispatcher. */
@Configuration
public class PisConfiguration {

    @Bean
    public PaymentEventPublisher paymentEventPublisher(SubscriptionStorePort subscriptions) {
        return new WebhookDispatcher(subscriptions);
    }

    @Bean
    public PisService pisService(
            ProviderRegistry providers,
            PaymentRepositoryPort payments,
            PaymentSessionStorePort sessions,
            PaymentEventPublisher events,
            @Value("${yapilayer.payments-callback-url}") URI callbackUrl,
            Clock clock) {
        return new PisService(providers, payments, sessions, events, callbackUrl, clock);
    }
}
