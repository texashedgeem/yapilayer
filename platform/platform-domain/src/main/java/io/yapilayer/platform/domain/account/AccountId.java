package io.yapilayer.platform.domain.account;

/**
 * Provider-scoped account identifier — the reference by which the provider knows this account (not
 * a platform-assigned id).
 */
public record AccountId(String value) {

    public AccountId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("account id must not be blank");
        }
    }
}
