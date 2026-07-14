package io.yapilayer.platform.api.v1;

import io.yapilayer.platform.application.ais.AisService;
import io.yapilayer.platform.domain.account.Account;
import io.yapilayer.platform.domain.account.AccountId;
import io.yapilayer.platform.domain.account.AccountIdentifier;
import io.yapilayer.platform.domain.account.Balance;
import io.yapilayer.platform.domain.account.Transaction;
import io.yapilayer.platform.domain.consent.ConsentId;
import io.yapilayer.provider.sdk.ais.TransactionPage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Account data endpoints, scoped under an authorised consent. */
@RestController
@RequestMapping("/api/v1/consents/{consentId}/accounts")
public class AccountsController {

    public record AccountDto(String accountId, String nickname, String currency,
                             String type, List<IdentifierDto> identifiers) {}

    public record IdentifierDto(String scheme, String identification) {}

    public record BalanceDto(String accountId, String type, String amount,
                             String currency, String timestamp) {}

    public record TransactionDto(String transactionId, String amount, String currency,
                                 String status, String bookingDate, String reference,
                                 String merchantName) {}

    public record TransactionPageDto(List<TransactionDto> transactions, String nextPageKey) {}

    private final AisService ais;

    public AccountsController(AisService ais) {
        this.ais = ais;
    }

    @GetMapping
    public List<AccountDto> accounts(@PathVariable UUID consentId) {
        return ais.listAccounts(new ConsentId(consentId)).stream()
                .map(AccountsController::toDto)
                .toList();
    }

    @GetMapping("/{accountId}/balances")
    public List<BalanceDto> balances(@PathVariable UUID consentId,
                                     @PathVariable String accountId) {
        return ais.getBalances(new ConsentId(consentId), new AccountId(accountId)).stream()
                .map(b -> new BalanceDto(
                        b.accountId().value(),
                        b.type().name(),
                        b.amount().amount().toPlainString(),
                        b.amount().currency().getCurrencyCode(),
                        b.timestamp().toString()))
                .toList();
    }

    @GetMapping("/{accountId}/transactions")
    public TransactionPageDto transactions(
            @PathVariable UUID consentId,
            @PathVariable String accountId,
            @RequestParam(value = "pageKey", required = false) String pageKey,
            @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        TransactionPage page = ais.getTransactions(new ConsentId(consentId),
                new AccountId(accountId), Optional.ofNullable(pageKey), pageSize);
        return new TransactionPageDto(
                page.transactions().stream().map(AccountsController::toDto).toList(),
                page.nextPageKey().orElse(null));
    }

    private static AccountDto toDto(Account account) {
        return new AccountDto(
                account.id().value(),
                account.nickname(),
                account.currency().getCurrencyCode(),
                account.type().name(),
                account.identifiers().stream()
                        .map(i -> new IdentifierDto(i.scheme(), i.identification()))
                        .toList());
    }

    private static TransactionDto toDto(Transaction txn) {
        return new TransactionDto(
                txn.id(),
                txn.amount().amount().toPlainString(),
                txn.amount().currency().getCurrencyCode(),
                txn.status().name(),
                txn.bookingDate().toString(),
                txn.reference().orElse(null),
                txn.merchantName().orElse(null));
    }
}
