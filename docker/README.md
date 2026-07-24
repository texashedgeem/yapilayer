# docker/

The full Yapilayer stack, one command:

```bash
cd docker
docker compose up -d --build
```

| Service    | URL                     | What it is                          |
|------------|-------------------------|-------------------------------------|
| ais-demo   | http://localhost:42031  | Account information demo app        |
| pis-demo   | http://localhost:42032  | Payment initiation demo app         |
| platform   | http://localhost:42130  | Yapilayer API (`/api/v1`, actuator) |
| mock-bank  | http://localhost:42730  | Simulated ASPSP (OAuth + OB API)    |
| postgres   | localhost:42230         | Platform database                   |

Ports per the cross-portfolio registry (apisaurus `docs/PORT-REGISTRY.md`,
ADR 0002) — project id 30 (platform/mock-bank/postgres, via service-type
digit) plus 31/32 for the two demo frontends.

Configuration: copy `.env.example` to `.env` to override defaults. All
platform settings are environment variables (see `docker-compose.yml`).

Images build inside containers (multi-stage), so no local Java/Node
toolchain is required — first build takes a few minutes.
