package io.yapilayer.platform.persistence.session;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionJpaRepository extends JpaRepository<ProviderSessionEntity, UUID> {}
