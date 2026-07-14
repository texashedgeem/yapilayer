package io.yapilayer.provider.sdk;

/**
 * Capabilities a bank connector may declare (ADR 0004).
 *
 * <p>{@code COP} and {@code VRP} are reserved for Phase 2+ (DECISIONS.md D-3); declaring them has
 * no effect until the corresponding ports exist.
 */
public enum ProviderCapability {
    /** Account Information Services. */
    AIS,
    /** Payment Initiation Services. */
    PIS,
    /** Confirmation of Payee (reserved, Phase 2+). */
    COP,
    /** Variable Recurring Payments (reserved, Phase 2+). */
    VRP
}
