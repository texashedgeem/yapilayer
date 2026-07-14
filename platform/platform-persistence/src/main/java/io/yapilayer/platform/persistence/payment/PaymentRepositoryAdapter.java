package io.yapilayer.platform.persistence.payment;

import io.yapilayer.platform.application.pis.PaymentRepositoryPort;
import io.yapilayer.platform.domain.payment.Payment;
import io.yapilayer.platform.domain.payment.PaymentId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.Optional;

/** Implements the application-layer payment port on Spring Data JPA. */
@Component
@Transactional
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final PaymentJpaRepository repository;

    public PaymentRepositoryAdapter(PaymentJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Payment payment, String oauthState, URI clientRedirectUri,
                     String providerPaymentConsentId) {
        repository.save(PaymentEntity.fromDomain(payment, oauthState,
                clientRedirectUri, providerPaymentConsentId));
    }

    @Override
    public void update(Payment payment) {
        PaymentEntity entity = repository.findById(payment.id().value())
                .orElseThrow(() -> new IllegalStateException(
                        "payment to update not found: " + payment.id().value()));
        entity.applyDomain(payment);
        repository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> byId(PaymentId id) {
        return repository.findById(id.value()).map(PaymentEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentWithJourney> byOauthState(String oauthState) {
        return repository.findByOauthState(oauthState)
                .map(e -> new PaymentWithJourney(e.toDomain(), e.getOauthState(),
                        e.getClientRedirectUri(), e.getProviderPaymentConsentId()));
    }
}
