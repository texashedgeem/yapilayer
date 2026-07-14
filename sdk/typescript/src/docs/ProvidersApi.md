# ProvidersApi

All URIs are relative to *http://localhost:8080/api/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**listProviders**](ProvidersApi.md#listproviders) | **GET** /providers | List registered bank providers and their declared capabilities |



## listProviders

> Array&lt;Provider&gt; listProviders()

List registered bank providers and their declared capabilities

### Example

```ts
import {
  Configuration,
  ProvidersApi,
} from '';
import type { ListProvidersRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new ProvidersApi();

  try {
    const data = await api.listProviders();
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**Array&lt;Provider&gt;**](Provider.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `application/json`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Registered providers |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)

