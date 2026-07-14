# Yapilayer

An open-source Open Banking platform — a secure, extensible, standards-compliant foundation for connecting to financial institutions, initially focused on UK Open Banking.

Yapilayer aims to be an open-source alternative to commercial Open Banking providers (Yapily, TrueLayer, Plaid, Tink, and others), so developers, fintechs, banks and enterprises can build financial products without depending exclusively on proprietary middleware.

## Status

**Phase 1 (reference implementation) in progress** — see [PROJECT_STATUS.md](./PROJECT_STATUS.md) for the current milestone and [ROADMAP.md](./ROADMAP.md) for the plan. Not yet usable; the first end-to-end AIS/PIS journeys against the bundled mock bank arrive over the coming milestones.

## Quick start (for contributors)

Prerequisites: Java 21, Node 20+, Docker with Compose.

```bash
git clone https://github.com/texashedgeem/yapilayer.git
cd yapilayer
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
