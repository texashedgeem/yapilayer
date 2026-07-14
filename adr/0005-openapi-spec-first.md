# ADR 0005

## OpenAPI spec-first, not code-first

## Context

The Constitution's "API First" principle (§6) and ENGINEERING_STANDARDS §9 require every API to have OpenAPI definitions, examples, defined errors and versioning. Two workflows exist: write the spec and generate server stubs/SDKs from it, or annotate code and generate the spec from it.

## Decision

Spec-first. `openapi/yapilayer-api.yaml` is the single source of truth:

- Server-side interface stubs are generated from the spec (OpenAPI Generator `spring` generator); `platform-api` controllers implement those interfaces.
- The Java and TypeScript SDKs (Milestone 7) are generated from the same spec.
- Contract tests (`tests/contract`) validate the running implementation against the spec.
- API versioning is explicit in paths (`/api/v1/...`); breaking changes require `/api/v2`.

## Alternatives

- **Code-first** (springdoc generates spec from annotations): rejected — the spec becomes a build artifact rather than a design contract, drifts toward implementation details, and inverts the "API First" principle.

## Consequences

- API design happens in review-able YAML before implementation; SDKs and docs can never drift from the server contract because all are generated from one file.
- Requires generator tooling in the build; connector between spec and controllers is a generated-sources step in `platform-api`.

## Status

Accepted (2026-07-14)
