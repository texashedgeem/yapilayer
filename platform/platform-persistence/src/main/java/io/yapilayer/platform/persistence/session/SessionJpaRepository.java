package io.yapilayer.platform.persistence.session;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionJpaRepository extends JpaRepository<ProviderSessionEntity, UUID> {
}
