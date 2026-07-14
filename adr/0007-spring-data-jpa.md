# ADR 0007

## Spring Data JPA for the persistence abstraction

## Context

ENGINEERING_STANDARDS §4 says "Database access must use abstraction layers" and names Spring Data in the recommended stack, but "abstraction layers" could mean full ORM (JPA/Hibernate), a typed SQL DSL (jOOQ), or hand-rolled mappers over Spring JDBC.

## Decision

Spring Data JPA (Hibernate) in `platform-persistence`. JPA entities are mapped to/from the pure domain objects in `platform-domain` at the persistence boundary — domain classes are **not** annotated with JPA, preserving the framework-free domain core (ADR 0001).

## Alternatives

- **jOOQ**: excellent type-safe SQL, but commercial licensing nuances for some database versions and a smaller contributor familiarity pool.
- **Spring JDBC + manual mappers**: maximum transparency, but significant boilerplate for the entity count AIS/PIS requires.

## Consequences

- Fastest alignment with the Spring Boot conventions the standards mandate; largest contributor familiarity.
- ORM abstraction-leakage risks (N+1 queries, lazy-loading surprises) are recorded in TECH_DEBT.md for revisit if query performance becomes a concern.
- Entity↔domain mapping code is a deliberate, accepted cost of keeping `platform-domain` framework-free.

## Status

Accepted (2026-07-14)
