package io.yapilayer.provider.mockbank.connector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.yapilayer.platform.domain.common.ProviderId;
import io.yapilayer.provider.sdk.ProviderException;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Minimal JSON-over-HTTP helper for the mock bank API. Deliberately uses only the JDK HTTP client
 * plus Jackson — connectors do not require Spring.
 */
final class MockBankHttp {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final URI baseUrl;
    private final ProviderId providerId;

    MockBankHttp(URI baseUrl, ProviderId providerId) {
        this.baseUrl = baseUrl;
        this.providerId = providerId;
    }

    URI resolve(String path) {
        return baseUrl.resolve(path);
    }

    JsonNode getJsonNoAuth(String path) {
        return send(HttpRequest.newBuilder(resolve(path)).GET().build());
    }

    JsonNode getJson(String path, String bearerToken) {
        HttpRequest request =
                HttpRequest.newBuilder(resolve(path))
                        .header("Authorization", "Bearer " + bearerToken)
                        .GET()
                        .build();
        return send(request);
    }

    JsonNode postJson(String path, Object body, String bearerToken) {
        try {
            HttpRequest.Builder builder =
                    HttpRequest.newBuilder(resolve(path))
                            .header("Content-Type", "application/json")
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            mapper.writeValueAsString(body)));
            if (bearerToken != null) {
                builder.header("Authorization", "Bearer " + bearerToken);
            }
            return send(builder.build());
        } catch (IOException e) {
            throw new ProviderException(providerId, "failed to serialise request body", e);
        }
    }

    JsonNode postForm(String path, Map<String, String> form) {
        String encoded =
                form.entrySet().stream()
                        .map(
                                e ->
                                        URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8)
                                                + "="
                                                + URLEncoder.encode(
                                                        e.getValue(), StandardCharsets.UTF_8))
                        .collect(Collectors.joining("&"));
        HttpRequest request =
                HttpRequest.newBuilder(resolve(path))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .POST(HttpRequest.BodyPublishers.ofString(encoded))
                        .build();
        return send(request);
    }

    private JsonNode send(HttpRequest request) {
        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                throw new ProviderException(
                        providerId,
                        "mock bank returned HTTP "
                                + response.statusCode()
                                + " for "
                                + request.uri());
            }
            return mapper.readTree(response.body());
        } catch (IOException e) {
            throw new ProviderException(providerId, "I/O error calling mock bank", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ProviderException(providerId, "interrupted calling mock bank", e);
        }
    }
}
