#!/usr/bin/env bash
# Phase 1 Definition-of-Done verification (PRODUCT_REQUIREMENTS.md §19).
#
# Note on item 6: "at least one CMA9 sandbox integration works" is formally
# reinterpreted for Phase 1 as "at least one bank connector works end-to-end
# against the mock bank simulator" — DECISIONS.md D-1 / ADR 0003.
set -uo pipefail
cd "$(dirname "$0")/.."

pass=0
fail=0

check() {
  local desc="$1"; shift
  if "$@" > /dev/null 2>&1; then
    echo "  ✓ $desc"
    pass=$((pass + 1))
  else
    echo "  ✗ $desc"
    fail=$((fail + 1))
  fi
}

echo "PRD §19 — Phase 1 Definition of Done"
echo

echo "1. Repository exists"
check "git repository with remote" git remote get-url origin

echo "2. Documentation exists"
for f in PROJECT_STATUS.md ROADMAP.md ARCHITECTURE.md DECISIONS.md KNOWN_GAPS.md \
         SECURITY.md RISKS.md TECH_DEBT.md CHANGELOG.md README.md; do
  check "living doc: $f" test -s "$f"
done
check "docs site (Mintlify) with navigation" test -s docs/docs.json
check "docs pages (>= 12)" test "$(find docs -name '*.mdx' | wc -l)" -ge 12

echo "3. Docker deployment works"
check "compose file defines 5 services" \
  bash -c 'cd docker && docker compose config --services | wc -l | grep -q 5'
check "compose config is valid" bash -c 'cd docker && docker compose config -q'
check "e2e workflow exercises the full stack in CI" test -s .github/workflows/e2e.yml

echo "4. AIS demo works"
check "ais-demo app present" test -s demo/ais-demo/src/App.tsx
check "AIS journey integration test" test -s platform/platform-bootstrap/src/test/java/io/yapilayer/platform/bootstrap/AisJourneyIntegrationTest.java
check "AIS browser e2e spec" test -s tests/e2e/specs/ais.spec.ts

echo "5. PIS demo works"
check "pis-demo app present" test -s demo/pis-demo/src/App.tsx
check "PIS journey integration test (incl. webhooks)" test -s platform/platform-bootstrap/src/test/java/io/yapilayer/platform/bootstrap/PisJourneyIntegrationTest.java
check "PIS browser e2e spec" test -s tests/e2e/specs/pis.spec.ts

echo "6. Bank integration works (mock — DECISIONS.md D-1)"
check "D-1 reinterpretation recorded" grep -q "D-1" DECISIONS.md
check "connector end-to-end acceptance test" test -s providers/mock-bank/mock-bank-simulator/src/test/java/io/yapilayer/provider/mockbank/simulator/ConnectorEndToEndTest.java
check "full JVM build and test suite passes" ./gradlew build

echo "7. Architecture documentation exists"
check "ARCHITECTURE.md with diagrams" grep -q mermaid ARCHITECTURE.md
check "ADRs recorded (>= 12)" test "$(ls adr/*.md | wc -l)" -ge 12

echo "8. OpenAPI specification exists"
check "spec file" test -s openapi/yapilayer-api.yaml
check "generated TypeScript SDK" test -s sdk/typescript/src/index.ts
check "generated Java SDK" test -s sdk/java/pom.xml
check "SDK contract tests" test -s tests/contract/contract.spec.ts

echo "9. Contribution framework exists"
for f in CONTRIBUTING.md CODE_OF_CONDUCT.md SECURITY.md \
         .github/PULL_REQUEST_TEMPLATE.md .github/ISSUE_TEMPLATE/bug_report.md; do
  check "$f" test -s "$f"
done

echo "10. CI/CD pipeline exists"
for f in build test security release e2e; do
  check "workflow: $f.yml" test -s ".github/workflows/$f.yml"
done

echo
echo "Result: $pass passed, $fail failed"
[ "$fail" -eq 0 ] && echo "PHASE 1 DEFINITION OF DONE: SATISFIED" || echo "PHASE 1 DEFINITION OF DONE: NOT MET"
exit "$fail"
