package io.yapilayer.platform.api.v1;

import io.yapilayer.platform.application.ais.AisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * OAuth callback endpoint the bank redirects the customer to after the
 * authorisation journey. Completes the exchange, then sends the customer's
 * browser on to the client application's redirect URI with the outcome.
 */
@RestController
@RequestMapping("/api/v1/callback")
public class CallbackController {

    private final AisService ais;

    public CallbackController(AisService ais) {
        this.ais = ais;
    }

    @GetMapping
    public ResponseEntity<Void> callback(
            @RequestParam("state") String state,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "error", required = false) String error) {

        AisService.CallbackResult result = (code != null && error == null)
                ? ais.handleCallback(state, code)
                : ais.handleDenied(state);

        URI target = URI.create(result.clientRedirectUri().toString()
                + "?consentId=" + result.consent().id().value()
                + "&status=" + result.consent().status().name());
        return ResponseEntity.status(HttpStatus.FOUND).location(target).build();
    }
}
