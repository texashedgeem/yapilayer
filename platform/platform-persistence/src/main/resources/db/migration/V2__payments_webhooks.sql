-- Yapilayer schema, version 2: payments, payment sessions, webhook subscriptions.

CREATE TABLE payments (
    id                          UUID PRIMARY KEY,
    tenant_id                   UUID           NOT NULL,
    provider_id                 VARCHAR(64)    NOT NULL,
    amount                      NUMERIC(19, 4) NOT NULL,
    currency                    VARCHAR(3)     NOT NULL,
    creditor_name               VARCHAR(255)   NOT NULL,
    creditor_scheme             VARCHAR(64)    NOT NULL,
    creditor_identification    VARCHAR(64)    NOT NULL,
    reference                   VARCHAR(255)   NOT NULL,
    status                      VARCHAR(32)    NOT NULL,
    created_at                  TIMESTAMPTZ    NOT NULL,
    updated_at                  TIMESTAMPTZ    NOT NULL,
    provider_payment_id         VARCHAR(128),
    provider_payment_consent_id VARCHAR(128),
    oauth_state                 VARCHAR(64) UNIQUE,
    client_redirect_uri         TEXT
);

CREATE INDEX idx_payments_tenant ON payments (tenant_id);
CREATE INDEX idx_payments_status ON payments (status);

CREATE TABLE payment_sessions (
    payment_id          UUID PRIMARY KEY REFERENCES payments (id),
    provider_consent_id VARCHAR(128) NOT NULL,
    access_token        TEXT         NOT NULL,
    expires_at          TIMESTAMPTZ  NOT NULL,
    refresh_token       TEXT
);

CREATE TABLE webhook_subscriptions (
    id     UUID PRIMARY KEY,
    url    TEXT NOT NULL,
    secret TEXT NOT NULL
);
