# ADR 0004

## Provider interface contract: capability-declaring ports in provider-sdk

## Context

The Constitution's plugin philosophy (§7) and ENGINEERING_STANDARDS §3.3 (Core Platform → Provider Interface → Bank Connector Plugin) require a stable contract that every bank connector implements and that the core platform depends on exclusively. This is the most architecturally load-bearing interface in the project.

## Decision

`providers/provider-sdk` defines the contract:

- `BankConnector` — the plugin root: identifies the provider (id, display name, supported country) and **declares its capabilities** (AIS, PIS, and later CoP/VRP) so the platform can discover what each connector supports rather than assuming.
- `AisProviderPort` / `PisProviderPort` — capability-specific ports a connector implements only if it declares that capability.
- `ProviderRegistry` — runtime registry the platform queries; connectors self-register (Spring auto-configuration in connector modules).
- Consent state is threaded through port method parameters as explicit domain objects (from `platform-domain`), never held as connector-internal mutable state.
- Signatures are synchronous in Phase 1 (blocking I/O, Spring MVC); an async migration would be a new major SDK version.

Detailed interface design lands in Milestone 2; this ADR fixes the shape.

## Alternatives

- **One fat `BankConnector` interface with optional methods** (`UnsupportedOperationException` for absent capabilities): rejected — runtime surprises instead of compile-time clarity; capability discovery becomes trial-and-error.
- **Async-first (reactive) signatures**: rejected for Phase 1 — Spring WebFlux would complicate every layer and contributor onboarding for throughput Phase 1 does not need. Recorded in TECH_DEBT.md for revisit.

## Consequences

- New connectors implement only the ports for capabilities they actually support; the platform can list per-provider capabilities via API.
- The contract lives in its own artifact, versioned independently of connectors, enabling out-of-tree community connectors later.

## Status

Accepted (2026-07-14) — interface details to be finalized in Milestone 2.
