# ADR 0001

## Monorepo with layer-per-module and plugin-per-connector boundaries

## Context

ENGINEERING_STANDARDS.md §3.2 requires six architectural layers (Presentation → API → Application Services → Domain → Provider Integration → Infrastructure) to "remain independent", and §3.3 requires that bank-specific logic never leaks into the core platform. PRODUCT_REQUIREMENTS.md §12 requires a single `git clone` → `docker compose up` developer experience. We needed to decide how source code is physically organised to enforce these constraints.

## Decision

A single monorepo containing a Gradle multi-module build:

- `platform/` holds one Gradle module per architectural concern (`platform-domain`, `platform-application`, `platform-api`, `platform-security`, `platform-persistence`, `platform-webhooks`, `platform-bootstrap`, `platform-observability`). Layer independence is enforced at compile time by module dependency direction, not by package-naming discipline.
- `platform-domain` has **zero framework dependencies** (no Spring). This is the cheapest possible enforcement of a clean domain core.
- `providers/` holds `provider-sdk` (the plugin contract) plus one module per connector. Core platform modules depend only on `provider-sdk`, never on individual connectors. Adding a bank means adding a module and one `settings.gradle.kts` line — no core changes.
- TypeScript packages (`demo/*`, `sdk/typescript`) live in the same repo as npm workspaces.
- Maven group id: `io.yapilayer`.

## Alternatives

- **Polyrepo** (separate repos for platform, providers, demos, docs): rejected — contradicts the single-clone developer experience, fragments contribution, and adds release-coordination overhead with no benefit at this stage.
- **Single flat Gradle module with package conventions**: rejected — layer independence would rely on human discipline rather than the compiler; provider leakage into core would be undetectable until review.

## Consequences

- Compile-time enforcement of the architecture: a change that makes `platform-domain` depend on Spring, or `platform-application` depend on a concrete connector, fails the build.
- More build files to maintain (one per module), accepted as the cost of enforcement.
- Future real bank connectors follow the exact pattern established by `mock-bank-connector`.

## Status

Accepted (2026-07-14)
