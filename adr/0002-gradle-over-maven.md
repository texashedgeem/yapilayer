# ADR 0002

## Gradle (Kotlin DSL) over Maven

## Context

ENGINEERING_STANDARDS.md names Java 21 + Spring Boot but no build tool. The repo is a growing multi-module monorepo (11 JVM modules at genesis, more with every provider connector), so build-tool choice affects contributor experience and architectural enforcement for years.

## Decision

Gradle with the Kotlin DSL (`build.gradle.kts`), a version catalog (`gradle/libs.versions.toml`) for dependency pinning, and the committed Gradle wrapper pinned to a version verified compatible with the current Spring Boot release (8.14.3 with Spring Boot 3.5 at genesis).

## Alternatives

- **Maven**: mature, widely known by casual contributors, simpler mental model. Rejected because the plugin-per-provider architecture maps more naturally onto Gradle subprojects, incremental/parallel builds scale better across a module count that grows with every connector, and version catalogs give a single audited place for dependency management (an ENGINEERING_STANDARDS §20 requirement).

## Consequences

- Slightly steeper learning curve for contributors used to Maven; mitigated by keeping the root build minimal and documenting `./gradlew` usage in CONTRIBUTING.md.
- The wrapper jar is committed (`.gitignore` explicitly un-ignores it) so contributors need no local Gradle installation.

## Status

Accepted (2026-07-14)
