package io.yapilayer.provider.sdk.pis;

import io.yapilayer.platform.domain.payment.PaymentStatus;
import io.yapilayer.provider.sdk.ais.ProviderSession;

/**
 * Payment Initiation Services port (ADR 0004). Implemented by connectors that
 * declare the {@code PIS} capability.
 *
 * <p>Flow: {@link #createPaymentConsent} → customer authorises at the bank via
 * the returned URL → platform exchanges the callback code with
 * {@link #exchangeAuthorisationCode} → {@link #submitPayment} → status polling
 * (or provider webhooks) until terminal.
 */
public interface PisProviderPort {

    /** Creates the provider-side payment consent and the customer authorisation URL. */
    PaymentAuthorisation createPaymentConsent(PaymentRequest request);

    /** Exchanges the OAuth authorisation code from the customer redirect for a session. */
    ProviderSession exchangeAuthorisationCode(String providerPaymentConsentId, String authorisationCode);

    /** Submits the authorised payment for execution; returns the provider's payment reference. */
    String submitPayment(ProviderSession session, String providerPaymentConsentId);

    PaymentStatus getPaymentStatus(ProviderSession session, String providerPaymentId);
}
