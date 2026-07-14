# providers/

The provider plugin framework.

- `provider-sdk/` — the contract every bank connector implements (BankConnector, AIS/PIS ports, registry). Core platform code depends only on this module.
- `mock-bank/` — the Phase 1 reference connector: a standalone bank simulator plus the connector that integrates against it.

To build a new connector, start from the guide in `docs/providers/` and use `mock-bank` as the worked example. See also ADR 0004.
