package io.yapilayer.platform.application.ais;

import io.yapilayer.platform.domain.consent.ConsentId;
import io.yapilayer.provider.sdk.ais.ProviderSession;
import java.util.Optional;

/**
 * Storage port for provider sessions (access tokens), implemented by platform-persistence.
 *
 * <p>Phase 1 stores tokens unencrypted in the database — an explicit limitation recorded in
 * SECURITY.md; encryption-at-rest lands with the platform-security hardening work.
 */
public interface SessionStorePort {

    void save(ConsentId consentId, ProviderSession session);

    Optional<ProviderSession> byConsentId(ConsentId consentId);
}
