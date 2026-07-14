# Contributing to Yapilayer

Thank you for your interest! Yapilayer aims to be a community-owned open-source Open Banking platform — contributions of connectors, code, documentation, tests and security research are all welcome.

## Getting started

Prerequisites: **Java 21**, **Node 20+**, **Docker** (with Compose).

```bash
git clone https://github.com/texashedgeem/yapilayer.git
cd yapilayer
./gradlew build     # builds all JVM modules and runs tests (no local Gradle install needed)
npm install         # sets up the TypeScript workspaces
```

## Repository layout

- `platform/` — the core platform, one Gradle module per architectural layer (see [ARCHITECTURE.md](ARCHITECTURE.md))
- `providers/` — the provider plugin framework; `provider-sdk` is the contract, everything else is a connector
- `demo/` — AIS and PIS demo applications (React/TypeScript)
- `openapi/` — the API specification (source of truth — see ADR 0005)
- `docs/` — documentation site source
- `adr/` — Architecture Decision Records

## Ground rules

1. **Read the constitution.** [PROJECT_CONSTITUTION.md](PROJECT_CONSTITUTION.md) governs; [ENGINEERING_STANDARDS.md](ENGINEERING_STANDARDS.md) defines the bar.
2. **Provider logic stays out of the core.** Bank-specific code belongs in a connector module implementing `provider-sdk` interfaces — PRs that leak provider specifics into `platform/*` will be asked to restructure.
3. **`platform-domain` stays framework-free.** No Spring (or any framework) dependencies there.
4. **Definition of done** (ENGINEERING_STANDARDS §31): code + tests + documentation + API docs updated + CI green.
5. **Significant decisions need an ADR** — copy the template style in `adr/` and reference it from DECISIONS.md.
6. **Never commit secrets** — see [SECURITY.md](SECURITY.md).

## Workflow

1. Fork and create a feature branch.
2. Make your change with tests and docs.
3. Ensure `./gradlew build` passes locally.
4. Open a PR using the template. CI must pass before review.

## Adding a bank connector

The worked example is `providers/mock-bank/`. In short: create a module under `providers/`, implement the `provider-sdk` ports for the capabilities your bank supports, declare the module in `settings.gradle.kts`, and add sandbox setup documentation. A full guide lives in the docs site (`docs/providers/`).

## Questions

Open a GitHub Discussion or issue. Be excellent to each other — see [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md).
