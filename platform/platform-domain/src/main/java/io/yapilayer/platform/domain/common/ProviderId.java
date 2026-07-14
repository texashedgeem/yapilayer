package io.yapilayer.platform.domain.common;

/**
 * Identifies a bank provider connector (e.g. {@code mock-bank}).
 *
 * <p>Lower-case kebab-case by convention; matches the connector's registered id.
 */
public record ProviderId(String value) {

    public ProviderId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("provider id must not be blank");
        }
        if (!value.matches("[a-z0-9][a-z0-9-]*")) {
            throw new IllegalArgumentException(
                    "provider id must be lower-case kebab-case: " + value);
        }
    }
}
