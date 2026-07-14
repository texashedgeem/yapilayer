# Project Status

**Phase:** 1 — reference implementation: **COMPLETE** ✅
**Verified:** 2026-07-14, `scripts/verify-phase1-dod.sh` — 41/41 PRD §19 checks pass
**Last updated:** 2026-07-14

## What exists

A working open-source Open Banking reference implementation:

- `docker compose up -d --build` brings up five healthy services (postgres, mock bank, platform, two demo apps) with no local toolchain
- Complete AIS journeys (consent → authorise → accounts/balances/paginated transactions) and PIS journeys (payment → authorise → status lifecycle → HMAC-signed webhooks) — proven by integration tests on Testcontainers, SDK contract tests, and Playwright browser e2e, all green in CI
- Provider plugin framework with a NatWest-flavoured mock bank as the reference connector
- Generated TypeScript and Java SDKs, spec-first with CI drift protection
- 13-page documentation site, 12 ADRs, complete living-docs set
- CI: build/format, tests, CodeQL (Java+TS), Trivy, dependency review, full-stack e2e, release automation

## Phase 2 candidates (need project-owner decisions)

| Item | What it needs from Simon |
|------|--------------------------|
| First real bank connector (NatWest per ADR 0010) | Bank of APIs developer-portal registration (credentials) |
| SDK publishing (npm / Maven Central) | Registry accounts + tokens |
| Docs site hosting | Mintlify account/tier decision |
| Platform API authentication + multi-tenancy enforcement | Prioritisation |
| FAPI conformance in platform-security | Follows real connector |

## Housekeeping

- Ten superseded Dependabot PRs (#1–#10) remain open — close at leisure
- A `v0.1.0` tag would exercise the release pipeline end-to-end when desired

## Milestone history

| Date | Milestone | Outcome |
|------|-----------|---------|
| 2026-07-14 | Genesis (pre-M0) | Repo created, foundational docs, pushed to github.com/texashedgeem/yapilayer |
| 2026-07-14 | M0 | Gradle multi-module skeleton, npm workspaces, ADRs 0001–0008, living docs |
| 2026-07-14 | M1 | CI skeleton green on main |
| 2026-07-14 | M2 | Domain aggregates + provider-sdk contract, 18 unit tests |
| 2026-07-14 | M3 | Mock bank simulator + connector, e2e acceptance test, first compose bring-up, ADR 0010 |
| 2026-07-14 | M4 | AIS vertical slice with Testcontainers journey test |
| 2026-07-14 | M5 | PIS vertical slice with signed webhooks (ADR 0011) |
| 2026-07-14 | M6 | Demo apps + Playwright e2e, ADR 0008 resolved |
| 2026-07-14 | M7 | TS + Java SDKs, contract tests (ADR 0012) |
| 2026-07-14 | M8 | Five-service single-command compose stack |
| 2026-07-14 | M9 | CI completion — Trivy caught+fixed a real jackson ACE CVE; e2e in Actions |
| 2026-07-14 | M10 | 13-page documentation site, link-validated |
| 2026-07-14 | M11+M12 | Contribution docs verified; **DoD 41/41 — Phase 1 complete** |
