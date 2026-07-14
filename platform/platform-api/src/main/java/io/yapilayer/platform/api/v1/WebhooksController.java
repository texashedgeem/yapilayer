package io.yapilayer.platform.api.v1;

import io.yapilayer.platform.webhooks.SubscriptionStorePort;
import io.yapilayer.platform.webhooks.WebhookSubscription;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/** Webhook subscription management. Secrets are write-only — never returned. */
@RestController
@RequestMapping("/api/v1/webhooks")
public class WebhooksController {

    public record CreateSubscriptionRequest(String url, String secret) {}

    public record SubscriptionDto(UUID id, String url) {}

    private final SubscriptionStorePort subscriptions;

    public WebhooksController(SubscriptionStorePort subscriptions) {
        this.subscriptions = subscriptions;
    }

    @PostMapping
    public ResponseEntity<SubscriptionDto> create(
            @RequestBody CreateSubscriptionRequest request) {
        WebhookSubscription subscription = new WebhookSubscription(
                UUID.randomUUID(), URI.create(request.url()), request.secret());
        subscriptions.save(subscription);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SubscriptionDto(subscription.id(), subscription.url().toString()));
    }

    @GetMapping
    public List<SubscriptionDto> list() {
        return subscriptions.all().stream()
                .map(s -> new SubscriptionDto(s.id(), s.url().toString()))
                .toList();
    }
}
