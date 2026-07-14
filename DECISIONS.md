# Decisions

Significant decisions, per PROJECT_CONSTITUTION.md §10. Architecture decisions have full ADRs in [`adr/`](./adr/); this file indexes them and records document-interpretation decisions that resolve contradictions in the foundational docs.

## ADR Index

| ADR | Decision | Status |
|-----|----------|--------|
| [0001](adr/0001-monorepo-and-module-boundaries.md) | Monorepo, layer-per-module, plugin-per-connector | Accepted |
| [0002](adr/0002-gradle-over-maven.md) | Gradle (Kotlin DSL) over Maven | Accepted |
| [0003](adr/0003-mock-bank-first-strategy.md) | Mock bank first; real CMA9 sandbox deferred to Phase 2 | Accepted |
| [0004](adr/0004-provider-interface-contract.md) | Capability-declaring provider ports in provider-sdk | Accepted |
| [0005](adr/0005-openapi-spec-first.md) | OpenAPI spec-first | Accepted |
| [0006](adr/0006-flyway-over-liquibase.md) | Flyway migrations | Accepted |
| [0007](adr/0007-spring-data-jpa.md) | Spring Data JPA behind a framework-free domain | Accepted |
| [0008](adr/0008-bff-auth-for-demo-apps.md) | BFF auth pattern for demo apps | Accepted |
| [0009](adr/0009-multi-tenancy-seam.md) | Multi-tenancy seam in domain model, enforcement deferred | Accepted |
| [0010](adr/0010-mock-bank-identity-natwest.md) | Mock bank models NatWest Group's OB implementation | Accepted |
| [0011](adr/0011-webhook-delivery.md) | Webhooks: at-least-once, HMAC-SHA256, bounded in-memory retry | Accepted |
| [0012](adr/0012-sdk-generation-tooling.md) | SDKs: openapi-generator-cli, typescript-fetch + java-native, committed | Accepted |

## Document-Interpretation Decisions

### D-1 — Phase 1 DoD reinterpretation: mock bank satisfies the "CMA9 sandbox" item
**Date:** 2026-07-14 · **Decided by:** Project owner (Simon Hewins)
PRODUCT_REQUIREMENTS §19's "at least one CMA9 sandbox integration works" is reinterpreted for Phase 1 as "at least one bank connector works end-to-end against the mock bank simulator". Real-sandbox integration moves to Phase 2. Full reasoning in [ADR 0003](adr/0003-mock-bank-first-strategy.md).

### D-2 — `/docs` vs `/mintlify` directory contradiction
**Date:** 2026-07-14
ENGINEERING_STANDARDS §5 requires both `/docs` and `/mintlify` top-level directories, but §22's Mintlify structure is written as if `docs/` *is* the Mintlify project root, leaving `/mintlify` without defined content. **Resolution:** `docs/` holds the Mintlify site exactly per §22; `mintlify/` holds only Mintlify deployment/CI configuration. Alternatives: dropping `/mintlify` entirely (rejected — §5 names it explicitly); putting the site in `/mintlify` (rejected — contradicts §22's literal paths).

### D-3 — Confirmation of Payee & VRP are Phase 2+, not Phase 1
**Date:** 2026-07-14
PRODUCT_REQUIREMENTS §8 lists CoP and VRP among "standards to support" while §16 places both in the "Future Functional Roadmap". **Resolution:** §16 is authoritative — both are Phase 2+. The provider capability model (ADR 0004) reserves capability identifiers for them so connectors can declare support later without contract changes.

### D-4 — Performance testing deferred from Phase 1
**Date:** 2026-07-14
ENGINEERING_STANDARDS §17 lists performance testing as unconditionally mandatory alongside unit/integration/contract testing. For a Phase 1 reference implementation running against a mock bank, a full performance suite (latency/concurrency/throughput) measures nothing meaningful. **Resolution:** unit, integration and contract tests are mandatory in Phase 1; automated security scanning runs in CI; the performance suite is deferred and tracked in TECH_DEBT.md rather than silently omitted.

### D-5 — CMA9 list redundancy acknowledged; first reference bank chosen during Milestone 3
**Date:** 2026-07-14
PRODUCT_REQUIREMENTS §9 lists nine bank brands as if independent, but RBS is part of NatWest Group and Bank of Scotland/Halifax are part of Lloyds Banking Group — the real integration surface is smaller than nine. **Resolution:** the mock bank's behavioural "identity" (which group's published API characteristics it models) is researched and chosen during Milestone 3 and recorded as an execution-time ADR, per project-owner instruction.
