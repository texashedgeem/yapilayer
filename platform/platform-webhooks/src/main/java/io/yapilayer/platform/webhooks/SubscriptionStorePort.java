package io.yapilayer.platform.webhooks;

import java.util.List;

/** Persistence port for webhook subscriptions, implemented by platform-persistence. */
public interface SubscriptionStorePort {

    void save(WebhookSubscription subscription);

    List<WebhookSubscription> all();
}
