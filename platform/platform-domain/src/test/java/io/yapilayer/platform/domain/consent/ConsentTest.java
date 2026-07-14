package io.yapilayer.platform.domain.consent;

import io.yapilayer.platform.domain.common.ProviderId;
import io.yapilayer.platform.domain.common.TenantId;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

class ConsentTest {

    private static final Instant NOW = Instant.parse("2026-07-14T12:00:00Z");
    private static final Instant LATER = NOW.plus(Duration.ofDays(90));

    private Consent newConsent() {
        return Consent.create(
                TenantId.DEFAULT,
                new ProviderId("mock-bank"),
                Set.of(Permission.READ_ACCOUNTS, Permission.READ_BALANCES),
                NOW,
                LATER);
    }

    @Test
    void createsAwaitingAuthorisation() {
        Consent consent = newConsent();
        assertThat(consent.status()).isEqualTo(ConsentStatus.AWAITING_AUTHORISATION);
        assertThat(consent.providerConsentId()).isEmpty();
    }

    @Test
    void requiresAtLeastOnePermission() {
        assertThatIllegalArgumentException().isThrownBy(() ->
                Consent.create(TenantId.DEFAULT, new ProviderId("mock-bank"),
                        Set.of(), NOW, LATER));
    }

    @Test
    void authoriseHappyPath() {
        Consent authorised = newConsent().authorise(NOW.plusSeconds(60));
        assertThat(authorised.status()).isEqualTo(ConsentStatus.AUTHORISED);
    }

    @Test
    void cannotAuthoriseAfterExpiry() {
        assertThatIllegalStateException().isThrownBy(() ->
                newConsent().authorise(LATER.plusSeconds(1)));
    }

    @Test
    void cannotAuthoriseTwice() {
        Consent authorised = newConsent().authorise(NOW);
        assertThatIllegalStateException().isThrownBy(() -> authorised.authorise(NOW));
    }

    @Test
    void revokeRequiresAuthorised() {
        assertThatIllegalStateException().isThrownBy(() -> newConsent().revoke());
        Consent revoked = newConsent().authorise(NOW).revoke();
        assertThat(revoked.status()).isEqualTo(ConsentStatus.REVOKED);
    }

    @Test
    void cannotExpireTerminalConsent() {
        Consent rejected = newConsent().reject();
        assertThatIllegalStateException().isThrownBy(rejected::expire);
    }

    @Test
    void transitionsPreserveIdentityAndPermissions() {
        Consent original = newConsent();
        Consent authorised = original.withProviderConsentId("bank-ref-1").authorise(NOW);
        assertThat(authorised.id()).isEqualTo(original.id());
        assertThat(authorised.permissions()).isEqualTo(original.permissions());
        assertThat(authorised.providerConsentId()).contains("bank-ref-1");
    }
}
