package io.yapilayer.platform.domain.common;

import java.util.Objects;
import java.util.UUID;

/**
 * Identifies the tenant (operating organisation) that owns a resource.
 *
 * <p>Multi-tenancy seam (ADR 0009): every aggregate carries a tenant identity from day one because
 * retrofitting one is expensive. Phase 1 runs single-tenant — {@link #DEFAULT} — and no isolation
 * is enforced yet (see KNOWN_GAPS.md).
 */
public record TenantId(UUID value) {

    /** The single tenant used throughout Phase 1. */
    public static final TenantId DEFAULT =
            new TenantId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

    public TenantId {
        Objects.requireNonNull(value, "value");
    }
}
