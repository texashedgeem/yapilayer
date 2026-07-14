package io.yapilayer.platform.api.v1;

import io.yapilayer.provider.sdk.ProviderCapability;
import io.yapilayer.provider.sdk.ProviderRegistry;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Provider discovery: which bank connectors are registered and what they support. */
@RestController
@RequestMapping("/api/v1/providers")
public class ProvidersController {

    public record ProviderDto(
            String id, String name, String country, List<ProviderCapability> capabilities) {}

    private final ProviderRegistry registry;

    public ProvidersController(ProviderRegistry registry) {
        this.registry = registry;
    }

    @GetMapping
    public List<ProviderDto> list() {
        return registry.all().stream()
                .map(
                        connector -> {
                            var d = connector.descriptor();
                            return new ProviderDto(
                                    d.id().value(),
                                    d.displayName(),
                                    d.countryCode(),
                                    d.capabilities().stream().sorted().toList());
                        })
                .toList();
    }
}
