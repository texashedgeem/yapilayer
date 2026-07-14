package io.yapilayer.platform.api.v1;

import io.yapilayer.platform.application.ais.AisService;
import io.yapilayer.provider.sdk.ProviderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/** Maps application and provider exceptions to consistent API error responses. */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(AisService.NotFoundException.class)
    public ResponseEntity<Map<String, String>> notFound(AisService.NotFoundException e) {
        return error(HttpStatus.NOT_FOUND, "not_found", e.getMessage());
    }

    @ExceptionHandler(AisService.ConsentNotAuthorisedException.class)
    public ResponseEntity<Map<String, String>> consentState(
            AisService.ConsentNotAuthorisedException e) {
        return error(HttpStatus.CONFLICT, "consent_not_authorised", e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badRequest(IllegalArgumentException e) {
        return error(HttpStatus.BAD_REQUEST, "invalid_request", e.getMessage());
    }

    @ExceptionHandler(ProviderException.class)
    public ResponseEntity<Map<String, String>> provider(ProviderException e) {
        return error(HttpStatus.BAD_GATEWAY, "provider_error",
                "provider " + e.providerId().value() + ": " + e.getMessage());
    }

    private static ResponseEntity<Map<String, String>> error(HttpStatus status,
                                                             String code, String message) {
        return ResponseEntity.status(status)
                .body(Map.of("error", code, "message", message == null ? "" : message));
    }
}
