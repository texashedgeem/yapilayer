package io.yapilayer.provider.mockbank.simulator.oauth;

import io.yapilayer.provider.mockbank.simulator.consent.ConsentStore;
import java.net.URI;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simulated ASPSP OAuth 2.0 authorisation-code endpoints.
 *
 * <p>{@code GET /oauth/authorize} renders the customer consent page; {@code POST
 * /oauth/authorize/decision} records approval/denial and redirects back to the TPP with a code;
 * {@code POST /oauth/token} exchanges the code.
 */
@RestController
@RequestMapping("/oauth")
public class OAuthController {

    private final TokenStore tokens;
    private final ConsentStore consents;

    public OAuthController(TokenStore tokens, ConsentStore consents) {
        this.tokens = tokens;
        this.consents = consents;
    }

    @GetMapping(value = "/authorize", produces = MediaType.TEXT_HTML_VALUE)
    public String authorise(
            @RequestParam("consent_id") String consentId,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("state") String state) {
        if (consents.byId(consentId).isEmpty()) {
            return "<html><body><h1>Unknown consent</h1></body></html>";
        }
        // Deliberately simple consent page — enough for a human demo and for
        // tests to drive the decision endpoint directly.
        return """
                <html><body>
                <h1>Mock Bank</h1>
                <p>An application is requesting access (consent %s).</p>
                <form method="post" action="/oauth/authorize/decision">
                  <input type="hidden" name="consent_id" value="%s"/>
                  <input type="hidden" name="redirect_uri" value="%s"/>
                  <input type="hidden" name="state" value="%s"/>
                  <button name="decision" value="approve">Approve</button>
                  <button name="decision" value="deny">Deny</button>
                </form>
                </body></html>
                """
                .formatted(consentId, consentId, redirectUri, state);
    }

    @PostMapping("/authorize/decision")
    public ResponseEntity<Void> decision(
            @RequestParam("consent_id") String consentId,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("state") String state,
            @RequestParam("decision") String decision) {
        boolean approved = "approve".equals(decision);
        if (consents.decide(consentId, approved).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String location =
                approved
                        ? redirectUri + "?code=" + tokens.issueCode(consentId) + "&state=" + state
                        : redirectUri + "?error=access_denied&state=" + state;
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(location)).build();
    }

    @PostMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> token(
            @RequestParam("grant_type") String grantType, @RequestParam("code") String code) {
        if (!"authorization_code".equals(grantType)) {
            return ResponseEntity.badRequest().body(Map.of("error", "unsupported_grant_type"));
        }
        return tokens.redeemCode(code)
                .map(
                        consentId ->
                                ResponseEntity.ok(
                                        Map.<String, Object>of(
                                                "access_token",
                                                tokens.issueToken(consentId),
                                                "token_type",
                                                "Bearer",
                                                "expires_in",
                                                3600,
                                                "consent_id",
                                                consentId)))
                .orElse(ResponseEntity.badRequest().body(Map.of("error", "invalid_grant")));
    }
}
