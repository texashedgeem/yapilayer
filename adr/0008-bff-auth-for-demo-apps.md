# ADR 0008

## Backend-for-Frontend pattern for demo application authentication

## Context

The AIS/PIS demo apps (PRODUCT_REQUIREMENTS §11) are React SPAs that must drive OAuth 2.0 / PKCE journeys through the platform to the (mock) bank. The question is where tokens live: in the browser, or server-side behind a thin backend.

## Decision

Backend-for-Frontend (BFF): each demo app's OAuth tokens are held server-side; the browser holds only a session cookie (HttpOnly, SameSite). The SPA talks to its BFF, which talks to the platform API.

## Alternatives

- **SPA-held tokens** (access/refresh tokens in browser memory or storage): rejected — browser-held tokens are the pattern OWASP consistently warns against (XSS exfiltration risk), and a security-first Open Banking reference implementation should model the safer pattern even at demo scale.

## Consequences

- Demo apps ship with a small server component (can be the platform itself serving the session endpoints, or a thin Node/Java sidecar — finalized in Milestone 6).
- Slightly more moving parts than a pure static SPA; accepted because the demos exist to teach the *right* pattern.

## Status

Accepted (2026-07-14) — concrete BFF topology finalized in Milestone 6.
