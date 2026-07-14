# Engineering Standards

## OpenBanking OSS Platform (Yapilayer)

**Document:** Engineering Standards  
**Version:** 0.1 Draft  
**Status:** Foundational Engineering Specification  

---

# 1. Purpose

This document defines the engineering standards, architecture principles, development practices, security requirements, testing expectations and operational standards for the OpenBanking OSS Platform.

The purpose is to ensure that the platform is built to enterprise financial-services standards while remaining accessible to open-source contributors.

---

# 2. Engineering Philosophy

The platform must be built according to the following principles:

- Security by default
- Documentation first
- API first
- Test first
- Automation first
- Cloud neutral
- Open standards
- Modular architecture
- Continuous improvement

Every engineering decision should consider:

- security impact
- maintainability
- scalability
- contributor experience
- future extensibility

---

# 3. Architecture Principles

## 3.1 Domain Driven Design

Use Domain Driven Design principles where appropriate.

The platform should clearly separate:

- business domains
- infrastructure concerns
- external integrations
- user interfaces

---

# 3.2 Separation of Concerns

The following layers should remain independent:

```
Presentation Layer

API Layer

Application Services

Domain Layer

Provider Integration Layer

Infrastructure Layer
```

---

# 3.3 Provider Independence

Bank-specific logic must never leak into the core platform.

All bank integrations must implement standard interfaces.

Example:

```
Core Platform

       |

Provider Interface

       |

Bank Connector Plugin
```

---

# 4. Recommended Technology Stack

Unless a documented decision changes this:

---

## Backend

Java 21

Spring Boot

Spring Security

Spring Data

---

## Frontend

React

TypeScript

Modern component framework

---

## Database

PostgreSQL

Database access must use abstraction layers.

---

## Containerisation

Docker

Docker Compose

---

## API

REST

OpenAPI Specification

JSON Schema

---

## Documentation

Mintlify

Markdown

OpenAPI generated documentation

---

## CI/CD

GitHub Actions

---

# 5. Repository Standards

The repository should contain:

```
/platform
/providers
/sdk
/demo
/docs
/mintlify
/openapi
/tests
/docker
/scripts
/infrastructure
```

---

# 6. Coding Standards

## General Rules

Code must be:

- readable
- documented
- tested
- maintainable

Prefer:

- simplicity over cleverness
- explicit behaviour over hidden behaviour
- small focused components

---

# 7. Java Standards

Follow:

- Java best practices
- Spring Boot conventions
- SOLID principles

Required:

- meaningful naming
- dependency injection
- immutable objects where practical
- clear exception handling

---

# 8. TypeScript Standards

Follow:

- strict TypeScript mode
- typed interfaces
- reusable components
- clear state management

Avoid:

- unnecessary complexity
- duplicated logic

---

# 9. API Standards

All APIs must:

- be documented
- have OpenAPI definitions
- include examples
- define errors
- support versioning

---

# 10. API Versioning

Use explicit versions.

Example:

```
/api/v1/accounts
/api/v1/payments
```

Breaking changes require a new major version.

---

# 11. Authentication Standards

Support:

- OAuth 2.0
- OpenID Connect
- PKCE
- FAPI requirements

Implement:

- secure token storage
- token expiry handling
- refresh handling
- revocation

---

# 12. Security Standards

Security is mandatory.

Follow:

- OWASP ASVS
- OWASP API Security Top 10
- NIST Cybersecurity Framework

---

# 13. Secrets Management

Never store secrets in:

- source code
- Git repositories
- documentation

Support:

Development:

- environment variables

Future:

- Hashicorp Vault
- AWS Secrets Manager
- Azure Key Vault

---

# 14. Certificates

Open Banking certificates must be handled securely.

Support:

- certificate storage abstraction
- certificate rotation
- expiry monitoring

---

# 15. Audit Logging

The platform must provide audit capability.

Capture:

- authentication events
- consent events
- payment events
- administrative actions

Audit records should be:

- timestamped
- immutable
- searchable

---

# 16. Threat Modelling

Before major features are implemented:

Create threat models covering:

- authentication
- payments
- APIs
- data storage
- external integrations

Document:

- threats
- risks
- mitigations

---

# 17. Testing Standards

Testing is mandatory.

---

## Unit Testing

Required for:

- business logic
- services
- utilities

---

## Integration Testing

Required for:

- APIs
- databases
- provider connectors

---

## Contract Testing

Required for:

- bank APIs
- OpenAPI contracts

---

## Security Testing

Include:

- dependency scanning
- vulnerability scanning
- authentication testing

---

## Performance Testing

Include:

- API latency
- concurrency
- throughput

---

# 18. CI/CD Standards

Every pull request should execute:

- build
- unit tests
- integration tests
- static analysis
- security scanning

---

# 19. GitHub Workflow Standards

Include:

```
.github/workflows

build.yml

test.yml

security.yml

release.yml
```

---

# 20. Dependency Management

Dependencies must be:

- actively maintained
- security reviewed
- version controlled

Regularly perform:

- dependency updates
- vulnerability scans

---

# 21. Documentation Standards

Documentation is part of the implementation.

Every feature requires:

- architecture documentation
- API documentation
- developer guide
- examples

---

# 22. Mintlify Documentation Structure

Recommended:

```
docs/

index.mdx

getting-started/

installation/

quickstart/

authentication/

ais/

pis/

providers/

security/

architecture/

deployment/

sdk/

contributing/

troubleshooting/
```

---

# 23. Architecture Decision Records

All significant decisions require ADRs.

Template:

```
# ADR Number

## Title

## Context

## Decision

## Alternatives

## Consequences

## Status
```

---

# 24. Observability Standards

The platform should support:

- logging
- metrics
- tracing

Future compatibility:

- OpenTelemetry
- Prometheus
- Grafana

---

# 25. Deployment Standards

Initial:

Docker Compose

Future:

Kubernetes

Requirements:

- stateless services where possible
- configuration externalisation
- health checks
- graceful shutdown

---

# 26. Database Standards

PostgreSQL reference implementation.

Requirements:

- migrations
- schema versioning
- backups
- indexes
- performance monitoring

---

# 27. Open Source Standards

The repository must include:

- CONTRIBUTING.md
- CODE_OF_CONDUCT.md
- SECURITY.md
- issue templates
- pull request templates

---

# 28. Release Management

Use semantic versioning:

```
MAJOR.MINOR.PATCH
```

Example:

```
1.2.3
```

---

# 29. AI Development Standards

AI-generated code must:

- follow architecture
- include tests
- include documentation
- be reviewed

AI should proactively identify:

- technical debt
- security issues
- improvements

---

# 30. Continuous Improvement

The platform should continuously improve through:

- community feedback
- security research
- developer feedback
- architectural reviews

---

# 31. Definition of Done

A feature is complete only when:

- code exists
- tests exist
- documentation exists
- security reviewed
- API documented
- CI passes
- architecture updated

---

# 32. Engineering Principle

The objective is not merely to make software that works.

The objective is to create software that developers, banks and enterprises can trust for many years.

---

End of Document.
