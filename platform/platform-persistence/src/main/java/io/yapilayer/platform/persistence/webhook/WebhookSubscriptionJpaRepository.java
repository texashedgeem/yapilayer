package io.yapilayer.platform.persistence.webhook;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WebhookSubscriptionJpaRepository
        extends JpaRepository<WebhookSubscriptionEntity, UUID> {
}
