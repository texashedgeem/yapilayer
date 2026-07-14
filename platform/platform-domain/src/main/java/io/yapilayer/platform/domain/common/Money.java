package io.yapilayer.platform.domain.common;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

/**
 * A monetary amount in a specific currency.
 *
 * <p>Amounts may be negative (debit transactions). Scale is preserved as supplied by the provider.
 */
public record Money(BigDecimal amount, Currency currency) {

    public Money {
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(currency, "currency");
    }

    public static Money of(String amount, String currencyCode) {
        return new Money(new BigDecimal(amount), Currency.getInstance(currencyCode));
    }

    public boolean isPositive() {
        return amount.signum() > 0;
    }
}
