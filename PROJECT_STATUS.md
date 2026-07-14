# Project Status

**Phase:** 1 — reference implementation
**Current milestone:** 11 — Contribution framework verification
**Last updated:** 2026-07-14

## Now

Milestone 11: revisiting CONTRIBUTING/CODE_OF_CONDUCT/SECURITY/templates against the real module structure, then Milestone 12's Definition-of-Done pass.

## Next

Milestone 12 — Phase 1 DoD verification (`scripts/verify-phase1-dod.sh`).

## Notes for maintainer

- Mintlify hosting: the docs site content is complete in `docs/` and previews locally (`npx mint dev`); publishing needs a Mintlify account/hosting decision when wanted (see RISKS.md).
- Ten superseded Dependabot PRs (#1–#10) remain open — close at leisure.

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
| 2026-07-14 | M9 complete | Spotless enforced, SDK-freshness + spec-lint CI jobs, Trivy scanning (caught+fixed jackson ACE CVE, non-root containers), release.yml completed, e2e workflow green in Actions |
| 2026-07-14 | M10 complete | Full Mintlify docs site: 13 pages, 4 groups, link-validated |

## Blockers

None.
