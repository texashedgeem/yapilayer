package io.yapilayer.platform.webhooks;

import java.net.URI;
import java.util.Objects;
import java.util.UUID;

/**
 * A registered webhook endpoint. The secret signs every delivery
 * (HMAC-SHA256 over the raw body — see ADR 0011) so receivers can verify
 * authenticity.
 */
public record WebhookSubscription(UUID id, URI url, String secret) {

    public WebhookSubscription {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(url, "url");
        if (secret == null || secret.length() < 16) {
            throw new IllegalArgumentException("webhook secret must be at least 16 characters");
        }
    }
}
