package io.yapilayer.provider.mockbank.simulator.security;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import io.yapilayer.provider.mockbank.simulator.consent.ConsentStore;
import io.yapilayer.provider.mockbank.simulator.oauth.TokenStore;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * Resolves the {@code Authorization: Bearer} header to an authorised consent, rejecting
 * missing/invalid tokens (401) and unauthorised consents (403).
 */
@Component
public class BearerAuth {

    private final TokenStore tokens;
    private final ConsentStore consents;

    public BearerAuth(TokenStore tokens, ConsentStore consents) {
        this.tokens = tokens;
        this.consents = consents;
    }

    public ConsentStore.SimConsent requireAuthorisedConsent(String authorizationHeader) {
        String token =
                Optional.ofNullable(authorizationHeader)
                        .filter(h -> h.startsWith("Bearer "))
                        .map(h -> h.substring("Bearer ".length()))
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                UNAUTHORIZED, "missing bearer token"));

        String consentId =
                tokens.consentForToken(token)
                        .orElseThrow(
                                () -> new ResponseStatusException(UNAUTHORIZED, "invalid token"));

        ConsentStore.SimConsent consent =
                consents.byId(consentId)
                        .orElseThrow(
                                () -> new ResponseStatusException(UNAUTHORIZED, "unknown consent"));

        if (consent.status() != ConsentStore.Status.AUTHORISED) {
            throw new ResponseStatusException(FORBIDDEN, "consent not authorised");
        }
        return consent;
    }
}
