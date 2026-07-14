#!/usr/bin/env bash
# Regenerates the Java and TypeScript SDKs from openapi/yapilayer-api.yaml
# (spec-first, ADR 0005; tooling choices, ADR 0012).
# Generated code is committed; do not hand-edit sdk/java or sdk/typescript/src.
set -euo pipefail
cd "$(dirname "$0")/.."

SPEC=openapi/yapilayer-api.yaml

echo "Linting spec..."
npx --yes @stoplight/spectral-cli lint "$SPEC" --fail-severity error

echo "Generating TypeScript SDK (typescript-fetch) -> sdk/typescript/src ..."
rm -rf sdk/typescript/src
npx --yes @openapitools/openapi-generator-cli generate \
  -i "$SPEC" \
  -g typescript-fetch \
  -o sdk/typescript/src \
  --global-property apis,models,supportingFiles \
  --additional-properties=supportsES6=true,typescriptThreePlus=true

echo "Generating Java SDK (native HttpClient) -> sdk/java ..."
rm -rf sdk/java
npx --yes @openapitools/openapi-generator-cli generate \
  -i "$SPEC" \
  -g java \
  -o sdk/java \
  --library native \
  --additional-properties=groupId=io.yapilayer,artifactId=yapilayer-sdk-java,artifactVersion=0.1.0,useJakartaEe=true

echo "Building TypeScript SDK..."
npm run build -w @yapilayer/sdk

echo "Done. Review the diff and commit."
