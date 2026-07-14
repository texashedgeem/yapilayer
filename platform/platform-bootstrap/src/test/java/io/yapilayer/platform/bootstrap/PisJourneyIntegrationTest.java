package io.yapilayer.platform.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import com.sun.net.httpserver.HttpServer;
import io.yapilayer.provider.mockbank.simulator.MockBankSimulatorApplication;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Milestone 5 acceptance test: complete PIS journey through the platform's public API — create
 * payment → authorise at the (simulated) bank → callback submits the payment → status polling to
 * COMPLETED — with signed webhook events received and HMAC-verified by a local receiver.
 */
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PisJourneyIntegrationTest {

    private static final Pattern CODE_PARAM = Pattern.compile("[?&]code=([^&]+)");
    private static final String WEBHOOK_SECRET = "test-webhook-secret-123";

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("yapilayer")
                    .withUsername("yapilayer")
                    .withPassword("test");

    static ConfigurableApplicationContext simulator;

    record ReceivedWebhook(String body, String signature) {}

    static final BlockingQueue<ReceivedWebhook> receivedWebhooks = new LinkedBlockingQueue<>();
    static HttpServer webhookReceiver;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) throws Exception {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        int platformPort;
        try (java.net.ServerSocket socket = new java.net.ServerSocket(0)) {
            platformPort = socket.getLocalPort();
        }
        registry.add("server.port", () -> platformPort);
        registry.add(
                "yapilayer.payments-callback-url",
                () -> "http://localhost:" + platformPort + "/api/v1/payments/callback");

        simulator =
                new SpringApplicationBuilder(MockBankSimulatorApplication.class)
                        .properties(
                                "server.port=0",
                                "spring.autoconfigure.exclude="
                                        + "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
                                        + "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,"
                                        + "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration")
                        .run();
        int simulatorPort =
                simulator.getEnvironment().getProperty("local.server.port", Integer.class);
        registry.add(
                "yapilayer.providers.mock-bank.base-url",
                () -> "http://localhost:" + simulatorPort);

        webhookReceiver = HttpServer.create(new InetSocketAddress(0), 0);
        webhookReceiver.createContext(
                "/hook",
                exchange -> {
                    try (InputStream in = exchange.getRequestBody()) {
                        String body = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                        String signature =
                                exchange.getRequestHeaders().getFirst("X-Yapilayer-Signature");
                        receivedWebhooks.add(new ReceivedWebhook(body, signature));
                    }
                    exchange.sendResponseHeaders(204, -1);
                    exchange.close();
                });
        webhookReceiver.start();
    }

    @AfterAll
    static void tearDown() {
        if (simulator != null) {
            simulator.close();
        }
        if (webhookReceiver != null) {
            webhookReceiver.stop(0);
        }
    }

    @Autowired private TestRestTemplate rest;

    @Test
    @SuppressWarnings("unchecked")
    void fullPisJourneyWithSignedWebhooks() throws Exception {
        // 0. Register the webhook subscription
        int hookPort = webhookReceiver.getAddress().getPort();
        ResponseEntity<Map> hook =
                rest.postForEntity(
                        "/api/v1/webhooks",
                        Map.of(
                                "url",
                                "http://localhost:" + hookPort + "/hook",
                                "secret",
                                WEBHOOK_SECRET),
                        Map.class);
        assertThat(hook.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // 1. Create the payment
        ResponseEntity<Map> created =
                rest.postForEntity(
                        "/api/v1/payments",
                        Map.of(
                                "providerId",
                                "mock-bank",
                                "amount",
                                "25.00",
                                "currency",
                                "GBP",
                                "creditor",
                                Map.of(
                                        "name", "Acme Ltd",
                                        "scheme", "UK.OBIE.SortCodeAccountNumber",
                                        "identification", "20000012345678"),
                                "reference",
                                "INV-001",
                                "redirectUri",
                                "http://client.example/paid"),
                        Map.class);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String paymentId = (String) created.getBody().get("paymentId");
        String authorisationUrl = (String) created.getBody().get("authorisationUrl");
        assertThat(created.getBody().get("status")).isEqualTo("PENDING");

        // 2. Customer authorises the payment at the bank
        URI bankRedirect = approveAtBank(authorisationUrl);

        // 3. Platform callback: exchanges code, submits payment, transitions to AUTHORISED
        ResponseEntity<Void> callback =
                noRedirectClient()
                        .exchange(
                                org.springframework.http.RequestEntity.get(bankRedirect).build(),
                                Void.class);
        assertThat(callback.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        String clientRedirect = callback.getHeaders().getFirst(HttpHeaders.LOCATION);
        assertThat(clientRedirect).startsWith("http://client.example/paid");
        assertThat(clientRedirect).contains("status=AUTHORISED");

        // 4. Status polling drives the lifecycle to COMPLETED (simulator advances
        //    one step per poll: first poll confirms AUTHORISED, second completes)
        ResponseEntity<Map> afterFirstPoll =
                rest.getForEntity("/api/v1/payments/" + paymentId, Map.class);
        assertThat(afterFirstPoll.getBody().get("status")).isEqualTo("AUTHORISED");
        ResponseEntity<Map> afterSecondPoll =
                rest.getForEntity("/api/v1/payments/" + paymentId, Map.class);
        assertThat(afterSecondPoll.getBody().get("status")).isEqualTo("COMPLETED");

        // 5. Webhook events arrived for AUTHORISED and COMPLETED, each correctly signed
        ReceivedWebhook first = receivedWebhooks.poll(15, TimeUnit.SECONDS);
        ReceivedWebhook second = receivedWebhooks.poll(15, TimeUnit.SECONDS);
        assertThat(first).isNotNull();
        assertThat(second).isNotNull();

        List<String> statuses = List.of(statusOf(first.body()), statusOf(second.body()));
        assertThat(statuses).containsExactly("AUTHORISED", "COMPLETED");
        for (ReceivedWebhook webhook : List.of(first, second)) {
            assertThat(webhook.signature())
                    .isEqualTo("sha256=" + hmac(WEBHOOK_SECRET, webhook.body()));
            assertThat(webhook.body()).contains("payment.status.changed");
        }

        // 6. Terminal payments are not re-polled — status stays COMPLETED
        ResponseEntity<Map> terminal =
                rest.getForEntity("/api/v1/payments/" + paymentId, Map.class);
        assertThat(terminal.getBody().get("status")).isEqualTo("COMPLETED");
    }

    private static String statusOf(String eventJson) {
        Matcher m = Pattern.compile("\"status\":\"([A-Z]+)\"").matcher(eventJson);
        return m.find() ? m.group(1) : "?";
    }

    private static String hmac(String secret, String body) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return HexFormat.of().formatHex(mac.doFinal(body.getBytes(StandardCharsets.UTF_8)));
    }

    private static org.springframework.web.client.RestTemplate noRedirectClient() {
        org.springframework.http.client.SimpleClientHttpRequestFactory factory =
                new org.springframework.http.client.SimpleClientHttpRequestFactory() {
                    @Override
                    protected void prepareConnection(
                            java.net.HttpURLConnection connection, String httpMethod)
                            throws java.io.IOException {
                        super.prepareConnection(connection, httpMethod);
                        connection.setInstanceFollowRedirects(false);
                    }
                };
        return new org.springframework.web.client.RestTemplate(factory);
    }

    private URI approveAtBank(String authorisationUrl) {
        URI authorise = URI.create(authorisationUrl);
        Map<String, String> params =
                java.util.Arrays.stream(authorise.getRawQuery().split("&"))
                        .map(p -> p.split("=", 2))
                        .collect(
                                java.util.stream.Collectors.toMap(
                                        p -> p[0],
                                        p ->
                                                java.net.URLDecoder.decode(
                                                        p[1], StandardCharsets.UTF_8)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("consent_id", params.get("consent_id"));
        form.add("redirect_uri", params.get("redirect_uri"));
        form.add("state", params.get("state"));
        form.add("decision", "approve");

        String bankRoot = authorise.getScheme() + "://" + authorise.getAuthority();
        ResponseEntity<Void> response =
                new org.springframework.web.client.RestTemplate()
                        .postForEntity(
                                bankRoot + "/oauth/authorize/decision",
                                new HttpEntity<>(form, headers),
                                Void.class);
        return URI.create(response.getHeaders().getFirst(HttpHeaders.LOCATION));
    }
}
