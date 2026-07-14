# AccountsApi

All URIs are relative to *http://localhost:8080/api/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getBalances**](AccountsApi.md#getbalances) | **GET** /consents/{consentId}/accounts/{accountId}/balances | Get balances for an account |
| [**getTransactions**](AccountsApi.md#gettransactions) | **GET** /consents/{consentId}/accounts/{accountId}/transactions | Get transactions for an account (paginated) |
| [**listAccounts**](AccountsApi.md#listaccounts) | **GET** /consents/{consentId}/accounts | List accounts available under an authorised consent |



## getBalances

> Array&lt;Balance&gt; getBalances(consentId, accountId)

Get balances for an account

### Example

```ts
import {
  Configuration,
  AccountsApi,
} from '';
import type { GetBalancesRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AccountsApi();

  const body = {
    // string
    consentId: 38400000-8cf0-11bd-b23e-10b96e4ef00d,
    // string
    accountId: accountId_example,
  } satisfies GetBalancesRequest;

  try {
    const data = await api.getBalances(body);
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **consentId** | `string` |  | [Defaults to `undefined`] |
| **accountId** | `string` |  | [Defaults to `undefined`] |

### Return type

[**Array&lt;Balance&gt;**](Balance.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `application/json`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Balances |  -  |
| **404** | Resource not found |  -  |
| **409** | Consent exists but is not in a state permitting data access |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


## getTransactions

> TransactionPage getTransactions(consentId, accountId, pageKey, pageSize)

Get transactions for an account (paginated)

### Example

```ts
import {
  Configuration,
  AccountsApi,
} from '';
import type { GetTransactionsRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AccountsApi();

  const body = {
    // string
    consentId: 38400000-8cf0-11bd-b23e-10b96e4ef00d,
    // string
    accountId: accountId_example,
    // string (optional)
    pageKey: pageKey_example,
    // number (optional)
    pageSize: 56,
  } satisfies GetTransactionsRequest;

  try {
    const data = await api.getTransactions(body);
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **consentId** | `string` |  | [Defaults to `undefined`] |
| **accountId** | `string` |  | [Defaults to `undefined`] |
| **pageKey** | `string` |  | [Optional] [Defaults to `undefined`] |
| **pageSize** | `number` |  | [Optional] [Defaults to `50`] |

### Return type

[**TransactionPage**](TransactionPage.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `application/json`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | One page of transactions |  -  |
| **404** | Resource not found |  -  |
| **409** | Consent exists but is not in a state permitting data access |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


## listAccounts

> Array&lt;Account&gt; listAccounts(consentId)

List accounts available under an authorised consent

### Example

```ts
import {
  Configuration,
  AccountsApi,
} from '';
import type { ListAccountsRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new AccountsApi();

  const body = {
    // string
    consentId: 38400000-8cf0-11bd-b23e-10b96e4ef00d,
  } satisfies ListAccountsRequest;

  try {
    const data = await api.listAccounts(body);
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **consentId** | `string` |  | [Defaults to `undefined`] |

### Return type

[**Array&lt;Account&gt;**](Account.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `application/json`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Accounts |  -  |
| **404** | Resource not found |  -  |
| **409** | Consent exists but is not in a state permitting data access |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)

