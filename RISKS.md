# Risks

| Risk | Impact | Likelihood | Mitigation |
|------|--------|------------|------------|
| Provider abstraction proves wrong when first *real* bank connector is built (Phase 2) | Rework of provider-sdk and connectors | Medium | Mock simulator models a real ASPSP boundary (separate process, real OAuth journey) rather than an in-process stub; provider contract shaped around published UK Open Banking Read/Write API standard |
| Cloud-neutrality is aspirational — only Docker Compose is validated in Phase 1 | Surprise coupling discovered at first K8s/cloud deploy | Medium | Stateless services, externalised config; planned smoke test running platform via bare `docker run` against external Postgres |
| Long unattended autonomous build runs accumulate breakage | Wasted iterations, inconsistent repo state | Medium | CI stood up at Milestone 1 (before meaningful code); milestone checkpoint rule: CI green before advancing |
| Mintlify hosting/account not yet provisioned | Docs site (Milestone 10) can be authored/previewed locally but not published | Low | Content is plain MDX in-repo; hosting decision needed only at publish time |
| FAPI-grade security deferred (mock-first) requires platform-security rework in Phase 2 | Delays first real connector | Low–Medium | platform-security isolates client-auth mechanics behind its own module boundary; SECURITY.md records the exact deferred surface |
| Single maintainer at genesis | Bus factor, review gaps | High (now) | Constitution's community vision; contribution framework from Milestone 0; AI-assisted review per ENGINEERING_STANDARDS §29 |
| Spring Boot / Gradle version drift during long build phase | Build breakage mid-run | Low | Versions pinned in gradle/libs.versions.toml and wrapper; Dependabot proposes controlled updates once CI exists |
