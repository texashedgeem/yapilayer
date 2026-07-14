# Roadmap

## Phase 1 — Working reference implementation (current)

Goal (PRODUCT_REQUIREMENTS §3/§19): a developer can clone the repo, `docker compose up`, run the AIS and PIS demos end-to-end against the mock bank, and understand how to extend the platform.

Each milestone ends with a self-checkpoint: commit, CHANGELOG.md entry, PROJECT_STATUS.md update, relevant ADR/DECISIONS.md entries — and CI must be green before advancing.

| # | Milestone | Scope | Status |
|---|-----------|-------|--------|
| 0 | Genesis & scaffolding | Directory layout, Gradle multi-module skeleton, npm workspaces, living docs, contribution framework, Genesis ADRs 0001–0008 | In progress |
| 1 | CI/CD skeleton | build.yml + test.yml protecting every later milestone from day one; security.yml (Dependabot, CodeQL); release.yml stubbed | Pending |
| 2 | Domain model & provider contract | `platform-domain` aggregates; `provider-sdk` ports/registry; multi-tenancy seam decision | Pending |
| 3 | Mock bank connector | `mock-bank-simulator` + `mock-bank-connector`; first docker-compose bring-up; bank-identity ADR | Pending |
| 4 | AIS vertical slice | Consent → PKCE redirect → callback → accounts → balances → transactions, with tests and Flyway migrations | Pending |
| 5 | PIS vertical slice | Payment creation → consent → authorisation → status lifecycle → webhooks with retry + signatures | Pending |
| 6 | Demo applications | ais-demo + pis-demo (React/TS, BFF pattern per ADR 0008); Playwright e2e | Pending |
| 7 | OpenAPI + SDKs | Spec reconciliation; Java + TypeScript SDK generation; contract tests | Pending |
| 8 | Docker Compose hardening | Full stack, health checks, `.env.example` — the single-command developer experience | Pending |
| 9 | CI/CD completion | Enforced scanning, static analysis, semver release automation | Pending |
| 10 | Documentation site | Full Mintlify content incl. "build a custom provider" guide | Pending |
| 11 | Contribution framework verification | Revisit contribution docs against the real module structure | Pending |
| 12 | Phase 1 DoD verification | `scripts/verify-phase1-dod.sh` against PRD §19, item by item | Pending |

## Phase 2 — Real bank connectivity (future)

- First real CMA9 sandbox connector (registration, certificates, FAPI-grade auth) — see ADR 0003 and D-1
- FAPI conformance work in `platform-security` (MTLS / private_key_jwt, signed request objects)
- Confirmation of Payee and Variable Recurring Payments (see D-3)
- Additional SDK languages (Python, Go, C#, PHP)

## Phase 3+ — Expansion (future)

- European PSD2 providers
- Kubernetes deployment target; validated cloud-neutrality (AWS/Azure/GCP)
- Multi-tenancy enforcement, rate limiting, observability stack (OpenTelemetry/Prometheus/Grafana)
- Global ecosystems (North America, APAC, Australia)
