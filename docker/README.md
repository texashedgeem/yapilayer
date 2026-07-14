# docker/

The full Yapilayer stack, one command:

```bash
cd docker
docker compose up -d --build
```

| Service    | URL                     | What it is                          |
|------------|-------------------------|-------------------------------------|
| ais-demo   | http://localhost:5173   | Account information demo app        |
| pis-demo   | http://localhost:5174   | Payment initiation demo app         |
| platform   | http://localhost:8080   | Yapilayer API (`/api/v1`, actuator) |
| mock-bank  | http://localhost:8090   | Simulated ASPSP (OAuth + OB API)    |
| postgres   | localhost:5432          | Platform database                   |

Configuration: copy `.env.example` to `.env` to override defaults. All
platform settings are environment variables (see `docker-compose.yml`).

Images build inside containers (multi-stage), so no local Java/Node
toolchain is required — first build takes a few minutes.
