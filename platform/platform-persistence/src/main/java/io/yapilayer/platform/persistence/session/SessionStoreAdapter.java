package io.yapilayer.platform.persistence.session;

import io.yapilayer.platform.application.ais.SessionStorePort;
import io.yapilayer.platform.domain.consent.ConsentId;
import io.yapilayer.provider.sdk.ais.ProviderSession;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Implements the application-layer session port on Spring Data JPA. */
@Component
@Transactional
public class SessionStoreAdapter implements SessionStorePort {

    private final SessionJpaRepository repository;

    public SessionStoreAdapter(SessionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(ConsentId consentId, ProviderSession session) {
        repository.save(ProviderSessionEntity.of(consentId.value(), session));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProviderSession> byConsentId(ConsentId consentId) {
        return repository.findById(consentId.value()).map(ProviderSessionEntity::toSession);
    }
}
