package io.yapilayer.platform.persistence.webhook;

import io.yapilayer.platform.webhooks.SubscriptionStorePort;
import io.yapilayer.platform.webhooks.WebhookSubscription;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Implements the webhooks subscription port on Spring Data JPA. */
@Component
@Transactional
public class SubscriptionStoreAdapter implements SubscriptionStorePort {

    private final WebhookSubscriptionJpaRepository repository;

    public SubscriptionStoreAdapter(WebhookSubscriptionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(WebhookSubscription subscription) {
        repository.save(WebhookSubscriptionEntity.fromDomain(subscription));
    }

    @Override
    @Transactional(readOnly = true)
    public List<WebhookSubscription> all() {
        return repository.findAll().stream()
                .map(WebhookSubscriptionEntity::toDomain)
                .toList();
    }
}
