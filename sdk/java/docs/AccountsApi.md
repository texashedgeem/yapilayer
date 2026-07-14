# AccountsApi

All URIs are relative to *http://localhost:8080/api/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getBalances**](AccountsApi.md#getBalances) | **GET** /consents/{consentId}/accounts/{accountId}/balances | Get balances for an account |
| [**getBalancesWithHttpInfo**](AccountsApi.md#getBalancesWithHttpInfo) | **GET** /consents/{consentId}/accounts/{accountId}/balances | Get balances for an account |
| [**getTransactions**](AccountsApi.md#getTransactions) | **GET** /consents/{consentId}/accounts/{accountId}/transactions | Get transactions for an account (paginated) |
| [**getTransactionsWithHttpInfo**](AccountsApi.md#getTransactionsWithHttpInfo) | **GET** /consents/{consentId}/accounts/{accountId}/transactions | Get transactions for an account (paginated) |
| [**listAccounts**](AccountsApi.md#listAccounts) | **GET** /consents/{consentId}/accounts | List accounts available under an authorised consent |
| [**listAccountsWithHttpInfo**](AccountsApi.md#listAccountsWithHttpInfo) | **GET** /consents/{consentId}/accounts | List accounts available under an authorised consent |



## getBalances

> List<Balance> getBalances(consentId, accountId)

Get balances for an account

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AccountsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        AccountsApi apiInstance = new AccountsApi(defaultClient);
        UUID consentId = UUID.randomUUID(); // UUID | 
        String accountId = "accountId_example"; // String | 
        try {
            List<Balance> result = apiInstance.getBalances(consentId, accountId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AccountsApi#getBalances");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **consentId** | **UUID**|  | |
| **accountId** | **String**|  | |

### Return type

[**List&lt;Balance&gt;**](Balance.md)


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Balances |  -  |
| **404** | Resource not found |  -  |
| **409** | Consent exists but is not in a state permitting data access |  -  |

## getBalancesWithHttpInfo

> ApiResponse<List<Balance>> getBalancesWithHttpInfo(consentId, accountId)

Get balances for an account

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AccountsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        AccountsApi apiInstance = new AccountsApi(defaultClient);
        UUID consentId = UUID.randomUUID(); // UUID | 
        String accountId = "accountId_example"; // String | 
        try {
            ApiResponse<List<Balance>> response = apiInstance.getBalancesWithHttpInfo(consentId, accountId);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
            System.out.println("Response body: " + response.getData());
        } catch (ApiException e) {
            System.err.println("Exception when calling AccountsApi#getBalances");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Response headers: " + e.getResponseHeaders());
            System.err.println("Reason: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **consentId** | **UUID**|  | |
| **accountId** | **String**|  | |

### Return type

ApiResponse<[**List&lt;Balance&gt;**](Balance.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Balances |  -  |
| **404** | Resource not found |  -  |
| **409** | Consent exists but is not in a state permitting data access |  -  |


## getTransactions

> TransactionPage getTransactions(consentId, accountId, pageKey, pageSize)

Get transactions for an account (paginated)

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AccountsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        AccountsApi apiInstance = new AccountsApi(defaultClient);
        UUID consentId = UUID.randomUUID(); // UUID | 
        String accountId = "accountId_example"; // String | 
        String pageKey = "pageKey_example"; // String | 
        Integer pageSize = 50; // Integer | 
        try {
            TransactionPage result = apiInstance.getTransactions(consentId, accountId, pageKey, pageSize);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AccountsApi#getTransactions");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **consentId** | **UUID**|  | |
| **accountId** | **String**|  | |
| **pageKey** | **String**|  | [optional] |
| **pageSize** | **Integer**|  | [optional] [default to 50] |

### Return type

[**TransactionPage**](TransactionPage.md)


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | One page of transactions |  -  |
| **404** | Resource not found |  -  |
| **409** | Consent exists but is not in a state permitting data access |  -  |

## getTransactionsWithHttpInfo

> ApiResponse<TransactionPage> getTransactionsWithHttpInfo(consentId, accountId, pageKey, pageSize)

Get transactions for an account (paginated)

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AccountsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        AccountsApi apiInstance = new AccountsApi(defaultClient);
        UUID consentId = UUID.randomUUID(); // UUID | 
        String accountId = "accountId_example"; // String | 
        String pageKey = "pageKey_example"; // String | 
        Integer pageSize = 50; // Integer | 
        try {
            ApiResponse<TransactionPage> response = apiInstance.getTransactionsWithHttpInfo(consentId, accountId, pageKey, pageSize);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
            System.out.println("Response body: " + response.getData());
        } catch (ApiException e) {
            System.err.println("Exception when calling AccountsApi#getTransactions");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Response headers: " + e.getResponseHeaders());
            System.err.println("Reason: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **consentId** | **UUID**|  | |
| **accountId** | **String**|  | |
| **pageKey** | **String**|  | [optional] |
| **pageSize** | **Integer**|  | [optional] [default to 50] |

### Return type

ApiResponse<[**TransactionPage**](TransactionPage.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | One page of transactions |  -  |
| **404** | Resource not found |  -  |
| **409** | Consent exists but is not in a state permitting data access |  -  |


## listAccounts

> List<Account> listAccounts(consentId)

List accounts available under an authorised consent

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AccountsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        AccountsApi apiInstance = new AccountsApi(defaultClient);
        UUID consentId = UUID.randomUUID(); // UUID | 
        try {
            List<Account> result = apiInstance.listAccounts(consentId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AccountsApi#listAccounts");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **consentId** | **UUID**|  | |

### Return type

[**List&lt;Account&gt;**](Account.md)


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Accounts |  -  |
| **404** | Resource not found |  -  |
| **409** | Consent exists but is not in a state permitting data access |  -  |

## listAccountsWithHttpInfo

> ApiResponse<List<Account>> listAccountsWithHttpInfo(consentId)

List accounts available under an authorised consent

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AccountsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        AccountsApi apiInstance = new AccountsApi(defaultClient);
        UUID consentId = UUID.randomUUID(); // UUID | 
        try {
            ApiResponse<List<Account>> response = apiInstance.listAccountsWithHttpInfo(consentId);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
            System.out.println("Response body: " + response.getData());
        } catch (ApiException e) {
            System.err.println("Exception when calling AccountsApi#listAccounts");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Response headers: " + e.getResponseHeaders());
            System.err.println("Reason: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **consentId** | **UUID**|  | |

### Return type

ApiResponse<[**List&lt;Account&gt;**](Account.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Accounts |  -  |
| **404** | Resource not found |  -  |
| **409** | Consent exists but is not in a state permitting data access |  -  |

