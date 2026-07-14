# Changelog

All notable changes to this project are documented here. Format follows [Keep a Changelog](https://keepachangelog.com/); versioning follows [SemVer](https://semver.org/).

## [Unreleased]

### Added — Milestone 2 (Domain model & provider contract), 2026-07-14
- `platform-domain`: Consent aggregate (UK OB-style lifecycle with expiry), Payment aggregate (explicit status state machine), Account/Balance/Transaction, Money/TenantId/ProviderId value objects — zero framework dependencies
- `provider-sdk`: BankConnector root with capability discovery, AisProviderPort (consent → authorise → exchange → accounts/balances/paginated transactions), PisProviderPort (payment consent → authorise → submit → status), ProviderRegistry, ProviderException
- ADR 0009: multi-tenancy seam — every customer-scoped aggregate carries TenantId from day one, isolation enforcement deferred
- 18 domain/contract unit tests

### Added — Milestone 1 (CI/CD skeleton), 2026-07-14
- GitHub Actions workflows: build.yml (JVM + TypeScript), test.yml, security.yml (CodeQL for Java, dependency review on PRs, weekly schedule), release.yml (tag-triggered artifact build, completed fully in Milestone 9)
- Dependabot: weekly Gradle, npm and GitHub Actions updates, minor/patch only — major upgrades are deliberate recorded decisions

### Fixed — Milestone 1
- CodeQL scoped to Java until first TypeScript source lands (Milestone 6); JS/TS analysis fails on a manifests-only repo
- assertj 3.27.3 → 3.27.7 (patch)

### Added — Milestone 0 (Genesis & scaffolding), 2026-07-14
- Gradle multi-module skeleton (Kotlin DSL, version catalog, wrapper 8.14.3): 8 platform modules, provider-sdk, mock-bank simulator + connector; first `./gradlew build` green on Java 21
- npm workspaces for demo apps and TypeScript SDK
- Genesis ADRs 0001–0008 (monorepo boundaries, Gradle, mock-bank-first, provider contract, spec-first OpenAPI, Flyway, Spring Data JPA, BFF demos)
- Living documents: PROJECT_STATUS, ROADMAP, ARCHITECTURE (with diagrams), DECISIONS (incl. contradiction resolutions D-1…D-5), KNOWN_GAPS, SECURITY, RISKS, TECH_DEBT
- Contribution framework: CONTRIBUTING, CODE_OF_CONDUCT, issue/PR templates
- OpenAPI spec skeleton; Mintlify docs skeleton; scripts/setup.sh

### Added — pre-Milestone 0, 2026-07-14
- Repository created with foundational documents (PROJECT_CONSTITUTION, PRODUCT_REQUIREMENTS, ENGINEERING_STANDARDS), Apache 2.0 licence, README
