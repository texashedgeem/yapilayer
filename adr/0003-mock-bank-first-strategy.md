# ADR 0003

## Mock bank connector first; real CMA9 sandbox deferred beyond Phase 1

## Context

PRODUCT_REQUIREMENTS.md §19 lists "at least one CMA9 sandbox integration works" as a Phase 1 Definition-of-Done item. Real Open Banking sandbox access requires developer-portal registration and, for full flows, TPP certificates — external dependencies that would block an autonomous build run and that no one on the project currently holds. The project owner (Simon Hewins) explicitly decided on 2026-07-14 to reinterpret this DoD item.

## Decision

Phase 1 ships a **spec-compliant mock bank** in two deliberately separated modules:

- `mock-bank-simulator` — a standalone service simulating an ASPSP (its own OAuth 2.0 endpoints, consent journey, account/transaction/payment data), run as a separate process so the connector exercises a realistic network boundary.
- `mock-bank-connector` — a normal `provider-sdk` implementation that happens to point at the simulator.

The Phase 1 DoD item is formally reinterpreted as: **"at least one bank connector works end-to-end against the mock bank simulator."** Real-sandbox integration is a Phase 2 roadmap item, not a Phase 1 gap.

## Alternatives

- **Real bank sandbox from day one**: rejected — external registration friction would stall the autonomous build; credentials do not exist yet.
- **In-process stub instead of a standalone simulator**: rejected — a fused stub would make "swap mock for real bank" a rewrite instead of a new connector module, undermining the provider abstraction this project exists to prove.

## Consequences

- Phase 1 completes with zero external dependencies; every AIS/PIS journey is demonstrable offline.
- The connector/simulator seam means the first real bank connector (Phase 2) is additive: new module, no core or mock changes.
- SECURITY.md must record that full FAPI conformance (MTLS, signed request objects, eIDAS certs) is not exercised against a mock and remains open for Phase 2.

## Status

Accepted (2026-07-14) — decision made by project owner, recorded verbatim rather than silently rewording the PRD.
