import { expect, test } from "@playwright/test";

test("complete PIS journey: create, authorise, track to completion", async ({ page }) => {
  await page.goto("http://localhost:5174/");

  // 1. Create the payment
  await page.getByTestId("reference").fill("E2E-001");
  await page.getByTestId("pay").click();

  // 2. Bank authorisation page — approve
  await expect(page.locator("h1")).toHaveText("Mock Bank");
  await page.getByRole("button", { name: "Approve" }).click();

  // 3. Back in the demo: payment authorised, polling drives it to COMPLETED
  await expect(page).toHaveURL(/\/paid\?paymentId=.*status=AUTHORISED/);
  await expect(page.getByTestId("status")).toHaveText("COMPLETED", { timeout: 15_000 });
  await expect(page.locator("main")).toContainText("E2E-001");
});
