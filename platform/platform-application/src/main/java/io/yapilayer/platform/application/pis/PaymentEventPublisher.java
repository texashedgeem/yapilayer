package io.yapilayer.platform.application.pis;

import io.yapilayer.platform.domain.payment.Payment;

/** Outbound port for payment lifecycle events, implemented by platform-webhooks. */
public interface PaymentEventPublisher {

    /** Fired whenever a payment's status changes. */
    void paymentStatusChanged(Payment payment);
}
