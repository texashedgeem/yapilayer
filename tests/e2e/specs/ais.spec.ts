import { expect, test } from "@playwright/test";

test("complete AIS journey: connect, authorise, view accounts and transactions", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/");

  // 1. Start the connection with the mock bank
  await page.getByTestId("connect-mock-bank").click();

  // 2. Bank consent page (simulator) — approve
  await expect(page.locator("h1")).toHaveText("Mock Bank");
  await page.getByRole("button", { name: "Approve" }).click();

  // 3. Back in the demo via the platform callback, accounts render
  await expect(page).toHaveURL(/\/connected\?consentId=.*status=AUTHORISED/);
  await expect(page.getByTestId("account-acc-001")).toBeVisible();
  await expect(page.getByTestId("account-acc-002")).toBeVisible();
  await expect(page.getByTestId("account-acc-001")).toContainText("1174.56 GBP");

  // 4. Transactions with pagination
  await page
    .getByTestId("account-acc-001")
    .getByRole("button", { name: "Show transactions" })
    .click();
  await expect(page.locator("tbody tr")).toHaveCount(5);
  await page.getByRole("button", { name: "Load more" }).click();
  await expect(page.locator("tbody tr")).toHaveCount(7);
});

test("denied authorisation is surfaced to the customer", async ({ page }) => {
  await page.goto("http://localhost:5173/");
  await page.getByTestId("connect-mock-bank").click();
  await page.getByRole("button", { name: "Deny" }).click();

  await expect(page).toHaveURL(/status=REJECTED/);
  await expect(page.locator("main")).toContainText("access was not granted");
});
