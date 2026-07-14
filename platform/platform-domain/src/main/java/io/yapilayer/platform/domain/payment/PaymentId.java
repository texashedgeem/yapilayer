package io.yapilayer.platform.domain.payment;

import java.util.Objects;
import java.util.UUID;

/** Platform-assigned payment identifier. */
public record PaymentId(UUID value) {

    public PaymentId {
        Objects.requireNonNull(value, "value");
    }

    public static PaymentId random() {
        return new PaymentId(UUID.randomUUID());
    }
}
