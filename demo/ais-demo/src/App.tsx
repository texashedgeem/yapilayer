import { useEffect, useState } from "react";
import { api, Account, Balance, Provider, Transaction } from "./api";

const ALL_PERMISSIONS = ["READ_ACCOUNTS", "READ_BALANCES", "READ_TRANSACTIONS"];

export function App() {
  // The bank redirects back to /connected?consentId=...&status=...
  const path = window.location.pathname;
  return (
    <main>
      <h1>Yapilayer · AIS Demo</h1>
      {path === "/connected" ? <Connected /> : <Connect />}
    </main>
  );
}

function Connect() {
  const [providers, setProviders] = useState<Provider[]>([]);
  const [permissions, setPermissions] = useState<string[]>(ALL_PERMISSIONS);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    api.listProviders().then(setProviders).catch((e) => setError(String(e)));
  }, []);

  const connect = async (providerId: string) => {
    setBusy(true);
    setError(null);
    try {
      const consent = await api.createConsent(
        providerId,
        permissions,
        `${window.location.origin}/connected`,
      );
      // Send the customer to the bank to authorise
      window.location.href = consent.authorisationUrl!;
    } catch (e) {
      setError(String(e));
      setBusy(false);
    }
  };

  return (
    <>
      <div className="card">
        <h2>1 · Choose what to share</h2>
        {ALL_PERMISSIONS.map((permission) => (
          <label key={permission}>
            <input
              type="checkbox"
              checked={permissions.includes(permission)}
              onChange={(e) =>
                setPermissions((current) =>
                  e.target.checked
                    ? [...current, permission]
                    : current.filter((p) => p !== permission),
                )
              }
            />{" "}
            {permission}
          </label>
        ))}
      </div>
      <div className="card">
        <h2>2 · Choose your bank</h2>
        {providers.length === 0 && <p>Loading providers…</p>}
        {providers
          .filter((p) => p.capabilities.includes("AIS"))
          .map((provider) => (
            <p key={provider.id}>
              <button
                disabled={busy || permissions.length === 0}
                onClick={() => connect(provider.id)}
                data-testid={`connect-${provider.id}`}
              >
                Connect {provider.name} ({provider.country})
              </button>
            </p>
          ))}
      </div>
      {error && <p className="error">{error}</p>}
    </>
  );
}

function Connected() {
  const params = new URLSearchParams(window.location.search);
  const consentId = params.get("consentId")!;
  const status = params.get("status")!;

  if (status !== "AUTHORISED") {
    return (
      <div className="card">
        <h2>
          Connection <span className={`status ${status}`}>{status}</span>
        </h2>
        <p>The bank reported that access was not granted.</p>
        <p>
          <a href="/">Start again</a>
        </p>
      </div>
    );
  }
  return <Accounts consentId={consentId} />;
}

function Accounts({ consentId }: { consentId: string }) {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [selected, setSelected] = useState<Account | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    api.listAccounts(consentId).then(setAccounts).catch((e) => setError(String(e)));
  }, [consentId]);

  return (
    <>
      <div className="card">
        <h2>
          Connected <span className="status AUTHORISED">AUTHORISED</span>
        </h2>
        <p>Consent: {consentId}</p>
      </div>
      {error && <p className="error">{error}</p>}
      {accounts.map((account) => (
        <AccountCard
          key={account.accountId}
          consentId={consentId}
          account={account}
          expanded={selected?.accountId === account.accountId}
          onToggle={() =>
            setSelected(selected?.accountId === account.accountId ? null : account)
          }
        />
      ))}
    </>
  );
}

function AccountCard({
  consentId,
  account,
  expanded,
  onToggle,
}: {
  consentId: string;
  account: Account;
  expanded: boolean;
  onToggle: () => void;
}) {
  const [balances, setBalances] = useState<Balance[]>([]);

  useEffect(() => {
    api.getBalances(consentId, account.accountId).then(setBalances).catch(() => {});
  }, [consentId, account.accountId]);

  const available = balances.find((b) => b.type === "AVAILABLE");

  return (
    <div className="card" data-testid={`account-${account.accountId}`}>
      <h3>
        {account.nickname} <small>({account.type})</small>
      </h3>
      <p>
        Available:{" "}
        <strong>
          {available ? `${available.amount} ${available.currency}` : "…"}
        </strong>
      </p>
      <button className="secondary" onClick={onToggle}>
        {expanded ? "Hide transactions" : "Show transactions"}
      </button>
      {expanded && <Transactions consentId={consentId} accountId={account.accountId} />}
    </div>
  );
}

function Transactions({
  consentId,
  accountId,
}: {
  consentId: string;
  accountId: string;
}) {
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [nextPageKey, setNextPageKey] = useState<string | null>(null);

  // Initial load replaces state so it stays idempotent under StrictMode's
  // double-invoked dev effects; only user-triggered "load more" appends.
  useEffect(() => {
    api.getTransactions(consentId, accountId).then((page) => {
      setTransactions(page.transactions);
      setNextPageKey(page.nextPageKey);
    });
  }, [consentId, accountId]);

  const loadMore = () =>
    api.getTransactions(consentId, accountId, nextPageKey!).then((page) => {
      setTransactions((current) => [...current, ...page.transactions]);
      setNextPageKey(page.nextPageKey);
    });

  return (
    <>
      <table>
        <thead>
          <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Amount</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map((txn) => (
            <tr key={txn.transactionId}>
              <td>{txn.bookingDate}</td>
              <td>{txn.merchantName ?? txn.reference ?? "—"}</td>
              <td
                className={
                  txn.amount.startsWith("-") ? "amount-negative" : "amount-positive"
                }
              >
                {txn.amount} {txn.currency}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {nextPageKey && (
        <p>
          <button className="secondary" onClick={loadMore}>
            Load more
          </button>
        </p>
      )}
    </>
  );
}
