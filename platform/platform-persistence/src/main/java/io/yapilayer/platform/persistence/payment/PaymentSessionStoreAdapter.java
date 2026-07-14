package io.yapilayer.platform.persistence.payment;

import io.yapilayer.platform.application.pis.PaymentSessionStorePort;
import io.yapilayer.platform.domain.payment.PaymentId;
import io.yapilayer.provider.sdk.ais.ProviderSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/** Implements the application-layer payment-session port on Spring Data JPA. */
@Component
@Transactional
public class PaymentSessionStoreAdapter implements PaymentSessionStorePort {

    private final PaymentSessionJpaRepository repository;

    public PaymentSessionStoreAdapter(PaymentSessionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(PaymentId paymentId, ProviderSession session) {
        repository.save(PaymentSessionEntity.of(paymentId.value(), session));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProviderSession> byPaymentId(PaymentId paymentId) {
        return repository.findById(paymentId.value())
                .map(PaymentSessionEntity::toSession);
    }
}
