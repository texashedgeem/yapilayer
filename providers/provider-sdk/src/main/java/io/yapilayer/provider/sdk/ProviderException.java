package io.yapilayer.provider.sdk;

import io.yapilayer.platform.domain.common.ProviderId;

/**
 * Base exception for provider connector failures. Connectors wrap bank-specific errors in this type
 * so provider details never leak upward.
 */
public class ProviderException extends RuntimeException {

    private final ProviderId providerId;

    public ProviderException(ProviderId providerId, String message) {
        super(message);
        this.providerId = providerId;
    }

    public ProviderException(ProviderId providerId, String message, Throwable cause) {
        super(message, cause);
        this.providerId = providerId;
    }

    public ProviderId providerId() {
        return providerId;
    }
}
