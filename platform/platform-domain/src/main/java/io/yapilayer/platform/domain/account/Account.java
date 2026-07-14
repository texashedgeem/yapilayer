package io.yapilayer.platform.domain.account;

import io.yapilayer.platform.domain.common.ProviderId;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

/**
 * A customer account discovered through an authorised AIS consent.
 *
 * @param nickname customer-facing account name as reported by the provider
 */
public record Account(
        AccountId id,
        ProviderId providerId,
        String nickname,
        Currency currency,
        AccountType type,
        List<AccountIdentifier> identifiers) {

    public Account {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(providerId, "providerId");
        Objects.requireNonNull(nickname, "nickname");
        Objects.requireNonNull(currency, "currency");
        Objects.requireNonNull(type, "type");
        identifiers = identifiers == null ? List.of() : List.copyOf(identifiers);
    }

    public enum AccountType {
        CURRENT,
        SAVINGS,
        CREDIT_CARD,
        OTHER
    }
}
