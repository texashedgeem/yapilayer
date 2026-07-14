# Known Gaps

Deliberately unimplemented or unresolved items, tracked so they are never silent. See also RISKS.md and TECH_DEBT.md.

| Gap | Why it exists | Planned resolution |
|-----|---------------|--------------------|
| No real bank connectivity | Mock-first strategy (ADR 0003, D-1) | Phase 2: first real CMA9 sandbox connector |
| FAPI conformance not exercised (MTLS, signed request objects, eIDAS/QSEALC certs) | Meaningless against a mock; requires real TPP registration | Phase 2, alongside real connector; SECURITY.md records the boundary |
| Rate limiting absent | Never specified in foundational docs despite OWASP API Top 10 requirement | Seam decision (gateway vs filter) recorded when platform-api solidifies; implementation Phase 2+ |
| No data-retention / GDPR policy | Phase 1 processes synthetic data only | Required before any real-bank connector ships; noted in SECURITY.md |
| Multi-tenancy not enforced | Phase 1 is single-operator | Domain model carries a tenant seam (decided in Milestone 2, ADR to follow); enforcement Phase 2+ |
| Cloud-neutrality unvalidated beyond Docker Compose | Phase 1 targets Compose only | RISKS.md item; smoke-test planned (bare `docker run` against external Postgres); K8s in Phase 3 |
| Confirmation of Payee, VRP unsupported | Phase 2+ per D-3 | Capability identifiers reserved in provider contract (ADR 0004) |
| Performance test suite absent | Deferred per D-4 | TECH_DEBT.md; revisit when real connectivity exists |
