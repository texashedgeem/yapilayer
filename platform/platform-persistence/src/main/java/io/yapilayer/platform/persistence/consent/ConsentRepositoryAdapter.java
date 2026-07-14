package io.yapilayer.platform.persistence.consent;

import io.yapilayer.platform.application.ais.ConsentRepositoryPort;
import io.yapilayer.platform.domain.consent.Consent;
import io.yapilayer.platform.domain.consent.ConsentId;
import java.net.URI;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Implements the application-layer consent port on Spring Data JPA. */
@Component
@Transactional
public class ConsentRepositoryAdapter implements ConsentRepositoryPort {

    private final ConsentJpaRepository repository;

    public ConsentRepositoryAdapter(ConsentJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Consent consent, String oauthState, URI clientRedirectUri) {
        repository.save(ConsentEntity.fromDomain(consent, oauthState, clientRedirectUri));
    }

    @Override
    public void update(Consent consent) {
        ConsentEntity entity =
                repository
                        .findById(consent.id().value())
                        .orElseThrow(
                                () ->
                                        new IllegalStateException(
                                                "consent to update not found: "
                                                        + consent.id().value()));
        entity.applyDomain(consent);
        repository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Consent> byId(ConsentId id) {
        return repository.findById(id.value()).map(ConsentEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConsentWithJourney> byOauthState(String oauthState) {
        return repository
                .findByOauthState(oauthState)
                .map(
                        e ->
                                new ConsentWithJourney(
                                        e.toDomain(), e.getOauthState(), e.getClientRedirectUri()));
    }
}
