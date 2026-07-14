# Yapilayer

An open-source Open Banking platform — a secure, extensible, standards-compliant foundation for connecting to financial institutions, initially focused on UK Open Banking.

Yapilayer aims to be an open-source alternative to commercial Open Banking providers (Yapily, TrueLayer, Plaid, Tink, and others), so developers, fintechs, banks and enterprises can build financial products without depending exclusively on proprietary middleware.

## Status

**Phase 1 (reference implementation) in progress** — see [PROJECT_STATUS.md](./PROJECT_STATUS.md) for the current milestone and [ROADMAP.md](./ROADMAP.md) for the plan. End-to-end AIS and PIS journeys work today against the bundled mock bank.

## Quick start

Only Docker (with Compose) is required:

```bash
git clone https://github.com/texashedgeem/yapilayer.git
cd yapilayer/docker
docker compose up -d --build
```

Then open:

- **http://localhost:5173** — AIS demo: connect the mock bank, authorise, browse accounts, balances and transactions
- **http://localhost:5174** — PIS demo: create a payment, authorise it, watch the lifecycle complete
- **http://localhost:8080/api/v1/providers** — the platform API directly

For local development (Java 21, Node 20+):

```bash
./scripts/setup.sh   # checks prerequisites, builds JVM modules, installs TS workspaces
```

See [CONTRIBUTING.md](./CONTRIBUTING.md) for the full guide.

## Architecture at a glance

A modular Spring Boot core exposing stable AIS/PIS APIs, with all bank connectivity delivered through a plugin framework — adding a bank means adding a connector module, never changing the core. Full detail and diagrams in [ARCHITECTURE.md](./ARCHITECTURE.md).

```
Core Platform  →  Provider Interface (provider-sdk)  →  Bank Connector Plugins
```

## Project documents

- [PROJECT_CONSTITUTION.md](./PROJECT_CONSTITUTION.md) — vision, mission, principles, operating model
- [PRODUCT_REQUIREMENTS.md](./PRODUCT_REQUIREMENTS.md) — functional scope (AIS, PIS, consent, bank support)
- [ENGINEERING_STANDARDS.md](./ENGINEERING_STANDARDS.md) — architecture, stack, security, testing standards
- [ROADMAP.md](./ROADMAP.md) · [ARCHITECTURE.md](./ARCHITECTURE.md) · [DECISIONS.md](./DECISIONS.md) · [CHANGELOG.md](./CHANGELOG.md)
- [SECURITY.md](./SECURITY.md) — security policy and Phase 1 scope limitations

## License

Apache License 2.0 — see [`LICENSE`](./LICENSE).
