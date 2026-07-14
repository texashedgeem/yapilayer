package io.yapilayer.platform.application.pis;

import io.yapilayer.platform.application.ais.AisService;
import io.yapilayer.platform.domain.common.Money;
import io.yapilayer.platform.domain.common.ProviderId;
import io.yapilayer.platform.domain.common.TenantId;
import io.yapilayer.platform.domain.payment.Creditor;
import io.yapilayer.platform.domain.payment.Payment;
import io.yapilayer.platform.domain.payment.PaymentId;
import io.yapilayer.platform.domain.payment.PaymentStatus;
import io.yapilayer.provider.sdk.BankConnector;
import io.yapilayer.provider.sdk.ProviderRegistry;
import io.yapilayer.provider.sdk.ais.ProviderSession;
import io.yapilayer.provider.sdk.pis.PaymentAuthorisation;
import io.yapilayer.provider.sdk.pis.PaymentRequest;
import io.yapilayer.provider.sdk.pis.PisProviderPort;

import java.net.URI;
import java.security.SecureRandom;
import java.time.Clock;
import java.util.HexFormat;

/**
 * PIS use cases: payment creation, authorisation callback (which submits the
 * payment), and status refresh with webhook notification on change.
 *
 * <p>Plain Java — wired as a bean by platform-bootstrap.
 */
public class PisService {

    private final ProviderRegistry providers;
    private final PaymentRepositoryPort payments;
    private final PaymentSessionStorePort sessions;
    private final PaymentEventPublisher events;
    private final URI platformCallbackUri;
    private final Clock clock;
    private final SecureRandom random = new SecureRandom();

    public PisService(ProviderRegistry providers,
                      PaymentRepositoryPort payments,
                      PaymentSessionStorePort sessions,
                      PaymentEventPublisher events,
                      URI platformCallbackUri,
                      Clock clock) {
        this.providers = providers;
        this.payments = payments;
        this.sessions = sessions;
        this.events = events;
        this.platformCallbackUri = platformCallbackUri;
        this.clock = clock;
    }

    public record CreatedPayment(Payment payment, URI authorisationUrl) {}

    public record CallbackResult(Payment payment, URI clientRedirectUri) {}

    /** Creates the payment and its provider consent; returns the bank authorisation URL. */
    public CreatedPayment createPayment(ProviderId providerId, Money amount,
                                        Creditor creditor, String reference,
                                        URI clientRedirectUri) {
        PisProviderPort port = pisPort(providerId);
        Payment payment = Payment.create(TenantId.DEFAULT, providerId, amount,
                creditor, reference, clock.instant());

        String state = newState();
        PaymentAuthorisation authorisation = port.createPaymentConsent(new PaymentRequest(
                amount, creditor, reference, platformCallbackUri, state));

        payments.save(payment, state, clientRedirectUri,
                authorisation.providerPaymentConsentId());
        return new CreatedPayment(payment, authorisation.authorisationUrl());
    }

    /**
     * Completes the authorisation journey: exchanges the code, submits the
     * payment to the provider, and records the transition to AUTHORISED.
     */
    public CallbackResult handleCallback(String state, String authorisationCode) {
        PaymentRepositoryPort.PaymentWithJourney journey = payments.byOauthState(state)
                .orElseThrow(() -> new AisService.NotFoundException("no payment for callback state"));
        Payment payment = journey.payment();
        PisProviderPort port = pisPort(payment.providerId());

        ProviderSession session = port.exchangeAuthorisationCode(
                journey.providerPaymentConsentId(), authorisationCode);
        sessions.save(payment.id(), session);

        String providerPaymentId = port.submitPayment(session, journey.providerPaymentConsentId());
        Payment authorised = payment
                .withProviderPaymentId(providerPaymentId, clock.instant())
                .transitionTo(PaymentStatus.AUTHORISED, clock.instant());
        payments.update(authorised);
        events.paymentStatusChanged(authorised);
        return new CallbackResult(authorised, journey.clientRedirectUri());
    }

    /** Records a denied authorisation. */
    public CallbackResult handleDenied(String state) {
        PaymentRepositoryPort.PaymentWithJourney journey = payments.byOauthState(state)
                .orElseThrow(() -> new AisService.NotFoundException("no payment for callback state"));
        Payment rejected = journey.payment()
                .transitionTo(PaymentStatus.REJECTED, clock.instant());
        payments.update(rejected);
        events.paymentStatusChanged(rejected);
        return new CallbackResult(rejected, journey.clientRedirectUri());
    }

    /**
     * Returns the payment, first refreshing its status from the provider when
     * it is still in flight. Status changes are persisted and published.
     */
    public Payment getPayment(PaymentId id) {
        Payment payment = payments.byId(id)
                .orElseThrow(() -> new AisService.NotFoundException("payment not found"));
        if (payment.status().isTerminal() || payment.providerPaymentId().isEmpty()) {
            return payment;
        }
        ProviderSession session = sessions.byPaymentId(id)
                .orElseThrow(() -> new AisService.NotFoundException("no session for payment"));

        PaymentStatus providerStatus = pisPort(payment.providerId())
                .getPaymentStatus(session, payment.providerPaymentId().orElseThrow());
        if (providerStatus == payment.status() || !payment.status().canTransitionTo(providerStatus)) {
            return payment;
        }
        Payment updated = payment.transitionTo(providerStatus, clock.instant());
        payments.update(updated);
        events.paymentStatusChanged(updated);
        return updated;
    }

    private PisProviderPort pisPort(ProviderId providerId) {
        BankConnector connector = providers.byId(providerId)
                .orElseThrow(() -> new AisService.NotFoundException(
                        "unknown provider: " + providerId.value()));
        return connector.pisPort()
                .orElseThrow(() -> new AisService.NotFoundException(
                        "provider does not support PIS: " + providerId.value()));
    }

    private String newState() {
        byte[] bytes = new byte[24];
        random.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }
}
