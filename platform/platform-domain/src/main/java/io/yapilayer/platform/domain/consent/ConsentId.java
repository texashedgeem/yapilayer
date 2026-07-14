package io.yapilayer.platform.domain.consent;

import java.util.Objects;
import java.util.UUID;

/** Platform-assigned consent identifier (distinct from any provider-side reference). */
public record ConsentId(UUID value) {

    public ConsentId {
        Objects.requireNonNull(value, "value");
    }

    public static ConsentId random() {
        return new ConsentId(UUID.randomUUID());
    }
}
