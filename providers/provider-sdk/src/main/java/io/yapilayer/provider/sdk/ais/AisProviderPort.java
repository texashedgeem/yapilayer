package io.yapilayer.provider.sdk.ais;

import io.yapilayer.platform.domain.account.Account;
import io.yapilayer.platform.domain.account.AccountId;
import io.yapilayer.platform.domain.account.Balance;
import io.yapilayer.platform.domain.consent.ConsentStatus;

import java.util.List;

/**
 * Account Information Services port (ADR 0004). Implemented by connectors that
 * declare the {@code AIS} capability.
 *
 * <p>Flow: {@link #createConsent} → customer authorises at the bank via the
 * returned URL → platform exchanges the callback code with
 * {@link #exchangeAuthorisationCode} → data access with the resulting
 * {@link ProviderSession}.
 */
public interface AisProviderPort {

    /** Creates the provider-side consent resource and the customer authorisation URL. */
    ConsentAuthorisation createConsent(AisConsentRequest request);

    /** Provider-side view of consent state (source of truth for expiry/revocation at the bank). */
    ConsentStatus getConsentStatus(String providerConsentId);

    /** Exchanges the OAuth authorisation code from the customer redirect for a session. */
    ProviderSession exchangeAuthorisationCode(String providerConsentId, String authorisationCode);

    List<Account> listAccounts(ProviderSession session);

    List<Balance> getBalances(ProviderSession session, AccountId accountId);

    TransactionPage getTransactions(ProviderSession session, AccountId accountId, PageRequest page);
}
