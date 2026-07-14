# tests/

Cross-module test suites (module-level unit tests live inside each module):

- `integration/` — cross-module integration tests (Testcontainers: PostgreSQL + mock bank) — from Milestone 4
- `contract/` — OpenAPI contract tests + the provider-sdk conformance suite every connector must pass — from Milestone 7
- `e2e/` — Playwright tests driving the AIS/PIS demo apps — from Milestone 6
