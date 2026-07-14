# Security

## Reporting a vulnerability

Please report suspected vulnerabilities privately via GitHub Security Advisories on this repository (Security → Report a vulnerability). Do not open public issues for security reports. You should receive an acknowledgement within 7 days.

## Security posture

Yapilayer follows OWASP ASVS, the OWASP API Security Top 10 and the NIST Cybersecurity Framework as its reference standards (ENGINEERING_STANDARDS §12). Key practices:

- No secrets in source, git history or documentation; development configuration via environment variables (§13).
- OAuth 2.0 / OIDC / PKCE for all authentication journeys; secure token storage, expiry, refresh and revocation handling.
- Audit logging of authentication, consent, payment and administrative events.
- Dependency and vulnerability scanning in CI (Dependabot, CodeQL).
- Demo applications use the Backend-for-Frontend pattern rather than browser-held tokens (ADR 0008).

## Phase 1 scope limitations (explicit, not silent)

Phase 1 runs exclusively against a **mock bank simulator** with synthetic data (ADR 0003). Consequently:

1. **FAPI conformance is not exercised.** Standard OAuth 2.0/OIDC/PKCE flows are implemented, but the FAPI profile (mutual TLS or `private_key_jwt` client authentication, signed request objects, eIDAS/QSEALC certificates) is deferred to Phase 2 with the first real bank connector. `platform-security` is designed so this lands as configuration/extension, not rework.
2. **No real customer data is processed.** A data-retention and GDPR posture must be defined before any real-bank connector ships — tracked in KNOWN_GAPS.md.
3. **Rate limiting is not implemented.** Tracked in KNOWN_GAPS.md.
4. **Threat models** (ENGINEERING_STANDARDS §16) are written per major feature as those features land: authentication and consent (Milestone 4), payments and webhooks (Milestone 5).

## Supported versions

Pre-1.0: only the latest commit on `main` is supported.
