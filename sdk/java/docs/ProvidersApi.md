# ProvidersApi

All URIs are relative to *http://localhost:8080/api/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**listProviders**](ProvidersApi.md#listProviders) | **GET** /providers | List registered bank providers and their declared capabilities |
| [**listProvidersWithHttpInfo**](ProvidersApi.md#listProvidersWithHttpInfo) | **GET** /providers | List registered bank providers and their declared capabilities |



## listProviders

> List<Provider> listProviders()

List registered bank providers and their declared capabilities

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ProvidersApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        ProvidersApi apiInstance = new ProvidersApi(defaultClient);
        try {
            List<Provider> result = apiInstance.listProviders();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ProvidersApi#listProviders");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**List&lt;Provider&gt;**](Provider.md)


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Registered providers |  -  |

## listProvidersWithHttpInfo

> ApiResponse<List<Provider>> listProvidersWithHttpInfo()

List registered bank providers and their declared capabilities

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ProvidersApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        ProvidersApi apiInstance = new ProvidersApi(defaultClient);
        try {
            ApiResponse<List<Provider>> response = apiInstance.listProvidersWithHttpInfo();
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
            System.out.println("Response body: " + response.getData());
        } catch (ApiException e) {
            System.err.println("Exception when calling ProvidersApi#listProviders");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Response headers: " + e.getResponseHeaders());
            System.err.println("Reason: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

ApiResponse<[**List&lt;Provider&gt;**](Provider.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Registered providers |  -  |

