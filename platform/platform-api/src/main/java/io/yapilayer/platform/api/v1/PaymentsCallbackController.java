package io.yapilayer.platform.api.v1;

import io.yapilayer.platform.application.pis.PisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * OAuth callback for payment authorisation journeys — separate from the AIS
 * callback so consent and payment state never share a lookup path.
 */
@RestController
@RequestMapping("/api/v1/payments/callback")
public class PaymentsCallbackController {

    private final PisService pis;

    public PaymentsCallbackController(PisService pis) {
        this.pis = pis;
    }

    @GetMapping
    public ResponseEntity<Void> callback(
            @RequestParam("state") String state,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "error", required = false) String error) {

        PisService.CallbackResult result = (code != null && error == null)
                ? pis.handleCallback(state, code)
                : pis.handleDenied(state);

        URI target = URI.create(result.clientRedirectUri().toString()
                + "?paymentId=" + result.payment().id().value()
                + "&status=" + result.payment().status().name());
        return ResponseEntity.status(HttpStatus.FOUND).location(target).build();
    }
}
