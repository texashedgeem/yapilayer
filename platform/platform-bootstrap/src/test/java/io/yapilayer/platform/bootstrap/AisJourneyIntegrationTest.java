package io.yapilayer.platform.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import io.yapilayer.provider.mockbank.simulator.MockBankSimulatorApplication;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
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
 * Milestone 4 acceptance test: the complete AIS journey through the platform's public API — create
 * consent → customer authorises at the (simulated) bank → platform callback → accounts → balances →
 * paginated transactions — backed by real PostgreSQL (Testcontainers) and the real mock bank
 * simulator.
 */
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AisJourneyIntegrationTest {

    private static final Pattern CODE_PARAM = Pattern.compile("[?&]code=([^&]+)");
    private static final Pattern CONSENT_ID_PARAM = Pattern.compile("[?&]consentId=([^&]+)");

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("yapilayer")
                    .withUsername("yapilayer")
                    .withPassword("test");

    static ConfigurableApplicationContext simulator;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) throws Exception {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        // The callback URL is baked into the consent journey before the web
        // server exists, so pre-allocate the platform's port instead of using
        // a random one.
        int platformPort;
        try (java.net.ServerSocket socket = new java.net.ServerSocket(0)) {
            platformPort = socket.getLocalPort();
        }
        registry.add("server.port", () -> platformPort);
        registry.add(
                "yapilayer.callback-url",
                () -> "http://localhost:" + platformPort + "/api/v1/callback");

        // The simulator shares this test JVM's classpath (JPA, Flyway) and would
        // otherwise try to connect to the platform's database — exclude those.
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
    }

    @AfterAll
    static void stopSimulator() {
        if (simulator != null) {
            simulator.close();
        }
    }

    @LocalServerPort private int port;

    @Autowired private TestRestTemplate rest;

    @Autowired private Environment environment;

    @Test
    @SuppressWarnings("unchecked")
    void fullAisJourneyThroughPlatformApi() {
        // 0. Provider discovery
        ResponseEntity<List> providers = rest.getForEntity("/api/v1/providers", List.class);
        assertThat(providers.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(providers.getBody()).hasSize(1);

        // 1. Create consent
        ResponseEntity<Map> created =
                rest.postForEntity(
                        "/api/v1/consents",
                        Map.of(
                                "providerId",
                                "mock-bank",
                                "permissions",
                                List.of("READ_ACCOUNTS", "READ_BALANCES", "READ_TRANSACTIONS"),
                                "redirectUri",
                                "http://client.example/done"),
                        Map.class);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Map<String, Object> consentBody = created.getBody();
        String consentId = (String) consentBody.get("consentId");
        String authorisationUrl = (String) consentBody.get("authorisationUrl");
        assertThat(consentBody.get("status")).isEqualTo("AWAITING_AUTHORISATION");
        assertThat(authorisationUrl).contains("/oauth/authorize");

        // 2. Customer approves at the bank (posting the decision form as the browser would);
        //    the bank redirects to the platform callback with code + state.
        URI bankRedirect = approveAtBank(authorisationUrl);
        assertThat(bankRedirect.toString()).contains("/api/v1/callback");

        // 3. Follow the redirect into the platform callback; platform completes the
        //    exchange and redirects on to the client app. Redirect-following is
        //    disabled so the 302 to the (fake) client app is observable.
        ResponseEntity<Void> callback =
                noRedirectClient()
                        .exchange(
                                org.springframework.http.RequestEntity.get(bankRedirect).build(),
                                Void.class);
        assertThat(callback.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        String clientRedirect = callback.getHeaders().getFirst(HttpHeaders.LOCATION);
        assertThat(clientRedirect).startsWith("http://client.example/done");
        assertThat(clientRedirect).contains("status=AUTHORISED");
        Matcher consentMatcher = CONSENT_ID_PARAM.matcher(clientRedirect);
        assertThat(consentMatcher.find()).isTrue();
        assertThat(consentMatcher.group(1)).isEqualTo(consentId);

        // 4. Consent is now authorised
        ResponseEntity<Map> consent = rest.getForEntity("/api/v1/consents/" + consentId, Map.class);
        assertThat(consent.getBody().get("status")).isEqualTo("AUTHORISED");

        // 5. Accounts
        ResponseEntity<List> accounts =
                rest.getForEntity("/api/v1/consents/" + consentId + "/accounts", List.class);
        assertThat(accounts.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(accounts.getBody()).hasSize(2);
        Map<String, Object> account = (Map<String, Object>) accounts.getBody().get(0);
        String accountId = (String) account.get("accountId");

        // 6. Balances
        ResponseEntity<List> balances =
                rest.getForEntity(
                        "/api/v1/consents/" + consentId + "/accounts/" + accountId + "/balances",
                        List.class);
        assertThat(balances.getBody()).hasSize(2);

        // 7. Transactions with pagination
        ResponseEntity<Map> page1 =
                rest.getForEntity(
                        "/api/v1/consents/"
                                + consentId
                                + "/accounts/acc-001/transactions?pageSize=5",
                        Map.class);
        assertThat((List<?>) page1.getBody().get("transactions")).hasSize(5);
        String nextPageKey = (String) page1.getBody().get("nextPageKey");
        assertThat(nextPageKey).isNotNull();

        ResponseEntity<Map> page2 =
                rest.getForEntity(
                        "/api/v1/consents/"
                                + consentId
                                + "/accounts/acc-001/transactions?pageSize=5&pageKey="
                                + nextPageKey,
                        Map.class);
        assertThat((List<?>) page2.getBody().get("transactions")).hasSize(2);
        assertThat(page2.getBody().get("nextPageKey")).isNull();

        // 8. Data access without authorisation is rejected
        ResponseEntity<Map> freshConsent =
                rest.postForEntity(
                        "/api/v1/consents",
                        Map.of(
                                "providerId",
                                "mock-bank",
                                "permissions",
                                List.of("READ_ACCOUNTS"),
                                "redirectUri",
                                "http://client.example/done"),
                        Map.class);
        String unauthorisedId = (String) freshConsent.getBody().get("consentId");
        ResponseEntity<Map> denied =
                rest.getForEntity("/api/v1/consents/" + unauthorisedId + "/accounts", Map.class);
        assertThat(denied.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
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

    /** Simulates the customer's browser: loads nothing, just posts approval to the bank. */
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
                                                        p[1],
                                                        java.nio.charset.StandardCharsets.UTF_8)));

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
