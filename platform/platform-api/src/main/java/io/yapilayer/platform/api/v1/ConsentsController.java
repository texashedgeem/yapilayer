package io.yapilayer.platform.api.v1;

import io.yapilayer.platform.application.ais.AisService;
import io.yapilayer.platform.domain.common.ProviderId;
import io.yapilayer.platform.domain.consent.Consent;
import io.yapilayer.platform.domain.consent.ConsentId;
import io.yapilayer.platform.domain.consent.Permission;
import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** AIS consent lifecycle endpoints. */
@RestController
@RequestMapping("/api/v1/consents")
public class ConsentsController {

    public record CreateConsentRequest(
            String providerId, List<String> permissions, String redirectUri) {}

    public record ConsentDto(
            UUID consentId,
            String providerId,
            String status,
            List<String> permissions,
            String expiresAt,
            String authorisationUrl) {}

    private final AisService ais;

    public ConsentsController(AisService ais) {
        this.ais = ais;
    }

    @PostMapping
    public ResponseEntity<ConsentDto> create(@RequestBody CreateConsentRequest request) {
        Set<Permission> permissions =
                request.permissions().stream().map(Permission::valueOf).collect(Collectors.toSet());

        AisService.CreatedConsent created =
                ais.createConsent(
                        new ProviderId(request.providerId()),
                        permissions,
                        URI.create(request.redirectUri()));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toDto(created.consent(), created.authorisationUrl()));
    }

    @GetMapping("/{consentId}")
    public ConsentDto get(@PathVariable UUID consentId) {
        return toDto(ais.getConsent(new ConsentId(consentId)), null);
    }

    private static ConsentDto toDto(Consent consent, URI authorisationUrl) {
        return new ConsentDto(
                consent.id().value(),
                consent.providerId().value(),
                consent.status().name(),
                consent.permissions().stream().map(Enum::name).sorted().toList(),
                consent.expiresAt().toString(),
                authorisationUrl != null ? authorisationUrl.toString() : null);
    }
}
