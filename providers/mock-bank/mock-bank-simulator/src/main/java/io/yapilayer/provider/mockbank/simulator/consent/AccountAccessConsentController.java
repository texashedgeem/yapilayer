package io.yapilayer.provider.mockbank.simulator.consent;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OB Read/Write-style account-access-consent endpoints ({@code
 * /open-banking/v3.1/aisp/account-access-consents}).
 */
@RestController
@RequestMapping("/open-banking/v3.1/aisp/account-access-consents")
public class AccountAccessConsentController {

    private final ConsentStore consents;

    public AccountAccessConsentController(ConsentStore consents) {
        this.consents = consents;
    }

    @SuppressWarnings("unchecked")
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> body) {
        Map<String, Object> data = (Map<String, Object>) body.getOrDefault("Data", Map.of());
        List<String> permissions = (List<String>) data.getOrDefault("Permissions", List.of());
        String expiration = (String) data.get("ExpirationDateTime");
        Instant expiresAt =
                expiration != null
                        ? Instant.parse(expiration)
                        : Instant.now().plusSeconds(90L * 24 * 3600);

        ConsentStore.SimConsent consent = consents.createAis(permissions, expiresAt);
        return ResponseEntity.status(HttpStatus.CREATED).body(render(consent));
    }

    @GetMapping("/{consentId}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable String consentId) {
        return consents.byId(consentId)
                .map(c -> ResponseEntity.ok(render(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    static Map<String, Object> render(ConsentStore.SimConsent consent) {
        return Map.of(
                "Data",
                Map.of(
                        "ConsentId", consent.id(),
                        "Status", obStatus(consent.status()),
                        "Permissions", consent.permissions(),
                        "ExpirationDateTime", consent.expiresAt().toString()));
    }

    public static String obStatus(ConsentStore.Status status) {
        return switch (status) {
            case AWAITING_AUTHORISATION -> "AwaitingAuthorisation";
            case AUTHORISED -> "Authorised";
            case REJECTED -> "Rejected";
        };
    }
}
