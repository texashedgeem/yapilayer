package io.yapilayer.provider.mockbank.simulator.oauth;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory OAuth state: authorisation codes (single use) and access tokens,
 * each bound to the consent they authorise.
 */
@Component
public class TokenStore {

    private final Map<String, String> codeToConsent = new ConcurrentHashMap<>();
    private final Map<String, String> tokenToConsent = new ConcurrentHashMap<>();

    public String issueCode(String consentId) {
        String code = "code-" + UUID.randomUUID();
        codeToConsent.put(code, consentId);
        return code;
    }

    /** Redeems a code (single use) for the consent it was issued against. */
    public Optional<String> redeemCode(String code) {
        return Optional.ofNullable(codeToConsent.remove(code));
    }

    public String issueToken(String consentId) {
        String token = "token-" + UUID.randomUUID();
        tokenToConsent.put(token, consentId);
        return token;
    }

    /** Resolves a bearer token to its consent, if valid. */
    public Optional<String> consentForToken(String bearerToken) {
        return Optional.ofNullable(tokenToConsent.get(bearerToken));
    }
}
