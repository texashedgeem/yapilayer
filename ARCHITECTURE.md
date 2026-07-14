# Architecture

## Overview

Yapilayer is a modular Open Banking platform: a core that exposes stable AIS/PIS APIs, and a plugin framework through which bank connectors provide the actual connectivity. Bank-specific logic never enters the core (ENGINEERING_STANDARDS §3.3).

## Layered structure

Each layer is a separate Gradle module; dependency direction is enforced at compile time (ADR 0001).

```mermaid
flowchart TD
    subgraph Presentation
        AIS[ais-demo React/TS]
        PIS[pis-demo React/TS]
    end
    subgraph Platform["Yapilayer Platform (Spring Boot)"]
        API["platform-api\nREST /api/v1, OpenAPI-generated interfaces"]
        APP["platform-application\nAIS / PIS use cases"]
        DOM["platform-domain\npure domain model — no framework deps"]
        SEC["platform-security\nOAuth2 / OIDC / PKCE, tokens"]
        PER["platform-persistence\nSpring Data JPA + Flyway → PostgreSQL"]
        WH["platform-webhooks\ndispatch, retry, signatures"]
    end
    subgraph Providers["Provider plugin framework"]
        SDK["provider-sdk\nBankConnector, AisProviderPort, PisProviderPort, ProviderRegistry"]
        MOCK["mock-bank-connector"]
    end
    SIM["mock-bank-simulator\nstandalone fake ASPSP (own OAuth, consent UI, data)"]
    DB[(PostgreSQL)]

    AIS --> API
    PIS --> API
    API --> APP
    APP --> DOM
    APP --> SEC
    APP --> PER
    APP --> WH
    APP --> SDK
    SDK --> MOCK
    MOCK -->|HTTP, OAuth| SIM
    PER --> DB
```

## Provider plugin model

```mermaid
flowchart LR
    CORE["Core Platform\n(depends only on provider-sdk)"] --> IFACE["Provider Interface\nprovider-sdk"]
    IFACE --> C1["mock-bank-connector\n(Phase 1)"]
    IFACE -.-> C2["natwest-connector\n(Phase 2, illustrative)"]
    IFACE -.-> C3["community connectors\n(future)"]
```

Adding a bank = adding a connector module that implements the `provider-sdk` ports and declares its capabilities (ADR 0004). No core changes.

## Key invariants

- `platform-domain` has zero framework dependencies — the build fails if Spring leaks in.
- Core platform modules depend on `provider-sdk` only, never on a concrete connector.
- `mock-bank-simulator` is a separate process from the connector, so connector code crosses a real network/OAuth boundary (ADR 0003).
- The OpenAPI spec (`openapi/yapilayer-api.yaml`) is the source of truth; server interfaces and SDKs are generated from it (ADR 0005).

## Prior art & positioning

Per PRODUCT_REQUIREMENTS §17, commercial platforms are studied as benchmarks, not copied:

- **Yapily** — API-only aggregation (no UI layer); closest in spirit to Yapilayer's headless platform core. Benchmark for provider breadth and raw API design.
- **TrueLayer** — strong payments focus and developer experience; benchmark for the PIS journey and DX polish.
- **Plaid** — benchmark for onboarding flow and SDK ergonomics (Link-style drop-in is a possible future demo pattern).
- **Tink** — benchmark for European multi-market architecture, relevant to the Phase 3 PSD2 expansion.

Yapilayer's differentiator is not feature parity — it is that the platform is open source, self-hostable, and extensible via community connectors, with no commercial middleware dependency (Constitution §2).

## Status

Living document — updated at every milestone. Current as of Milestone 0 (genesis).
