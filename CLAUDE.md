# yapilayer: CLAUDE.md

## Secrets inventory rule (applies to every change)

`secrets.inventory.json` (repo root) is the names-only record the Qeetoto Hub reads to prompt monthly rotation. Whenever you add, rename, remove or rotate an
environment variable, key, token or login, update it in the same change (see `.claude/skills/maintain-secrets-inventory/SKILL.md`; check with `node scripts/check-secrets-inventory.mjs`).
Never put values in it. Rotation dates are logged in the Hub (D-231); `last_rotated` here is an optional seed.
