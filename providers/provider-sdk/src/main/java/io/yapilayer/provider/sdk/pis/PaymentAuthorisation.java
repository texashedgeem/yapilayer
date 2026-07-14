package io.yapilayer.provider.sdk.pis;

import java.net.URI;
import java.util.Objects;

/**
 * Result of creating a provider-side payment consent: the provider's consent reference and the URL
 * the customer must visit to authorise the payment.
 */
public record PaymentAuthorisation(String providerPaymentConsentId, URI authorisationUrl) {

    public PaymentAuthorisation {
        Objects.requireNonNull(providerPaymentConsentId, "providerPaymentConsentId");
        Objects.requireNonNull(authorisationUrl, "authorisationUrl");
    }
}
