# ADR 0009

## Multi-tenancy seam in the domain model, enforcement deferred

## Context

The foundational documents never mention multi-tenancy, yet the platform targets "fintech companies" and "enterprises" (PRODUCT_REQUIREMENTS §4) that would operate it on behalf of many customers. Tenant scoping retrofitted into an existing domain model and schema is one of the most expensive refactorings there is; carried as an unused field from day one it costs almost nothing.

## Decision

Every aggregate that represents customer-scoped state (`Consent`, `Payment`; account data follows the consent that grants it) carries a `TenantId` from Milestone 2 onward, and Phase 1 persists it. Phase 1 runs single-tenant using `TenantId.DEFAULT`; no isolation (row-level security, per-tenant API credentials, tenant-scoped audit) is enforced yet — that is Phase 2+ work tracked in KNOWN_GAPS.md.

## Alternatives

- **No tenant concept until needed**: rejected — the retrofit cost lands exactly when the project can least afford it (first enterprise adopter).
- **Full multi-tenant isolation in Phase 1**: rejected — significant scope (auth model, RLS, key scoping) with zero Phase 1 benefit against a mock bank.

## Consequences

- Schema and domain carry `tenant_id` columns/fields that are constant in Phase 1.
- Phase 2 multi-tenancy becomes an authorisation/enforcement feature, not a data-model migration.

## Status

Accepted (2026-07-14)
