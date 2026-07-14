package io.yapilayer.platform.domain.account;

import io.yapilayer.platform.domain.common.Money;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

/**
 * A single account transaction.
 *
 * @param id provider-side transaction reference
 * @param amount signed amount — negative for debits
 * @param reference statement reference / narrative, if the provider supplies one
 */
public record Transaction(
        String id,
        AccountId accountId,
        Money amount,
        Status status,
        LocalDate bookingDate,
        Optional<String> reference,
        Optional<String> merchantName) {

    public Transaction {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(accountId, "accountId");
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(status, "status");
        Objects.requireNonNull(bookingDate, "bookingDate");
        Objects.requireNonNull(reference, "reference");
        Objects.requireNonNull(merchantName, "merchantName");
    }

    public enum Status {
        BOOKED,
        PENDING
    }
}
