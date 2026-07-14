import { describe, expect, it } from "vitest";
import {
  AccountsApi,
  Configuration,
  ConsentsApi,
  PaymentsApi,
  ProvidersApi,
} from "@yapilayer/sdk";

/**
 * Prerequisite: the platform stack must be running —
 *   cd docker && docker compose up -d --build
 *
 * These tests use ONLY the generated SDK for platform calls, so any drift
 * between openapi/yapilayer-api.yaml and the implementation fails here.
 */
const config = new Configuration({ basePath: "http://localhost:8080/api/v1" });
const providers = new ProvidersApi(config);
const consents = new ConsentsApi(config);
const accounts = new AccountsApi(config);
const payments = new PaymentsApi(config);

/** Simulates the customer approving at the bank; returns the platform callback URL. */
async function approveAtBank(authorisationUrl: string): Promise<string> {
  const url = new URL(authorisationUrl);
  const form = new URLSearchParams({
    consent_id: url.searchParams.get("consent_id")!,
    redirect_uri: url.searchParams.get("redirect_uri")!,
    state: url.searchParams.get("state")!,
    decision: "approve",
  });
  const response = await fetch(`${url.origin}/oauth/authorize/decision`, {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: form,
    redirect: "manual",
  });
  expect(response.status).toBe(302);
  return response.headers.get("location")!;
}

/** Follows the platform callback without following its client redirect. */
async function completeCallback(callbackUrl: string): Promise<URL> {
  const response = await fetch(callbackUrl, { redirect: "manual" });
  expect(response.status).toBe(302);
  return new URL(response.headers.get("location")!);
}

describe("provider discovery", () => {
  it("lists the mock bank with AIS and PIS capabilities", async () => {
    const list = await providers.listProviders();
    const mockBank = list.find((p) => p.id === "mock-bank");
    expect(mockBank).toBeDefined();
    expect(mockBank!.capabilities).toEqual(
      expect.arrayContaining(["AIS", "PIS"]),
    );
  });
});

describe("AIS contract", () => {
  it("runs the full consent and account-data journey through the SDK", async () => {
    const consent = await consents.createConsent({
      createConsentRequest: {
        providerId: "mock-bank",
        permissions: ["READ_ACCOUNTS", "READ_BALANCES", "READ_TRANSACTIONS"],
        redirectUri: "http://client.example/done",
      },
    });
    expect(consent.status).toBe("AWAITING_AUTHORISATION");
    expect(consent.authorisationUrl).toBeTruthy();

    const clientRedirect = await completeCallback(
      await approveAtBank(consent.authorisationUrl!),
    );
    expect(clientRedirect.searchParams.get("status")).toBe("AUTHORISED");

    const fetched = await consents.getConsent({ consentId: consent.consentId });
    expect(fetched.status).toBe("AUTHORISED");

    const accountList = await accounts.listAccounts({
      consentId: consent.consentId,
    });
    expect(accountList).toHaveLength(2);

    const balances = await accounts.getBalances({
      consentId: consent.consentId,
      accountId: accountList[0].accountId,
    });
    expect(balances.map((b) => b.type).sort()).toEqual(["AVAILABLE", "CURRENT"]);

    const page1 = await accounts.getTransactions({
      consentId: consent.consentId,
      accountId: "acc-001",
      pageSize: 5,
    });
    expect(page1.transactions).toHaveLength(5);
    expect(page1.nextPageKey).toBeTruthy();

    const page2 = await accounts.getTransactions({
      consentId: consent.consentId,
      accountId: "acc-001",
      pageSize: 5,
      pageKey: page1.nextPageKey!,
    });
    expect(page2.transactions).toHaveLength(2);
    expect(page2.nextPageKey ?? null).toBeNull();
  });
});

describe("PIS contract", () => {
  it("runs the full payment journey through the SDK", async () => {
    const payment = await payments.createPayment({
      createPaymentRequest: {
        providerId: "mock-bank",
        amount: "12.34",
        currency: "GBP",
        creditor: {
          name: "Contract Test Ltd",
          scheme: "UK.OBIE.SortCodeAccountNumber",
          identification: "20000012345678",
        },
        reference: "CONTRACT-1",
        redirectUri: "http://client.example/paid",
      },
    });
    expect(payment.status).toBe("PENDING");

    const clientRedirect = await completeCallback(
      await approveAtBank(payment.authorisationUrl!),
    );
    expect(clientRedirect.searchParams.get("status")).toBe("AUTHORISED");

    // Poll through the SDK until terminal
    let current = await payments.getPayment({ paymentId: payment.paymentId });
    for (let i = 0; i < 5 && current.status !== "COMPLETED"; i++) {
      current = await payments.getPayment({ paymentId: payment.paymentId });
    }
    expect(current.status).toBe("COMPLETED");
  });
});
