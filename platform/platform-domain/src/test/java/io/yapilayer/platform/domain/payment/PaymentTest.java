package io.yapilayer.platform.domain.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import io.yapilayer.platform.domain.account.AccountIdentifier;
import io.yapilayer.platform.domain.common.Money;
import io.yapilayer.platform.domain.common.ProviderId;
import io.yapilayer.platform.domain.common.TenantId;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class PaymentTest {

    private static final Instant NOW = Instant.parse("2026-07-14T12:00:00Z");
    private static final Creditor CREDITOR =
            new Creditor(
                    "Acme Ltd",
                    new AccountIdentifier("UK.OBIE.SortCodeAccountNumber", "20000012345678"));

    private Payment newPayment() {
        return Payment.create(
                TenantId.DEFAULT,
                new ProviderId("mock-bank"),
                Money.of("25.00", "GBP"),
                CREDITOR,
                "INV-001",
                NOW);
    }

    @Test
    void createsPending() {
        assertThat(newPayment().status()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    void rejectsNonPositiveAmount() {
        assertThatIllegalArgumentException()
                .isThrownBy(
                        () ->
                                Payment.create(
                                        TenantId.DEFAULT,
                                        new ProviderId("mock-bank"),
                                        Money.of("0.00", "GBP"),
                                        CREDITOR,
                                        "INV-002",
                                        NOW));
    }

    @Test
    void fullHappyLifecycle() {
        Payment completed =
                newPayment()
                        .transitionTo(PaymentStatus.AUTHORISED, NOW)
                        .transitionTo(PaymentStatus.COMPLETED, NOW);
        assertThat(completed.status()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(completed.status().isTerminal()).isTrue();
    }

    @Test
    void cannotCompleteWithoutAuthorisation() {
        assertThatIllegalStateException()
                .isThrownBy(() -> newPayment().transitionTo(PaymentStatus.COMPLETED, NOW));
    }

    @Test
    void terminalStatesAcceptNoTransitions() {
        Payment cancelled = newPayment().transitionTo(PaymentStatus.CANCELLED, NOW);
        for (PaymentStatus target : PaymentStatus.values()) {
            assertThatIllegalStateException().isThrownBy(() -> cancelled.transitionTo(target, NOW));
        }
    }

    @Test
    void providerReferenceIsRecorded() {
        Payment withRef = newPayment().withProviderPaymentId("bank-pay-9", NOW);
        assertThat(withRef.providerPaymentId()).contains("bank-pay-9");
    }
}
