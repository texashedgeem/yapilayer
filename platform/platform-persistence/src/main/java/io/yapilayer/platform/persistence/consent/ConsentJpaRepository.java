package io.yapilayer.platform.persistence.consent;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsentJpaRepository extends JpaRepository<ConsentEntity, UUID> {

    Optional<ConsentEntity> findByOauthState(String oauthState);
}
