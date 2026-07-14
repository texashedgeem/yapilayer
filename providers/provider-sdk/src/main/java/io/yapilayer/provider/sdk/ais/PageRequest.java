package io.yapilayer.provider.sdk.ais;

import java.util.Optional;

/**
 * Pagination request for transaction history.
 *
 * @param pageKey opaque provider-issued continuation key; empty for the first page
 */
public record PageRequest(Optional<String> pageKey, int pageSize) {

    public PageRequest {
        if (pageSize < 1 || pageSize > 500) {
            throw new IllegalArgumentException("pageSize must be between 1 and 500");
        }
    }

    public static PageRequest firstPage(int pageSize) {
        return new PageRequest(Optional.empty(), pageSize);
    }
}
