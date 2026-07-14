-- Yapilayer schema, version 1: AIS consents and provider sessions.
-- tenant_id present from day one (ADR 0009) though Phase 1 is single-tenant.

CREATE TABLE consents (
    id                  UUID PRIMARY KEY,
    tenant_id           UUID        NOT NULL,
    provider_id         VARCHAR(64) NOT NULL,
    permissions         TEXT        NOT NULL,
    status              VARCHAR(32) NOT NULL,
    created_at          TIMESTAMPTZ NOT NULL,
    expires_at          TIMESTAMPTZ NOT NULL,
    provider_consent_id VARCHAR(128),
    oauth_state         VARCHAR(64) UNIQUE,
    client_redirect_uri TEXT
);

CREATE INDEX idx_consents_tenant ON consents (tenant_id);
CREATE INDEX idx_consents_provider ON consents (provider_id);

CREATE TABLE provider_sessions (
    consent_id          UUID PRIMARY KEY REFERENCES consents (id),
    provider_consent_id VARCHAR(128) NOT NULL,
    access_token        TEXT         NOT NULL,
    expires_at          TIMESTAMPTZ  NOT NULL,
    refresh_token       TEXT
);
