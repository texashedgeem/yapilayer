package io.yapilayer.provider.sdk.ais;

import java.net.URI;
import java.util.Objects;

/**
 * Result of creating a provider-side consent: the provider's consent reference
 * and the URL the customer must visit to authorise it.
 */
public record ConsentAuthorisation(String providerConsentId, URI authorisationUrl) {

    public ConsentAuthorisation {
        Objects.requireNonNull(providerConsentId, "providerConsentId");
        Objects.requireNonNull(authorisationUrl, "authorisationUrl");
    }
}
