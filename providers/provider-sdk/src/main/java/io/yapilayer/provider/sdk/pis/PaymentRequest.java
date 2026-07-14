package io.yapilayer.provider.sdk.pis;

import io.yapilayer.platform.domain.common.Money;
import io.yapilayer.platform.domain.payment.Creditor;

import java.net.URI;
import java.util.Objects;

/**
 * Request to create a provider-side payment consent for a single domestic payment.
 *
 * @param redirectUri where the bank sends the customer after authorisation
 * @param state       opaque anti-CSRF value the bank must echo back on redirect
 */
public record PaymentRequest(
        Money amount,
        Creditor creditor,
        String reference,
        URI redirectUri,
        String state) {

    public PaymentRequest {
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(creditor, "creditor");
        Objects.requireNonNull(redirectUri, "redirectUri");
        Objects.requireNonNull(state, "state");
        if (reference == null || reference.isBlank()) {
            throw new IllegalArgumentException("payment reference must not be blank");
        }
        if (!amount.isPositive()) {
            throw new IllegalArgumentException("payment amount must be positive");
        }
    }
}
