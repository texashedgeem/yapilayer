# Product Requirements Document

## OpenBanking OSS Platform (Yapilayer)

**Document:** Product Requirements  
**Version:** 0.1 Draft  
**Status:** Foundational Product Specification  

---

# 1. Purpose

This document defines the functional requirements of the OpenBanking OSS Platform.

The objective is to create an open-source alternative to commercial Open Banking aggregation and payment initiation platforms.

The initial implementation will focus on UK Open Banking while maintaining an architecture capable of global expansion.

---

# 2. Product Vision

Create a complete Open Banking platform that enables developers to:

- connect to financial institutions
- authenticate customers securely
- retrieve account information
- initiate payments
- manage consent
- monitor transaction status
- build financial applications

without depending on proprietary Open Banking providers.

---

# 3. Phase 1 Objective

The first release should create a fully working reference implementation.

A developer should be able to:

1. Clone the repository.
2. Install prerequisites.
3. Start the platform locally.
4. Connect to sandbox environments.
5. Configure bank credentials.
6. Run AIS demonstrations.
7. Run PIS demonstrations.
8. Understand how to extend the platform.

---

# 4. Target Users

## Developers

Need:

- clear documentation
- simple setup
- working examples
- SDKs
- APIs

---

## Fintech Companies

Need:

- reliable APIs
- security
- scalability
- extensibility

---

## Enterprises

Need:

- compliance
- auditability
- deployment flexibility
- supportability

---

## Open Source Contributors

Need:

- clear architecture
- contribution guidelines
- easy local development

---

# 5. Functional Scope

The platform must support:

- Account Information Services (AIS)
- Payment Initiation Services (PIS)
- Consent management
- Authentication flows
- Bank connectivity
- Transaction retrieval
- Payment status tracking
- Webhooks
- Developer sandbox integration

---

# 6. Account Information Services (AIS)

## Required Capabilities

The AIS implementation must support:

## Authentication

Support:

- OAuth 2.0
- OpenID Connect
- PKCE
- customer authentication journeys

---

## Consent Management

Support:

- consent creation
- consent status
- consent expiry
- consent revocation

---

## Account Information

Support:

- account discovery
- account details
- account identifiers
- account ownership information

---

## Balances

Support:

- current balance
- available balance
- currency
- timestamps

---

## Transactions

Support:

- transaction history
- pagination
- dates
- amounts
- references
- merchant information where available

---

## Identity Information

Where supported:

- customer name
- address
- account holder information

---

# 7. Payment Initiation Services (PIS)

## Required Capabilities

The PIS implementation must support:

## Payment Creation

Support:

- single payments
- payment consent creation
- payment authorisation

---

## Payment Flow

Support:

1. Create payment request.
2. Redirect customer.
3. Customer authenticates.
4. Bank confirms payment.
5. Receive status update.
6. Complete payment lifecycle.

---

## Payment Status

Support:

- pending
- authorised
- rejected
- completed
- cancelled

---

## Webhooks

Support:

- payment events
- status changes
- retry handling
- verification

---

# 8. Open Banking Standards

The platform should support relevant UK Open Banking standards including:

- Account Information API
- Payment Initiation API
- Confirmation of Payee
- Variable Recurring Payments
- Future Open Banking extensions

---

# 9. Bank Support Strategy

## Initial Target

UK CMA9 banks.

Expected initial support:

- Barclays
- HSBC
- Lloyds Banking Group
- NatWest Group
- Santander UK
- Nationwide Building Society
- Royal Bank of Scotland
- Bank of Scotland
- Halifax

---

# 10. Bank Integration Requirements

For each supported bank provide:

## Documentation

Include:

- developer portal URL
- sandbox registration process
- application creation steps
- certificate requirements
- OAuth configuration
- API documentation links
- known limitations

---

## Configuration

Each bank integration must define:

- endpoints
- authentication configuration
- certificates
- supported APIs
- environment settings

---

## Testing

Each bank integration must include:

- sandbox tests
- example journeys
- known issues

---

# 11. Demo Applications

The first repository must include two complete demonstrations.

---

# AIS Demo Application

Purpose:

Demonstrate the complete account information journey.

Required features:

- login
- consent creation
- bank selection
- authentication redirect
- callback handling
- account display
- balances
- transactions

---

# PIS Demo Application

Purpose:

Demonstrate payment initiation.

Required features:

- create payment
- authenticate
- authorise
- receive status
- display completion

---

# 12. Developer Experience Requirements

The first experience should be:

## Clone

```
git clone repository
```

## Start

```
docker compose up
```

## Configure

Developer enters:

- client ID
- client secret
- certificates
- redirect URLs

## Run

Applications start successfully.

---

# 13. Configuration Requirements

Configuration should support:

- local development
- sandbox
- testing
- production preparation

Configuration must support:

- environment variables
- secrets management
- profiles

---

# 14. SDK Requirements

The platform should automatically generate SDKs.

Initial:

- Java
- TypeScript

Future:

- Python
- Go
- C#
- PHP

SDK generation should use OpenAPI specifications.

---

# 15. Documentation Requirements

The platform must provide:

## Getting Started

- installation
- quick start
- first integration

---

## Developer Guides

Examples:

- create AIS integration
- create PIS integration
- build custom provider

---

## API Documentation

Include:

- endpoints
- authentication
- request examples
- response examples
- errors

---

## Operations Documentation

Include:

- deployment
- monitoring
- troubleshooting
- security

---

# 16. Future Functional Roadmap

The architecture should allow future support for:

---

## Variable Recurring Payments (VRP)

Support:

- recurring payments
- sweeping
- mandates

---

## Confirmation of Payee

Support:

- account verification
- fraud reduction

---

## European PSD2

Support:

- European banks
- PSD2 APIs
- local requirements

---

## Global Expansion

Support:

- North America
- Asia Pacific
- Australia
- other Open Banking ecosystems

---

# 17. Competitive Requirements

The product should benchmark against:

## Yapily

Study:

- APIs
- documentation
- supported functionality

---

## TrueLayer

Study:

- developer experience
- payments
- authentication

---

## Plaid

Study:

- developer onboarding
- SDK approach

---

## Tink

Study:

- European architecture

---

# 18. Product Quality Goals

The platform should achieve:

## Reliability

Enterprise-grade availability.

---

## Security

Bank-grade security.

---

## Scalability

Support:

- multiple banks
- multiple countries
- high transaction volumes

---

## Extensibility

Allow:

- community contributions
- new providers
- new standards

---

# 19. Phase 1 Definition of Done

Phase 1 is complete when:

- repository exists
- documentation exists
- Docker deployment works
- AIS demo works
- PIS demo works
- at least one CMA9 sandbox integration works
- architecture documentation exists
- OpenAPI specification exists
- contribution framework exists
- CI/CD pipeline exists

---

# 20. Product Principle

The platform should not merely connect APIs.

It should provide developers with the easiest, safest and most extensible way to build Open Banking applications.

---

End of Document.
