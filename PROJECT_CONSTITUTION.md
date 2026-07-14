# Project Constitution

## OpenBanking OSS Platform (Yapilayer)

**Document:** Project Constitution  
**Version:** 0.1 Draft  
**Status:** Foundational Governance Document  
**Purpose:** Defines the vision, mission, principles, operating model and strategic direction of the OpenBanking OSS Platform.

---

# 1. Introduction

This document defines the governing principles for the OpenBanking OSS Platform (also known as Yapilayer)

This is not a traditional software project specification.

This document establishes the long-term vision, engineering philosophy, decision-making framework and operating model for building a world-class open-source Open Banking ecosystem.

All future architecture, product decisions, implementation choices and community contributions should align with this constitution.

When decisions conflict, this document takes priority unless a deliberate architectural decision is recorded.

---

# 2. Mission Statement

Build the world's leading open-source Open Banking platform.

The platform should provide a secure, extensible, standards-compliant foundation that enables developers, fintech companies, banks and enterprises to build financial products without depending exclusively on commercial Open Banking providers.

The long-term ambition is to create the equivalent of:

- Linux for operating systems
- Kubernetes for container orchestration
- PostgreSQL for databases

but for Open Banking and financial connectivity.

---

# 3. Vision

The OpenBanking OSS Platform will become the preferred open-source foundation for:

- fintech developers
- banks
- financial institutions
- software companies
- enterprises
- system integrators
- academic institutions
- innovation communities

The platform should enable organisations to securely connect to financial institutions worldwide through a common, extensible architecture.

The initial implementation will focus on UK Open Banking.

The architecture must support future expansion into:

- Europe PSD2
- Open Finance
- global banking ecosystems
- emerging financial APIs

without requiring fundamental redesign.

---

# 4. Strategic Positioning

The platform is intended to compete directly with commercial Open Banking providers including:

- Yapily
- TrueLayer
- Plaid
- Tink
- GoCardless Bank Account Data
- Salt Edge
- Token.io
- Mastercard Open Banking
- Belvo
- Akoya

These platforms should be studied as benchmarks.

The objective is not to copy existing products.

The objective is to understand:

- their strengths
- their weaknesses
- their architecture
- their developer experience
- their documentation quality
- their commercial limitations

and create a superior open-source alternative.

---

# 5. Product Philosophy

The platform must follow these principles:

## 5.1 Developer First

The developer experience is the product.

A developer should be able to:

1. Discover the platform.
2. Read documentation.
3. Clone the repository.
4. Configure credentials.
5. Connect to a bank sandbox.
6. Run examples.
7. Build applications.

The experience should be comparable to world-class developer platforms such as Stripe.

---

## 5.2 Security First

Security is not a feature.

Security is the foundation.

The platform must be designed as though it will be independently audited by:

- financial regulators
- enterprise customers
- external security firms
- banking partners

---

## 5.3 Standards First

The platform should never create unnecessary proprietary standards.

Prefer:

- Open Banking Standards
- OAuth standards
- OpenID standards
- OpenAPI
- JSON Schema
- industry best practices

---

## 5.4 Open Source First

The project should encourage community contribution.

The architecture must make it easy for external developers to contribute:

- bank connectors
- documentation
- SDKs
- testing frameworks
- integrations
- improvements

---

## 5.5 Long-Term Thinking

Avoid short-term shortcuts that create long-term technical debt.

Optimise for:

- maintainability
- extensibility
- community adoption
- sustainability

---

# 6. Core Architectural Philosophy

The platform should be:

## Modular

Components should have clearly defined responsibilities.

## Extensible

New functionality should be added through extensions and plugins.

## Provider Neutral

The core platform must not become dependent on specific banks or providers.

## Cloud Neutral

The platform should run:

- locally
- Docker
- Kubernetes
- AWS
- Azure
- Google Cloud

without architectural changes.

## API First

All functionality should be accessible through documented APIs.

---

# 7. Plugin Philosophy

The platform must be designed around extensibility.

The core platform should provide frameworks for plugins including:

## Banking Providers

Examples:

- Barclays
- Lloyds
- HSBC
- NatWest
- Santander
- Nationwide

## Country Providers

Examples:

- United Kingdom
- Europe
- United States
- Australia

## Payment Providers

Examples:

- Faster Payments
- SEPA
- Open Banking payments

## Authentication Providers

Examples:

- OAuth providers
- Identity providers

## Additional Extensions

Examples:

- fraud detection
- risk engines
- notifications
- AI services
- analytics

Adding a new bank should ideally require implementing a provider module rather than modifying the core platform.

---

# 8. AI Engineering Model

AI tools such as Claude Code should be treated as active engineering contributors.

AI should assist with:

- architecture
- coding
- documentation
- testing
- research
- optimisation
- security reviews

AI output must always be reviewed against:

- security
- maintainability
- standards compliance
- architectural principles

---

# 9. Autonomous Working Model

AI agents working on this project should operate autonomously.

They should:

- research before implementing
- identify risks
- recommend improvements
- maintain documentation
- update project status
- record decisions

AI agents should not stop unnecessarily.

They should only request human input when:

- credentials are required
- a business decision is required
- multiple valid architectural paths exist
- requirements are genuinely ambiguous

---

# 10. Decision Management

All significant technical decisions must be recorded.

Maintain:

```
DECISIONS.md
```

Each decision should include:

- Date
- Decision
- Context
- Alternatives considered
- Reasoning
- Consequences

---

# 11. Living Documentation

The following documents must always remain current:

```
PROJECT_STATUS.md
ROADMAP.md
ARCHITECTURE.md
DECISIONS.md
KNOWN_GAPS.md
SECURITY.md
RISKS.md
TECH_DEBT.md
CHANGELOG.md
```

Documentation quality is considered part of the product.

---

# 12. Community Vision

The platform should eventually become a community-owned ecosystem.

Future governance should encourage:

- contributors
- maintainers
- security researchers
- financial institutions
- developers

Contribution should be simple and transparent.

---

# 13. Licensing Philosophy

The recommended licence is:

Apache License 2.0

Reasoning:

- widely adopted
- commercially friendly
- enterprise acceptable
- explicit patent protection
- encourages adoption

Any alternative licence must be justified.

---

# 14. Quality Standard

The platform should aim to achieve the quality expectations of:

- Stripe documentation
- Kubernetes architecture
- Spring Boot engineering standards
- PostgreSQL reliability
- Linux community practices

---

# 15. Definition of Success

The project is successful when:

A developer can discover the platform, understand it, deploy it, integrate with banks, build financial applications and contribute improvements without needing proprietary commercial middleware.

---

# 16. Final Principle

Every decision should answer:

> "Does this help create the world's best open-source Open Banking platform?"

If the answer is no, reconsider the decision.

---

End of Document.
