package io.yapilayer.provider.mockbank.simulator.account;

import io.yapilayer.provider.mockbank.simulator.security.BearerAuth;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OB Read/Write-style AIS data endpoints
 * ({@code /open-banking/v3.1/aisp/accounts...}), bearer-token protected.
 */
@RestController
@RequestMapping("/open-banking/v3.1/aisp")
public class AccountController {

    private static final int PAGE_SIZE = 5;

    private final TestData data;
    private final BearerAuth auth;

    public AccountController(TestData data, BearerAuth auth) {
        this.data = data;
        this.auth = auth;
    }

    @GetMapping("/accounts")
    public Map<String, Object> accounts(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        auth.requireAuthorisedConsent(authorization);
        List<Map<String, Object>> accounts = data.accounts().stream()
                .map(a -> Map.<String, Object>of(
                        "AccountId", a.id(),
                        "Nickname", a.nickname(),
                        "Currency", a.currency(),
                        "AccountSubType", a.subType(),
                        "Account", List.of(Map.of(
                                "SchemeName", a.scheme(),
                                "Identification", a.identification()))))
                .toList();
        return Map.of("Data", Map.of("Account", accounts));
    }

    @GetMapping("/accounts/{accountId}/balances")
    public ResponseEntity<Map<String, Object>> balances(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable String accountId) {
        auth.requireAuthorisedConsent(authorization);
        return data.account(accountId)
                .map(a -> ResponseEntity.ok(Map.<String, Object>of("Data", Map.of("Balance", List.of(
                        balance(a.id(), "InterimBooked", a.currentBalance().toPlainString(), a.currency()),
                        balance(a.id(), "InterimAvailable", a.availableBalance().toPlainString(), a.currency()))))))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<Map<String, Object>> transactions(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable String accountId,
            @RequestParam(name = "page", defaultValue = "0") int page) {
        auth.requireAuthorisedConsent(authorization);
        if (data.account(accountId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<TestData.SimTransaction> all = data.transactionsFor(accountId);
        int from = Math.min(page * PAGE_SIZE, all.size());
        int to = Math.min(from + PAGE_SIZE, all.size());

        List<Map<String, Object>> items = all.subList(from, to).stream()
                .map(t -> {
                    Map<String, Object> txn = new HashMap<>();
                    txn.put("TransactionId", t.id());
                    txn.put("AccountId", t.accountId());
                    txn.put("Status", t.status());
                    txn.put("BookingDateTime", t.bookingDate().atStartOfDay().toInstant(java.time.ZoneOffset.UTC).toString());
                    txn.put("Amount", Map.of("Amount", t.amount().toPlainString(), "Currency", t.currency()));
                    txn.put("TransactionInformation", t.information());
                    if (t.merchantName() != null) {
                        txn.put("MerchantDetails", Map.of("MerchantName", t.merchantName()));
                    }
                    return txn;
                })
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("Data", Map.of("Transaction", items));
        if (to < all.size()) {
            response.put("Links", Map.of("Next", String.valueOf(page + 1)));
        }
        response.put("Meta", Map.of("TotalPages", (all.size() + PAGE_SIZE - 1) / PAGE_SIZE,
                "GeneratedAt", Instant.now().toString()));
        return ResponseEntity.ok(response);
    }

    private static Map<String, Object> balance(String accountId, String type,
                                               String amount, String currency) {
        return Map.of(
                "AccountId", accountId,
                "Type", type,
                "Amount", Map.of("Amount", amount, "Currency", currency),
                "DateTime", Instant.now().toString());
    }
}
