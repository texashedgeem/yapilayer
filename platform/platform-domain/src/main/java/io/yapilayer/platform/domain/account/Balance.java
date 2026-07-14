package io.yapilayer.platform.domain.account;

import io.yapilayer.platform.domain.common.Money;
import java.time.Instant;
import java.util.Objects;

/** A point-in-time balance for an account. */
public record Balance(AccountId accountId, BalanceType type, Money amount, Instant timestamp) {

    public Balance {
        Objects.requireNonNull(accountId, "accountId");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(timestamp, "timestamp");
    }

    public enum BalanceType {
        /** Settled balance. */
        CURRENT,
        /** Funds available to the customer including any credit facility. */
        AVAILABLE
    }
}
