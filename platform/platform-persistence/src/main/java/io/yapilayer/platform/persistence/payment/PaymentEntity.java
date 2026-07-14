package io.yapilayer.platform.persistence.payment;

import io.yapilayer.platform.domain.account.AccountIdentifier;
import io.yapilayer.platform.domain.common.Money;
import io.yapilayer.platform.domain.common.ProviderId;
import io.yapilayer.platform.domain.common.TenantId;
import io.yapilayer.platform.domain.payment.Creditor;
import io.yapilayer.platform.domain.payment.Payment;
import io.yapilayer.platform.domain.payment.PaymentId;
import io.yapilayer.platform.domain.payment.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

/** JPA mapping for payments; converts to/from the domain aggregate (ADR 0007). */
@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "creditor_name", nullable = false)
    private String creditorName;

    @Column(name = "creditor_scheme", nullable = false)
    private String creditorScheme;

    @Column(name = "creditor_identification", nullable = false)
    private String creditorIdentification;

    @Column(nullable = false)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "provider_payment_id")
    private String providerPaymentId;

    @Column(name = "provider_payment_consent_id")
    private String providerPaymentConsentId;

    @Column(name = "oauth_state", unique = true)
    private String oauthState;

    @Column(name = "client_redirect_uri")
    private String clientRedirectUri;

    protected PaymentEntity() {
        // JPA
    }

    public static PaymentEntity fromDomain(
            Payment payment,
            String oauthState,
            URI clientRedirectUri,
            String providerPaymentConsentId) {
        PaymentEntity entity = new PaymentEntity();
        entity.id = payment.id().value();
        entity.oauthState = oauthState;
        entity.clientRedirectUri = clientRedirectUri.toString();
        entity.providerPaymentConsentId = providerPaymentConsentId;
        entity.applyDomain(payment);
        return entity;
    }

    public void applyDomain(Payment payment) {
        this.tenantId = payment.tenantId().value();
        this.providerId = payment.providerId().value();
        this.amount = payment.amount().amount();
        this.currency = payment.amount().currency().getCurrencyCode();
        this.creditorName = payment.creditor().name();
        this.creditorScheme = payment.creditor().account().scheme();
        this.creditorIdentification = payment.creditor().account().identification();
        this.reference = payment.reference();
        this.status = payment.status();
        this.createdAt = payment.createdAt();
        this.updatedAt = payment.updatedAt();
        this.providerPaymentId = payment.providerPaymentId().orElse(null);
    }

    public Payment toDomain() {
        return new Payment(
                new PaymentId(id),
                new TenantId(tenantId),
                new ProviderId(providerId),
                new Money(amount, Currency.getInstance(currency)),
                new Creditor(
                        creditorName,
                        new AccountIdentifier(creditorScheme, creditorIdentification)),
                reference,
                status,
                createdAt,
                updatedAt,
                Optional.ofNullable(providerPaymentId));
    }

    public String getOauthState() {
        return oauthState;
    }

    public URI getClientRedirectUri() {
        return URI.create(clientRedirectUri);
    }

    public String getProviderPaymentConsentId() {
        return providerPaymentConsentId;
    }
}
