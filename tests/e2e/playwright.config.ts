import { defineConfig } from "@playwright/test";

/**
 * Prerequisite: the platform stack must be running —
 *   cd docker && docker compose up -d --build
 * The demo dev servers are started automatically below.
 */
export default defineConfig({
  testDir: "./specs",
  timeout: 60_000,
  retries: 0,
  use: {
    headless: true,
  },
  webServer: [
    {
      command: "npm run dev -w @yapilayer/ais-demo",
      cwd: "../..",
      url: "http://localhost:42031",
      reuseExistingServer: true,
      timeout: 60_000,
    },
    {
      command: "npm run dev -w @yapilayer/pis-demo",
      cwd: "../..",
      url: "http://localhost:42032",
      reuseExistingServer: true,
      timeout: 60_000,
    },
  ],
});
