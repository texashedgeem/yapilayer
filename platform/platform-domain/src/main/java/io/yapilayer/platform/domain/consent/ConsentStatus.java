package io.yapilayer.platform.domain.consent;

import java.util.Set;

/** Consent lifecycle, aligned with UK Open Banking consent states. */
public enum ConsentStatus {
    AWAITING_AUTHORISATION,
    AUTHORISED,
    REJECTED,
    REVOKED,
    EXPIRED;

    private static final Set<ConsentStatus> TERMINAL = Set.of(REJECTED, REVOKED, EXPIRED);

    public boolean isTerminal() {
        return TERMINAL.contains(this);
    }
}
