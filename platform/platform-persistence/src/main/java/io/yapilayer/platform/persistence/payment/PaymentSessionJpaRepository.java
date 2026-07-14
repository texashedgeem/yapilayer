package io.yapilayer.platform.persistence.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentSessionJpaRepository extends JpaRepository<PaymentSessionEntity, UUID> {
}
