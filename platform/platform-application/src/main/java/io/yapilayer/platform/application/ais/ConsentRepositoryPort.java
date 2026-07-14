package io.yapilayer.platform.application.ais;

import io.yapilayer.platform.domain.consent.Consent;
import io.yapilayer.platform.domain.consent.ConsentId;

import java.net.URI;
import java.util.Optional;

/**
 * Persistence port for consents, implemented by platform-persistence.
 *
 * <p>The OAuth {@code state} value and the client's redirect URI are transport
 * artifacts of the authorisation journey, stored alongside the consent but kept
 * out of the domain aggregate.
 */
public interface ConsentRepositoryPort {

    void save(Consent consent, String oauthState, URI clientRedirectUri);

    void update(Consent consent);

    Optional<Consent> byId(ConsentId id);

    /** Resolves the callback: the consent whose journey used this state value. */
    Optional<ConsentWithJourney> byOauthState(String oauthState);

    record ConsentWithJourney(Consent consent, String oauthState, URI clientRedirectUri) {}
}
