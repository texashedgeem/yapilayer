package io.yapilayer.platform.persistence.webhook;

import io.yapilayer.platform.webhooks.WebhookSubscription;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.net.URI;
import java.util.UUID;

/** JPA mapping for webhook subscriptions. */
@Entity
@Table(name = "webhook_subscriptions")
public class WebhookSubscriptionEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String secret;

    protected WebhookSubscriptionEntity() {
        // JPA
    }

    public static WebhookSubscriptionEntity fromDomain(WebhookSubscription subscription) {
        WebhookSubscriptionEntity entity = new WebhookSubscriptionEntity();
        entity.id = subscription.id();
        entity.url = subscription.url().toString();
        entity.secret = subscription.secret();
        return entity;
    }

    public WebhookSubscription toDomain() {
        return new WebhookSubscription(id, URI.create(url), secret);
    }
}
