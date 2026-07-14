// Hand-written API client for the demo. Replaced by the generated
// @yapilayer/sdk in Milestone 7.

export interface Provider {
  id: string;
  name: string;
  country: string;
  capabilities: string[];
}

export interface Consent {
  consentId: string;
  providerId: string;
  status: string;
  permissions: string[];
  expiresAt: string;
  authorisationUrl: string | null;
}

export interface Account {
  accountId: string;
  nickname: string;
  currency: string;
  type: string;
  identifiers: { scheme: string; identification: string }[];
}

export interface Balance {
  accountId: string;
  type: "CURRENT" | "AVAILABLE";
  amount: string;
  currency: string;
  timestamp: string;
}

export interface Transaction {
  transactionId: string;
  amount: string;
  currency: string;
  status: string;
  bookingDate: string;
  reference: string | null;
  merchantName: string | null;
}

export interface TransactionPage {
  transactions: Transaction[];
  nextPageKey: string | null;
}

async function json<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const body = await response.text();
    throw new Error(`HTTP ${response.status}: ${body}`);
  }
  return response.json() as Promise<T>;
}

export const api = {
  listProviders: () => fetch("/api/v1/providers").then((r) => json<Provider[]>(r)),

  createConsent: (providerId: string, permissions: string[], redirectUri: string) =>
    fetch("/api/v1/consents", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ providerId, permissions, redirectUri }),
    }).then((r) => json<Consent>(r)),

  getConsent: (consentId: string) =>
    fetch(`/api/v1/consents/${consentId}`).then((r) => json<Consent>(r)),

  listAccounts: (consentId: string) =>
    fetch(`/api/v1/consents/${consentId}/accounts`).then((r) => json<Account[]>(r)),

  getBalances: (consentId: string, accountId: string) =>
    fetch(`/api/v1/consents/${consentId}/accounts/${accountId}/balances`).then((r) =>
      json<Balance[]>(r),
    ),

  getTransactions: (consentId: string, accountId: string, pageKey?: string) =>
    fetch(
      `/api/v1/consents/${consentId}/accounts/${accountId}/transactions?pageSize=5` +
        (pageKey ? `&pageKey=${encodeURIComponent(pageKey)}` : ""),
    ).then((r) => json<TransactionPage>(r)),
};
