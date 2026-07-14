# Project Status

**Phase:** 1 — reference implementation
**Current milestone:** 3 — Mock bank connector
**Last updated:** 2026-07-14

## Now

Milestone 3: building the mock bank simulator (standalone fake ASPSP) and the connector implementing provider-sdk against it; first docker-compose bring-up; researching which CMA9 bank/group the mock models (ADR to follow).

## Next

Milestone 4 — AIS vertical slice end-to-end.

## Notes for maintainer

Ten Dependabot PRs (#1–#10) opened on first activation are superseded: majors are now excluded by policy (.github/dependabot.yml) and the one patch bump was applied directly (commit 85f8f84). They can be closed at leisure — the agent's permission layer declined mass-closing them autonomously.

## Milestone history

| Date | Milestone | Outcome |
|------|-----------|---------|
| 2026-07-14 | Genesis (pre-M0) | Repo created, foundational docs committed, pushed to github.com/texashedgeem/yapilayer |
| 2026-07-14 | M0 complete | Gradle multi-module skeleton (build green), npm workspaces, ADRs 0001–0008, living docs, contribution framework (commit d83be1e) |
| 2026-07-14 | M1 complete | CI green on main: build, test, security workflows (commit 067bae7) |
| 2026-07-14 | M2 complete | Domain aggregates + provider-sdk contract + ADR 0009, 18 unit tests green |

## Blockers

None.
