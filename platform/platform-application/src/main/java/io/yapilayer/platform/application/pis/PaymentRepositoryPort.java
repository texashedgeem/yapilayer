package io.yapilayer.platform.application.pis;

import io.yapilayer.platform.domain.payment.Payment;
import io.yapilayer.platform.domain.payment.PaymentId;
import java.net.URI;
import java.util.Optional;

/**
 * Persistence port for payments, implemented by platform-persistence.
 *
 * <p>As with consents, the OAuth {@code state} and client redirect URI are journey artifacts stored
 * alongside the payment, outside the domain aggregate. The provider payment-consent reference is
 * likewise journey state (the domain aggregate tracks the provider <em>payment</em> id, which
 * exists only after submission).
 */
public interface PaymentRepositoryPort {

    void save(
            Payment payment,
            String oauthState,
            URI clientRedirectUri,
            String providerPaymentConsentId);

    void update(Payment payment);

    Optional<Payment> byId(PaymentId id);

    Optional<PaymentWithJourney> byOauthState(String oauthState);

    record PaymentWithJourney(
            Payment payment,
            String oauthState,
            URI clientRedirectUri,
            String providerPaymentConsentId) {}
}
