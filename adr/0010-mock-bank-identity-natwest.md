# ADR 0010

## Mock bank models NatWest Group's Open Banking implementation

## Context

DECISIONS.md D-5 deferred choosing which CMA9 bank/group the mock bank simulator behaviourally models. All CMA9 banks implement the same UK Open Banking Read/Write Data API standard, so the choice mainly determines (a) which real developer portal's journey quirks the simulator mirrors, and (b) which bank the first *real* Phase 2 connector targets — the mock's "personality" should be the bank we intend to integrate first, so the connector swap is minimal.

Research (2026-07-14): NatWest Group's "Bank of APIs" portal (bankofapis.com / developer.sandbox.natwest.com) offers self-serve registration, a sandbox with dynamic test data and a core banking simulator, downloadable Postman collections, and documentation aimed at first API call "in less than 5 minutes" — the most frictionless self-serve sandbox among CMA9 groups. NatWest Group also covers the NatWest and RBS brands with one integration.

## Decision

`mock-bank-simulator` models the NatWest Group flavour of the UK Open Banking standard: OB Read/Write Data API resource shapes, OAuth 2.0 authorisation-code journey with consent staging, and NatWest-style sandbox conventions (test data personas). The simulator remains generic OB-standard first — NatWest-specific quirks are only adopted where the standard leaves room.

Phase 2's first real connector therefore targets the NatWest Group sandbox.

## Alternatives

- **Lloyds Banking Group** — widest brand coverage (Lloyds, Bank of Scotland, Halifax) but a more gated developer onboarding journey.
- **HSBC / Barclays** — single-group portals with historically heavier registration friction for sandbox access.
- **Fully generic OB-standard mock with no bank personality** — rejected: a real connector swap would then hit untested journey quirks; anchoring the mock to the intended first real target keeps Phase 2 additive.

## Consequences

- Simulator endpoint shapes follow the OB Read/Write Data API as implemented by NatWest's sandbox; connector code written against it should port to the real NatWest sandbox with configuration-level changes.
- If NatWest's sandbox terms or friction change materially before Phase 2, revisit this ADR.

## Status

Accepted (2026-07-14)

Sources: [NatWest sandbox developer portal](https://developer.sandbox.natwest.com/), [Bank of APIs — NatWest Group](https://www.bankofapis.com/products/natwest-group-open-banking), [NatWest Group API portal page](https://www.natwestgroup.com/who-we-are/contact-us/open-banking-and-third-party-providers-api-portal-page.html)
