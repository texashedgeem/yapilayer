package io.yapilayer.platform.domain.payment;

import io.yapilayer.platform.domain.account.AccountIdentifier;
import java.util.Objects;

/** The payee of a payment. */
public record Creditor(String name, AccountIdentifier account) {

    public Creditor {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("creditor name must not be blank");
        }
        Objects.requireNonNull(account, "account");
    }
}
