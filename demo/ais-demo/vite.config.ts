import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  // Ports per apisaurus docs/PORT-REGISTRY.md, ADR 0002 (project id 31, web;
  // platform proxy target is project id 30, api → 42130).
  server: {
    port: 42031,
    proxy: {
      "/api": "http://localhost:42130",
    },
  },
  preview: {
    port: 42031,
    proxy: {
      "/api": "http://localhost:42130",
    },
  },
});
