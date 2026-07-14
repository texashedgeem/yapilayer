package io.yapilayer.provider.mockbank.simulator.account;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Seeded test persona: two accounts with balances and transaction history,
 * in the spirit of bank sandbox test data packs.
 */
@Component
public class TestData {

    public record SimAccount(String id, String nickname, String currency,
                             String subType, String scheme, String identification,
                             BigDecimal currentBalance, BigDecimal availableBalance) {}

    public record SimTransaction(String id, String accountId, BigDecimal amount,
                                 String currency, String status, LocalDate bookingDate,
                                 String information, String merchantName) {}

    private static final SimAccount CURRENT = new SimAccount(
            "acc-001", "Everyday Current", "GBP", "CurrentAccount",
            "UK.OBIE.SortCodeAccountNumber", "60161331926819",
            new BigDecimal("1274.56"), new BigDecimal("1174.56"));

    private static final SimAccount SAVINGS = new SimAccount(
            "acc-002", "Rainy Day Saver", "GBP", "Savings",
            "UK.OBIE.SortCodeAccountNumber", "60161331926820",
            new BigDecimal("15750.00"), new BigDecimal("15750.00"));

    private final Map<String, SimAccount> accounts = Map.of(
            CURRENT.id(), CURRENT,
            SAVINGS.id(), SAVINGS);

    private final Map<String, List<SimTransaction>> transactions = Map.of(
            CURRENT.id(), List.of(
                    txn("txn-101", CURRENT, "-32.40", "2026-07-12", "Card purchase", "Sainsbury's"),
                    txn("txn-102", CURRENT, "-9.99", "2026-07-11", "Subscription", "Spotify"),
                    txn("txn-103", CURRENT, "2200.00", "2026-07-10", "Salary BACS", null),
                    txn("txn-104", CURRENT, "-64.20", "2026-07-08", "Card purchase", "Shell"),
                    txn("txn-105", CURRENT, "-850.00", "2026-07-01", "Standing order - rent", null),
                    txn("txn-106", CURRENT, "-15.50", "2026-06-28", "Card purchase", "Pret A Manger"),
                    txn("txn-107", CURRENT, "-120.00", "2026-06-25", "Transfer to savings", null)),
            SAVINGS.id(), List.of(
                    txn("txn-201", SAVINGS, "120.00", "2026-06-25", "Transfer from current", null),
                    txn("txn-202", SAVINGS, "31.90", "2026-06-30", "Interest", null)));

    private static SimTransaction txn(String id, SimAccount account, String amount,
                                      String date, String info, String merchant) {
        return new SimTransaction(id, account.id(), new BigDecimal(amount),
                account.currency(), "Booked", LocalDate.parse(date), info, merchant);
    }

    public List<SimAccount> accounts() {
        return List.copyOf(accounts.values());
    }

    public Optional<SimAccount> account(String id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public List<SimTransaction> transactionsFor(String accountId) {
        return transactions.getOrDefault(accountId, List.of());
    }
}
