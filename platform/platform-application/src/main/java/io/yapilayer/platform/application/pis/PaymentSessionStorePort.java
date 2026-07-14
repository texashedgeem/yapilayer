package io.yapilayer.platform.application.pis;

import io.yapilayer.platform.domain.payment.PaymentId;
import io.yapilayer.provider.sdk.ais.ProviderSession;
import java.util.Optional;

/** Storage port for payment provider sessions, implemented by platform-persistence. */
public interface PaymentSessionStorePort {

    void save(PaymentId paymentId, ProviderSession session);

    Optional<ProviderSession> byPaymentId(PaymentId paymentId);
}
