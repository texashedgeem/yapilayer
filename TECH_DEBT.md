# Tech Debt

Deliberate shortcuts and revisit-later decisions. Nothing enters this file silently — each item cites its origin.

| Item | Origin | Revisit when |
|------|--------|--------------|
| Performance test suite deferred | D-4 (ENGINEERING_STANDARDS §17 softened for Phase 1) | Real bank connectivity exists; meaningful latency/throughput targets definable |
| Spring Data JPA abstraction-leakage risk (N+1, lazy loading) | ADR 0007 | Query performance issues observed, or persistence layer grows complex |
| Synchronous provider port signatures | ADR 0004 | Throughput requirements demand async; would be a new major provider-sdk version |
| Root-build `subprojects {}` convention block instead of convention plugins | Milestone 0 simplicity choice | Module count or per-module config divergence makes the root build hard to read |
| `platform-bootstrap` scans `io.yapilayer` broadly instead of explicit configuration imports | Milestone 0 | Module auto-configuration matures (candidate: per-module @AutoConfiguration) |
| Demo app BFF topology unresolved (platform-served vs sidecar) | ADR 0008 | Milestone 6 |
| Connector base-url serves double duty: API calls (container network) and browser-facing authorisation URLs — in compose, the authorise URL is `http://mock-bank:8090/...`, unresolvable from a host browser | Milestone 4 compose verification | Milestone 6/8: split internal vs public base URL config |
