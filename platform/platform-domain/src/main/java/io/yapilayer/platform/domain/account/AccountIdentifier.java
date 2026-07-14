package io.yapilayer.platform.domain.account;

import java.util.Objects;

/**
 * An external scheme identifier for an account, e.g. sort code + account number or IBAN.
 *
 * @param scheme identification scheme, e.g. {@code UK.OBIE.SortCodeAccountNumber} or {@code
 *     UK.OBIE.IBAN}
 * @param identification the identifier under that scheme
 */
public record AccountIdentifier(String scheme, String identification) {

    public AccountIdentifier {
        Objects.requireNonNull(scheme, "scheme");
        Objects.requireNonNull(identification, "identification");
    }
}
