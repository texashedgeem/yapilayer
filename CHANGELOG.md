# Changelog

All notable changes to this project are documented here. Format follows [Keep a Changelog](https://keepachangelog.com/); versioning follows [SemVer](https://semver.org/).

## [Unreleased]

### Added — Milestone 4 (AIS vertical slice), 2026-07-14
- `platform-application`: framework-free AisService (consent creation with anti-CSRF state, authorisation callback with code exchange, denied handling, account/balance/transaction access with consent-state and expiry enforcement) behind ConsentRepositoryPort/SessionStorePort
- `platform-persistence`: Consent + ProviderSession JPA entities and port adapters (domain stays JPA-free per ADR 0007), Flyway `V1__init.sql` (consents, provider_sessions, tenant_id per ADR 0009)
- `platform-api`: `/api/v1/providers`, `/api/v1/consents` (create/get), `/api/v1/callback`, `/api/v1/consents/{id}/accounts[/{id}/balances|/transactions]`, consistent error mapping (404/409/400/502)
- `platform-bootstrap`: datasource/Flyway/JPA wiring, AisService bean, env-based configuration
- OpenAPI spec: all implemented AIS paths and schemas (spec kept current per ADR 0005)
- Integration test: full AIS journey through the public API on Testcontainers PostgreSQL + live simulator, including pagination and unauthorised-access rejection
- Compose: platform now wired to postgres (datasource env), full stack re-verified healthy with API smoke test

### Fixed — Milestone 4
- `-parameters` compiler flag applied to all Java modules (Spring path-variable resolution failed in plain java-library modules)
- JPA repository/entity scanning made explicit (`@EnableJpaRepositories`/`@EntityScan` — `scanBasePackages` does not cover them)

### Added — Milestone 3 (Mock bank connector), 2026-07-14
- `mock-bank-simulator`: standalone fake ASPSP — OAuth 2.0 authorisation-code journey with consent decision page, OB Read/Write-style AIS endpoints (account-access-consents, accounts, balances, paginated transactions) and PIS endpoints (domestic-payment-consents, domestic-payments with auto-advancing status lifecycle), bearer-token enforcement, seeded two-account test persona
- `mock-bank-connector`: framework-free (JDK HTTP client + Jackson) implementation of provider-sdk against the simulator
- End-to-end acceptance test: real connector driven through complete AIS and PIS journeys against a live simulator (the D-1 reinterpreted DoD item)
- Platform registers the connector via ProviderRegistry at startup (configurable simulator URL)
- First `docker-compose.yml`: postgres + mock-bank + platform, all with healthchecks, verified healthy from one command
- ADR 0010: mock bank models NatWest Group's OB implementation (research-backed)

### Changed — Milestone 3
- `PisProviderPort.getPaymentStatus` now takes the ProviderSession — implementation surfaced that the M2 signature forced connectors to cache session state, violating the stateless-connector invariant (ADR 0004)

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
