# Project Status

**Phase:** 1 — reference implementation
**Current milestone:** 2 — Domain model & provider-interface contract
**Last updated:** 2026-07-14

## Now

Milestone 2: designing `platform-domain` aggregates and the `provider-sdk` plugin contract (the most architecturally consequential milestone).

## Next

Milestone 3 — Mock bank simulator + connector, first docker-compose bring-up.

## Notes for maintainer

Ten Dependabot PRs (#1–#10) opened on first activation are superseded: majors are now excluded by policy (.github/dependabot.yml) and the one patch bump was applied directly (commit 85f8f84). They can be closed at leisure — the agent's permission layer declined mass-closing them autonomously.

## Milestone history

| Date | Milestone | Outcome |
|------|-----------|---------|
| 2026-07-14 | Genesis (pre-M0) | Repo created, foundational docs committed, pushed to github.com/texashedgeem/yapilayer |
| 2026-07-14 | M0 complete | Gradle multi-module skeleton (build green), npm workspaces, ADRs 0001–0008, living docs, contribution framework (commit d83be1e) |
| 2026-07-14 | M1 started | CI/CD workflows: build, test, security (CodeQL + dependency review + Dependabot), release stub |

## Blockers

None.
