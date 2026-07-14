import { useEffect, useRef, useState } from "react";

interface Payment {
  paymentId: string;
  providerId: string;
  status: string;
  amount: string;
  currency: string;
  creditorName: string;
  reference: string;
  authorisationUrl: string | null;
}

async function json<T>(response: Response): Promise<T> {
  if (!response.ok) {
    throw new Error(`HTTP ${response.status}: ${await response.text()}`);
  }
  return response.json() as Promise<T>;
}

export function App() {
  const path = window.location.pathname;
  return (
    <main>
      <h1>Yapilayer · PIS Demo</h1>
      {path === "/paid" ? <PaymentResult /> : <CreatePayment />}
    </main>
  );
}

function CreatePayment() {
  const [amount, setAmount] = useState("25.00");
  const [creditorName, setCreditorName] = useState("Acme Ltd");
  const [reference, setReference] = useState("INV-001");
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const pay = async () => {
    setBusy(true);
    setError(null);
    try {
      const payment = await json<Payment>(
        await fetch("/api/v1/payments", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            providerId: "mock-bank",
            amount,
            currency: "GBP",
            creditor: {
              name: creditorName,
              scheme: "UK.OBIE.SortCodeAccountNumber",
              identification: "20000012345678",
            },
            reference,
            redirectUri: `${window.location.origin}/paid`,
          }),
        }),
      );
      window.location.href = payment.authorisationUrl!;
    } catch (e) {
      setError(String(e));
      setBusy(false);
    }
  };

  return (
    <div className="card">
      <h2>Make a payment</h2>
      <label>
        Amount (GBP){" "}
        <input value={amount} onChange={(e) => setAmount(e.target.value)} data-testid="amount" />
      </label>
      <label>
        Payee{" "}
        <input
          value={creditorName}
          onChange={(e) => setCreditorName(e.target.value)}
          data-testid="payee"
        />
      </label>
      <label>
        Reference{" "}
        <input
          value={reference}
          onChange={(e) => setReference(e.target.value)}
          data-testid="reference"
        />
      </label>
      <p>
        <button onClick={pay} disabled={busy} data-testid="pay">
          Pay with Mock Bank
        </button>
      </p>
      {error && <p className="error">{error}</p>}
    </div>
  );
}

function PaymentResult() {
  const params = new URLSearchParams(window.location.search);
  const paymentId = params.get("paymentId")!;
  const initialStatus = params.get("status")!;
  const [payment, setPayment] = useState<Payment | null>(null);
  const [history, setHistory] = useState<string[]>([initialStatus]);
  const timer = useRef<number>();

  useEffect(() => {
    const poll = async () => {
      const current = await json<Payment>(await fetch(`/api/v1/payments/${paymentId}`));
      setPayment(current);
      setHistory((h) => (h[h.length - 1] === current.status ? h : [...h, current.status]));
      if (["COMPLETED", "REJECTED", "CANCELLED"].includes(current.status)) {
        window.clearInterval(timer.current);
      }
    };
    poll();
    timer.current = window.setInterval(poll, 1500);
    return () => window.clearInterval(timer.current);
  }, [paymentId]);

  return (
    <div className="card">
      <h2>
        Payment{" "}
        <span className={`status ${payment?.status ?? initialStatus}`} data-testid="status">
          {payment?.status ?? initialStatus}
        </span>
      </h2>
      {payment && (
        <p>
          {payment.amount} {payment.currency} to {payment.creditorName} ·{" "}
          {payment.reference}
        </p>
      )}
      <p>Lifecycle: {history.join(" → ")}</p>
      <p>
        <a href="/">Make another payment</a>
      </p>
    </div>
  );
}
