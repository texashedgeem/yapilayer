package io.yapilayer.platform.application.ais;

import io.yapilayer.platform.domain.account.Account;
import io.yapilayer.platform.domain.account.AccountId;
import io.yapilayer.platform.domain.account.Balance;
import io.yapilayer.platform.domain.common.ProviderId;
import io.yapilayer.platform.domain.common.TenantId;
import io.yapilayer.platform.domain.consent.Consent;
import io.yapilayer.platform.domain.consent.ConsentId;
import io.yapilayer.platform.domain.consent.ConsentStatus;
import io.yapilayer.platform.domain.consent.Permission;
import io.yapilayer.provider.sdk.BankConnector;
import io.yapilayer.provider.sdk.ProviderRegistry;
import io.yapilayer.provider.sdk.ais.AisConsentRequest;
import io.yapilayer.provider.sdk.ais.AisProviderPort;
import io.yapilayer.provider.sdk.ais.ConsentAuthorisation;
import io.yapilayer.provider.sdk.ais.PageRequest;
import io.yapilayer.provider.sdk.ais.ProviderSession;
import io.yapilayer.provider.sdk.ais.TransactionPage;

import java.net.URI;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * AIS use cases: consent creation, authorisation callback, account data access.
 *
 * <p>Plain Java — wired as a bean by platform-bootstrap, no framework
 * dependencies here (ENGINEERING_STANDARDS §3.2).
 */
public class AisService {

    /** Default consent validity when the caller does not specify one. */
    private static final Duration DEFAULT_CONSENT_VALIDITY = Duration.ofDays(90);

    private final ProviderRegistry providers;
    private final ConsentRepositoryPort consents;
    private final SessionStorePort sessions;
    private final URI platformCallbackUri;
    private final Clock clock;
    private final SecureRandom random = new SecureRandom();

    public AisService(ProviderRegistry providers,
                      ConsentRepositoryPort consents,
                      SessionStorePort sessions,
                      URI platformCallbackUri,
                      Clock clock) {
        this.providers = providers;
        this.consents = consents;
        this.sessions = sessions;
        this.platformCallbackUri = platformCallbackUri;
        this.clock = clock;
    }

    public record CreatedConsent(Consent consent, URI authorisationUrl) {}

    /** Result of the customer's authorisation callback. */
    public record CallbackResult(Consent consent, URI clientRedirectUri) {}

    /**
     * Creates a consent at the provider and returns the bank authorisation URL.
     *
     * @param clientRedirectUri where the customer is sent after the platform
     *                          completes the callback exchange
     */
    public CreatedConsent createConsent(ProviderId providerId,
                                        Set<Permission> permissions,
                                        URI clientRedirectUri) {
        AisProviderPort port = aisPort(providerId);
        Instant now = clock.instant();
        Consent consent = Consent.create(TenantId.DEFAULT, providerId, permissions,
                now, now.plus(DEFAULT_CONSENT_VALIDITY));

        String state = newState();
        ConsentAuthorisation authorisation = port.createConsent(new AisConsentRequest(
                permissions, consent.expiresAt(), platformCallbackUri, state));

        consent = consent.withProviderConsentId(authorisation.providerConsentId());
        consents.save(consent, state, clientRedirectUri);
        return new CreatedConsent(consent, authorisation.authorisationUrl());
    }

    /**
     * Completes the authorisation journey: validates state, exchanges the code,
     * stores the session, marks the consent authorised.
     */
    public CallbackResult handleCallback(String state, String authorisationCode) {
        ConsentRepositoryPort.ConsentWithJourney journey = consents.byOauthState(state)
                .orElseThrow(() -> new NotFoundException("no consent for callback state"));
        Consent consent = journey.consent();

        AisProviderPort port = aisPort(consent.providerId());
        ProviderSession session = port.exchangeAuthorisationCode(
                consent.providerConsentId().orElseThrow(), authorisationCode);
        sessions.save(consent.id(), session);

        Consent authorised = consent.authorise(clock.instant());
        consents.update(authorised);
        return new CallbackResult(authorised, journey.clientRedirectUri());
    }

    /** Records a denied authorisation. */
    public CallbackResult handleDenied(String state) {
        ConsentRepositoryPort.ConsentWithJourney journey = consents.byOauthState(state)
                .orElseThrow(() -> new NotFoundException("no consent for callback state"));
        Consent rejected = journey.consent().reject();
        consents.update(rejected);
        return new CallbackResult(rejected, journey.clientRedirectUri());
    }

    public Consent getConsent(ConsentId id) {
        return consents.byId(id)
                .orElseThrow(() -> new NotFoundException("consent not found"));
    }

    public List<Account> listAccounts(ConsentId consentId) {
        AuthorisedAccess access = requireAuthorised(consentId);
        return access.port().listAccounts(access.session());
    }

    public List<Balance> getBalances(ConsentId consentId, AccountId accountId) {
        AuthorisedAccess access = requireAuthorised(consentId);
        return access.port().getBalances(access.session(), accountId);
    }

    public TransactionPage getTransactions(ConsentId consentId, AccountId accountId,
                                           Optional<String> pageKey, int pageSize) {
        AuthorisedAccess access = requireAuthorised(consentId);
        return access.port().getTransactions(access.session(), accountId,
                new PageRequest(pageKey, pageSize));
    }

    private record AuthorisedAccess(AisProviderPort port, ProviderSession session) {}

    private AuthorisedAccess requireAuthorised(ConsentId consentId) {
        Consent consent = getConsent(consentId);
        if (consent.status() != ConsentStatus.AUTHORISED) {
            throw new ConsentNotAuthorisedException(
                    "consent is " + consent.status() + ", not AUTHORISED");
        }
        if (consent.isExpired(clock.instant())) {
            consents.update(consent.expire());
            throw new ConsentNotAuthorisedException("consent has expired");
        }
        ProviderSession session = sessions.byConsentId(consentId)
                .orElseThrow(() -> new NotFoundException("no session for consent"));
        return new AuthorisedAccess(aisPort(consent.providerId()), session);
    }

    private AisProviderPort aisPort(ProviderId providerId) {
        BankConnector connector = providers.byId(providerId)
                .orElseThrow(() -> new NotFoundException("unknown provider: " + providerId.value()));
        return connector.aisPort()
                .orElseThrow(() -> new NotFoundException(
                        "provider does not support AIS: " + providerId.value()));
    }

    private String newState() {
        byte[] bytes = new byte[24];
        random.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    /** Requested resource does not exist. */
    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    /** Consent exists but is not in a state permitting data access. */
    public static class ConsentNotAuthorisedException extends RuntimeException {
        public ConsentNotAuthorisedException(String message) {
            super(message);
        }
    }
}
