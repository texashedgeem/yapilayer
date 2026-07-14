package io.yapilayer.platform.persistence.webhook;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebhookSubscriptionJpaRepository
        extends JpaRepository<WebhookSubscriptionEntity, UUID> {}
