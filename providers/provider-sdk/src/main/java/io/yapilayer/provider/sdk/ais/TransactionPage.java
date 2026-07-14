package io.yapilayer.provider.sdk.ais;

import io.yapilayer.platform.domain.account.Transaction;
import java.util.List;
import java.util.Optional;

/**
 * One page of transactions.
 *
 * @param nextPageKey continuation key for the following page; empty when this is the last
 */
public record TransactionPage(List<Transaction> transactions, Optional<String> nextPageKey) {

    public TransactionPage {
        transactions = transactions == null ? List.of() : List.copyOf(transactions);
        nextPageKey = nextPageKey == null ? Optional.empty() : nextPageKey;
    }
}
