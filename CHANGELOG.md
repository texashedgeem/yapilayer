# Changelog

All notable changes to this project are documented here. Format follows [Keep a Changelog](https://keepachangelog.com/); versioning follows [SemVer](https://semver.org/).

## [Unreleased]

### Added — Milestones 11 & 12 (Contribution verification + DoD pass), 2026-07-14
- CONTRIBUTING.md and PR template updated to the real workflow: spec-first API changes, Spotless, all four test layers, full CI gate list
- `scripts/verify-phase1-dod.sh`: scripted verification of every PRD §19 item — **41/41 checks pass; Phase 1 Definition of Done satisfied** (item 6 per the recorded D-1 reinterpretation)

### Added — Milestone 10 (Documentation site), 2026-07-14
- Full Mintlify documentation (13 pages, 4 nav groups): introduction, installation, API quickstart, authentication model, AIS guide, PIS guide with webhook verification code, "build a provider" guide, SDK usage (TS + Java), architecture, security posture with the explicit Phase 1 boundary, deployment/configuration reference, contributing, troubleshooting
- Validated with the Mintlify CLI (no broken links); content grounded in the implemented `/api/v1` surface

### Added — Milestone 9 (CI/CD completion), 2026-07-14
- Spotless (google-java-format AOSP + ktlint for build scripts) enforced in every build; one-time repo-wide reformat applied
- build workflow: OpenAPI lint job + SDK-freshness check (regenerates the TS SDK and fails on drift)
- security workflow: Trivy filesystem scan (vulnerabilities + misconfigurations, HIGH/CRITICAL fail)
- release workflow completed: tag-triggered GitHub Release with boot jars and the changelog's Unreleased section as release notes (SDK registry publishing deferred — needs credentials)
- e2e workflow: full compose stack build + SDK contract tests + Playwright browser suite on every main push

### Added — Milestone 8 (Full-stack Docker Compose), 2026-07-14
- Demo apps containerized: multi-stage node build → nginx serving the built SPA with `/api` proxied to the platform (SPA-fallback routing for `/connected` and `/paid`)
- Complete five-service stack (postgres, mock-bank, platform, ais-demo, pis-demo) healthy from one `docker compose up -d --build` — no local toolchain needed
- `docker/.env.example`; docker/README service table; root README quickstart rewritten around the single-command experience
- Playwright e2e suite verified green against the fully containerized stack (nginx-served demos)

### Fixed — Milestone 8
- Demo healthchecks use 127.0.0.1 — in-container `localhost` resolves to `::1` while nginx listens IPv4-only

### Added — Milestone 7 (OpenAPI finalization + SDKs), 2026-07-14
- `@yapilayer/sdk` (TypeScript, typescript-fetch, zero runtime deps) generated into `sdk/typescript/src` and building to typed `dist/`
- Java SDK (`io.yapilayer:yapilayer-sdk-java`, JDK-native HttpClient) generated as a standalone project in `sdk/java`, compile-verified
- `scripts/generate-sdk.sh`: single regeneration entrypoint — Spectral spec lint (errors fail), both generators, TS build
- Contract tests (`tests/contract`): full AIS and PIS journeys driven exclusively through the generated TS SDK against the running stack — 3 specs green (ADR 0012)
- Spectral ruleset (`.spectral.yaml`); spec lints with 0 errors

### Added — Milestone 6 (Demo applications), 2026-07-14
- `ais-demo` (React/TS/Vite, port 5173): provider discovery, permission selection, bank authorisation journey, accounts with balances, paginated transactions; denied-consent handling
- `pis-demo` (React/TS/Vite, port 5174): payment form, bank authorisation, live status polling to completion with lifecycle timeline
- Playwright e2e suite (`tests/e2e`): three specs covering the complete AIS journey (incl. pagination), denied authorisation, and the complete PIS journey — all passing against the containerized stack
- ADR 0008 resolved: platform-as-BFF — demos are static SPAs; no tokens ever reach the browser because provider sessions are platform-side by design
- Connector split into API base URL vs public (browser-facing) base URL — compose authorisation journeys now work from a host browser (closes the M4 TECH_DEBT item)
- CI: CodeQL javascript-typescript re-enabled (first real TS source); build workflow now builds all TS workspaces

### Added — Milestone 5 (PIS vertical slice), 2026-07-14
- `platform-application`: framework-free PisService — payment creation with provider payment consent, authorisation callback that submits the payment, status refresh from the provider with domain state-machine enforcement, webhook publication on every status change
- `platform-webhooks`: WebhookDispatcher — at-least-once delivery, `X-Yapilayer-Signature` HMAC-SHA256 over raw body, 3 attempts with backoff (ADR 0011); subscription model with write-only secrets
- `platform-persistence`: Payment/PaymentSession/WebhookSubscription entities + adapters, Flyway `V2__payments_webhooks.sql`
- `platform-api`: `/api/v1/payments` (create/get), `/api/v1/payments/callback` (separate from the AIS callback), `/api/v1/webhooks` (create/list)
- OpenAPI spec: payments and webhooks paths + schemas
- Integration test: full PIS journey through the public API — create → authorise → callback-submits → poll to COMPLETED — with a local webhook receiver asserting both events arrive correctly HMAC-signed
- ADR 0011 recorded; durable-outbox deferral tracked in TECH_DEBT.md

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
