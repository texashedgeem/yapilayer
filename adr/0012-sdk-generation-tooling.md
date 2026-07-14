# ADR 0012

## SDK generation: openapi-generator-cli, typescript-fetch + java-native, generated code committed

## Context

PRODUCT_REQUIREMENTS §14 requires auto-generated Java and TypeScript SDKs from the OpenAPI specification. Open questions: generator tooling, target library flavours, and whether generated code is committed or built on demand.

## Decision

- **Tooling**: `@openapitools/openapi-generator-cli` (pinned via `openapitools.json`), invoked by `scripts/generate-sdk.sh`, which also lints the spec with Spectral (`.spectral.yaml`, `spectral:oas` ruleset, fail on errors).
- **TypeScript**: `typescript-fetch` generator into `sdk/typescript/src` — zero runtime dependencies (native `fetch`), built with our own `tsconfig`/`package.json` as the `@yapilayer/sdk` workspace.
- **Java**: `java` generator with `--library native` (JDK 11+ HttpClient — no OkHttp/Jersey dependency tail) into `sdk/java` as a **standalone** Maven/Gradle project, deliberately outside the root Gradle build so generated build files can't destabilise platform CI. Verified compiling with its own wrapper.
- **Generated code is committed** (never hand-edited): SDK diffs become visible in review whenever the spec changes, and consumers can read the SDK source in-repo.
- **Contract validation**: `tests/contract` drives full AIS/PIS journeys using only the generated TS SDK against a running stack — empirical proof the spec matches the implementation, replacing a schema-diff tool.

## Alternatives

- **openapi-typescript + hand-rolled client**: lighter types-only output, but loses the "one generator, many languages" path needed for the Phase 2+ SDK matrix (Python/Go/C#/PHP).
- **Building SDKs only in CI (not committing)**: cleaner diffs, but consumers and reviewers lose visibility, and the demo/contract workspaces would need generation as a build step everywhere.

## Consequences

- `scripts/generate-sdk.sh` is the single regeneration entrypoint; CI can later assert freshness by regenerating and diffing (Milestone 9 candidate).
- The Java SDK's standalone build means it needs its own publish pipeline when SDK publishing starts (release.yml, Milestone 9+).

## Status

Accepted (2026-07-14)
