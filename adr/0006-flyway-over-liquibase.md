# ADR 0006

## Flyway over Liquibase for database migrations

## Context

ENGINEERING_STANDARDS §26 requires migrations and schema versioning for the PostgreSQL reference implementation but names no tool.

## Decision

Flyway, with plain-SQL versioned migrations in `platform/platform-persistence/src/main/resources/db/migration` (`V1__init.sql`, ...).

## Alternatives

- **Liquibase**: rejected — its XML/YAML changelog abstraction buys cross-database portability that is not needed (PostgreSQL is the fixed reference database per ENGINEERING_STANDARDS §4/§26), at the cost of a less transparent migration history than plain SQL.

## Consequences

- Migrations are readable SQL, reviewable by any contributor with PostgreSQL knowledge.
- If a second reference database is ever adopted, this decision must be revisited.

## Status

Accepted (2026-07-14)
