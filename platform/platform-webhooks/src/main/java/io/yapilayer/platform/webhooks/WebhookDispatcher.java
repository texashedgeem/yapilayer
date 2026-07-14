package io.yapilayer.platform.webhooks;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.yapilayer.platform.application.pis.PaymentEventPublisher;
import io.yapilayer.platform.domain.payment.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Delivers payment events to all registered webhook subscriptions (ADR 0011).
 *
 * <p>At-least-once semantics: each delivery is attempted up to
 * {@link #MAX_ATTEMPTS} times with exponential backoff; failures after the
 * final attempt are logged and dropped. A durable outbox is tracked in
 * TECH_DEBT.md. Signature: {@code X-Yapilayer-Signature: sha256=<hex hmac>}
 * computed over the raw request body with the subscription's secret.
 */
public class WebhookDispatcher implements PaymentEventPublisher {

    static final String SIGNATURE_HEADER = "X-Yapilayer-Signature";
    private static final int MAX_ATTEMPTS = 3;
    private static final Duration[] BACKOFF =
            {Duration.ZERO, Duration.ofSeconds(1), Duration.ofSeconds(5)};

    private static final Logger log = LoggerFactory.getLogger(WebhookDispatcher.class);

    private final SubscriptionStorePort subscriptions;
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private final ObjectMapper mapper = new ObjectMapper();
    private final ScheduledExecutorService executor =
            Executors.newSingleThreadScheduledExecutor(runnable -> {
                Thread thread = new Thread(runnable, "webhook-dispatcher");
                thread.setDaemon(true);
                return thread;
            });

    public WebhookDispatcher(SubscriptionStorePort subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Override
    public void paymentStatusChanged(Payment payment) {
        String body = eventJson(payment);
        List<WebhookSubscription> targets = subscriptions.all();
        for (WebhookSubscription subscription : targets) {
            deliver(subscription, body, 1);
        }
    }

    private void deliver(WebhookSubscription subscription, String body, int attempt) {
        executor.schedule(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder(subscription.url())
                        .header("Content-Type", "application/json")
                        .header(SIGNATURE_HEADER, "sha256=" + hmac(subscription.secret(), body))
                        .timeout(Duration.ofSeconds(10))
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();
                HttpResponse<Void> response =
                        client.send(request, HttpResponse.BodyHandlers.discarding());
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    return;
                }
                retryOrGiveUp(subscription, body, attempt,
                        "HTTP " + response.statusCode());
            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                retryOrGiveUp(subscription, body, attempt, e.toString());
            }
        }, BACKOFF[attempt - 1].toMillis(), TimeUnit.MILLISECONDS);
    }

    private void retryOrGiveUp(WebhookSubscription subscription, String body,
                               int attempt, String reason) {
        if (attempt < MAX_ATTEMPTS) {
            log.warn("webhook delivery to {} failed (attempt {}/{}): {} — retrying",
                    subscription.url(), attempt, MAX_ATTEMPTS, reason);
            deliver(subscription, body, attempt + 1);
        } else {
            log.error("webhook delivery to {} abandoned after {} attempts: {}",
                    subscription.url(), MAX_ATTEMPTS, reason);
        }
    }

    private String eventJson(Payment payment) {
        try {
            return mapper.writeValueAsString(Map.of(
                    "type", "payment.status.changed",
                    "occurredAt", Instant.now().toString(),
                    "data", Map.of(
                            "paymentId", payment.id().value().toString(),
                            "providerId", payment.providerId().value(),
                            "status", payment.status().name(),
                            "amount", payment.amount().amount().toPlainString(),
                            "currency", payment.amount().currency().getCurrencyCode(),
                            "reference", payment.reference())));
        } catch (Exception e) {
            throw new IllegalStateException("failed to serialise webhook event", e);
        }
    }

    static String hmac(String secret, String body) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return HexFormat.of().formatHex(mac.doFinal(body.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("HMAC failure", e);
        }
    }
}
