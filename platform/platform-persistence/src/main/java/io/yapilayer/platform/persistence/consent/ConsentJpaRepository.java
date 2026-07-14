package io.yapilayer.platform.persistence.consent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConsentJpaRepository extends JpaRepository<ConsentEntity, UUID> {

    Optional<ConsentEntity> findByOauthState(String oauthState);
}
