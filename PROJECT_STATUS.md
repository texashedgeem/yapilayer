# Project Status

**Phase:** 1 — reference implementation
**Current milestone:** 10 — Documentation site
**Last updated:** 2026-07-14

## Now

Milestone 10: populating the Mintlify documentation site — getting started, quickstart, AIS/PIS guides, provider development guide, architecture, deployment, SDKs.

## Next

Milestone 11 — contribution framework verification.

## Notes for maintainer

Ten Dependabot PRs (#1–#10) opened on first activation are superseded: majors are now excluded by policy (.github/dependabot.yml) and the one patch bump was applied directly (commit 85f8f84). They can be closed at leisure — the agent's permission layer declined mass-closing them autonomously.

## Milestone history

| Date | Milestone | Outcome |
|------|-----------|---------|
| 2026-07-14 | Genesis (pre-M0) | Repo created, foundational docs committed, pushed to github.com/texashedgeem/yapilayer |
| 2026-07-14 | M0 complete | Gradle multi-module skeleton (build green), npm workspaces, ADRs 0001–0008, living docs, contribution framework (commit d83be1e) |
| 2026-07-14 | M1 complete | CI green on main: build, test, security workflows (commit 067bae7) |
| 2026-07-14 | M2 complete | Domain aggregates + provider-sdk contract + ADR 0009, 18 unit tests green |
| 2026-07-14 | M3 complete | Mock bank simulator + connector, end-to-end AIS/PIS acceptance test, compose stack healthy (3 services), ADR 0010 |
| 2026-07-14 | M4 complete | AIS vertical slice: consent API → callback → accounts/balances/transactions, Flyway persistence, integration test on Testcontainers + simulator, compose re-verified |
| 2026-07-14 | M5 complete | PIS vertical slice: payments API → authorise → submit → status lifecycle, signed webhooks with retry (ADR 0011), full journey test incl. HMAC verification |
| 2026-07-14 | M6 complete | ais-demo + pis-demo SPAs (platform-as-BFF per ADR 0008 resolution), 3 Playwright e2e specs green against the compose stack, public/internal base-url split |
| 2026-07-14 | M7 complete | TS + Java SDKs generated (ADR 0012), contract tests via generated SDK green, spec lints clean |
| 2026-07-14 | M8 complete | Five-service compose stack healthy from one command, demos nginx-served, e2e green against containers, .env.example + quickstart docs |
| 2026-07-14 | M9 complete | Spotless enforced, SDK-freshness + spec-lint CI jobs, Trivy scanning, release.yml completed, e2e workflow on main pushes |

## Blockers

None.
