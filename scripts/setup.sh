#!/usr/bin/env bash
# One-shot developer environment check for Yapilayer.
set -euo pipefail

fail=0

check() {
  local name="$1" ok="$2" hint="$3"
  if [ "$ok" = "yes" ]; then
    echo "  ✓ $name"
  else
    echo "  ✗ $name — $hint"
    fail=1
  fi
}

echo "Checking prerequisites..."

# Any JVM 17+ can launch the Gradle wrapper; the build itself targets a JDK 21
# toolchain which Gradle auto-downloads if missing (foojay resolver).
java_ok=no
if command -v java >/dev/null 2>&1; then
  ver=$(java -version 2>&1 | head -1 | sed -E 's/.*"([0-9]+).*/\1/')
  [ "${ver:-0}" -ge 17 ] && java_ok=yes
fi
check "Java 17+ (to launch Gradle; JDK 21 toolchain auto-provisioned)" "$java_ok" "install a JDK, e.g. https://adoptium.net"

node_ok=no
if command -v node >/dev/null 2>&1; then
  nver=$(node --version | sed -E 's/v([0-9]+).*/\1/')
  [ "${nver:-0}" -ge 20 ] && node_ok=yes
fi
check "Node 20+" "$node_ok" "install Node 20+, e.g. via nvm"

docker_ok=no
command -v docker >/dev/null 2>&1 && docker compose version >/dev/null 2>&1 && docker_ok=yes
check "Docker + Compose" "$docker_ok" "install Docker Desktop or Docker Engine with the compose plugin"

if [ "$fail" -ne 0 ]; then
  echo "Fix the items above, then re-run scripts/setup.sh"
  exit 1
fi

echo "Building JVM modules..."
./gradlew build

echo "Installing TypeScript workspaces..."
npm install

echo "Done. Next: see CONTRIBUTING.md and ROADMAP.md."
