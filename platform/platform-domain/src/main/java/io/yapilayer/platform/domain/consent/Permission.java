package io.yapilayer.platform.domain.consent;

/** Data clusters an AIS consent may grant access to (UK Open Banking style). */
public enum Permission {
    READ_ACCOUNTS,
    READ_BALANCES,
    READ_TRANSACTIONS,
    READ_IDENTITY
}
