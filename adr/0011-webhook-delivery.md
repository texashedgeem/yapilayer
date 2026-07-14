# ADR 0011

## Webhook delivery: at-least-once, HMAC-SHA256 signatures, bounded in-memory retry

## Context

PRODUCT_REQUIREMENTS §7 requires webhooks for payment events with status changes, retry handling and verification. The design questions: delivery semantics, signature scheme, and how durable the retry mechanism must be for Phase 1.

## Decision

- **Event**: one type in Phase 1 — `payment.status.changed` — fired on every persisted status transition, carrying payment id, provider, status, amount/currency, reference and timestamp.
- **Signature**: `X-Yapilayer-Signature: sha256=<hex>` where the value is HMAC-SHA256 over the raw request body using the subscription's secret (min 16 chars, write-only via the API). This matches the prevailing industry pattern (GitHub/Stripe-style body HMAC).
- **Semantics**: at-least-once. Consumers must treat deliveries idempotently (the event carries the status, so replays are harmless).
- **Retry**: up to 3 attempts per delivery with backoff (0s / 1s / 5s) on non-2xx or transport failure, executed on a dedicated daemon scheduler; abandoned deliveries are logged at ERROR.
- **Durability**: in-memory only for Phase 1 — a platform restart loses queued retries. A durable outbox (DB-backed, redelivery on startup) is recorded in TECH_DEBT.md rather than built now.

## Alternatives

- **Durable outbox now**: rejected for Phase 1 — meaningful schema/worker complexity to protect events consumed only by demo apps against synthetic data; becomes necessary before production use.
- **Timestamp-in-signature (Stripe `t=` scheme)** for replay protection: deferred — the event body already carries `occurredAt`; full replay-window enforcement lands with the durable outbox.

## Consequences

- Receivers verify with one HMAC over the body — trivially implementable in any language; documented in the API spec.
- Missed events are recoverable by polling `GET /api/v1/payments/{id}`, which refreshes from the provider.

## Status

Accepted (2026-07-14)
