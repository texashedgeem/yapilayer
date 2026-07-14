# PaymentsApi

All URIs are relative to *http://localhost:8080/api/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createPayment**](PaymentsApi.md#createPayment) | **POST** /payments | Create a single domestic payment and obtain the bank authorisation URL |
| [**createPaymentWithHttpInfo**](PaymentsApi.md#createPaymentWithHttpInfo) | **POST** /payments | Create a single domestic payment and obtain the bank authorisation URL |
| [**getPayment**](PaymentsApi.md#getPayment) | **GET** /payments/{paymentId} | Get payment status (refreshes from the provider while in flight) |
| [**getPaymentWithHttpInfo**](PaymentsApi.md#getPaymentWithHttpInfo) | **GET** /payments/{paymentId} | Get payment status (refreshes from the provider while in flight) |
| [**paymentAuthorisationCallback**](PaymentsApi.md#paymentAuthorisationCallback) | **GET** /payments/callback | OAuth callback for payment authorisation journeys |
| [**paymentAuthorisationCallbackWithHttpInfo**](PaymentsApi.md#paymentAuthorisationCallbackWithHttpInfo) | **GET** /payments/callback | OAuth callback for payment authorisation journeys |



## createPayment

> Payment createPayment(createPaymentRequest)

Create a single domestic payment and obtain the bank authorisation URL

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PaymentsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        PaymentsApi apiInstance = new PaymentsApi(defaultClient);
        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest(); // CreatePaymentRequest | 
        try {
            Payment result = apiInstance.createPayment(createPaymentRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PaymentsApi#createPayment");
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
| **createPaymentRequest** | [**CreatePaymentRequest**](CreatePaymentRequest.md)|  | |

### Return type

[**Payment**](Payment.md)


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Payment created, awaiting customer authorisation |  -  |

## createPaymentWithHttpInfo

> ApiResponse<Payment> createPaymentWithHttpInfo(createPaymentRequest)

Create a single domestic payment and obtain the bank authorisation URL

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PaymentsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        PaymentsApi apiInstance = new PaymentsApi(defaultClient);
        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest(); // CreatePaymentRequest | 
        try {
            ApiResponse<Payment> response = apiInstance.createPaymentWithHttpInfo(createPaymentRequest);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
            System.out.println("Response body: " + response.getData());
        } catch (ApiException e) {
            System.err.println("Exception when calling PaymentsApi#createPayment");
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
| **createPaymentRequest** | [**CreatePaymentRequest**](CreatePaymentRequest.md)|  | |

### Return type

ApiResponse<[**Payment**](Payment.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Payment created, awaiting customer authorisation |  -  |


## getPayment

> Payment getPayment(paymentId)

Get payment status (refreshes from the provider while in flight)

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PaymentsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        PaymentsApi apiInstance = new PaymentsApi(defaultClient);
        UUID paymentId = UUID.randomUUID(); // UUID | 
        try {
            Payment result = apiInstance.getPayment(paymentId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling PaymentsApi#getPayment");
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
| **paymentId** | **UUID**|  | |

### Return type

[**Payment**](Payment.md)


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The payment |  -  |
| **404** | Resource not found |  -  |

## getPaymentWithHttpInfo

> ApiResponse<Payment> getPaymentWithHttpInfo(paymentId)

Get payment status (refreshes from the provider while in flight)

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PaymentsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        PaymentsApi apiInstance = new PaymentsApi(defaultClient);
        UUID paymentId = UUID.randomUUID(); // UUID | 
        try {
            ApiResponse<Payment> response = apiInstance.getPaymentWithHttpInfo(paymentId);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
            System.out.println("Response body: " + response.getData());
        } catch (ApiException e) {
            System.err.println("Exception when calling PaymentsApi#getPayment");
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
| **paymentId** | **UUID**|  | |

### Return type

ApiResponse<[**Payment**](Payment.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The payment |  -  |
| **404** | Resource not found |  -  |


## paymentAuthorisationCallback

> void paymentAuthorisationCallback(state, code, error)

OAuth callback for payment authorisation journeys

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PaymentsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        PaymentsApi apiInstance = new PaymentsApi(defaultClient);
        String state = "state_example"; // String | 
        String code = "code_example"; // String | 
        String error = "error_example"; // String | 
        try {
            apiInstance.paymentAuthorisationCallback(state, code, error);
        } catch (ApiException e) {
            System.err.println("Exception when calling PaymentsApi#paymentAuthorisationCallback");
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
| **state** | **String**|  | |
| **code** | **String**|  | [optional] |
| **error** | **String**|  | [optional] |

### Return type


null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **302** | Redirects to the client application&#39;s redirect URI with paymentId and status |  -  |

## paymentAuthorisationCallbackWithHttpInfo

> ApiResponse<Void> paymentAuthorisationCallbackWithHttpInfo(state, code, error)

OAuth callback for payment authorisation journeys

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PaymentsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        PaymentsApi apiInstance = new PaymentsApi(defaultClient);
        String state = "state_example"; // String | 
        String code = "code_example"; // String | 
        String error = "error_example"; // String | 
        try {
            ApiResponse<Void> response = apiInstance.paymentAuthorisationCallbackWithHttpInfo(state, code, error);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
        } catch (ApiException e) {
            System.err.println("Exception when calling PaymentsApi#paymentAuthorisationCallback");
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
| **state** | **String**|  | |
| **code** | **String**|  | [optional] |
| **error** | **String**|  | [optional] |

### Return type


ApiResponse<Void>

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **302** | Redirects to the client application&#39;s redirect URI with paymentId and status |  -  |

