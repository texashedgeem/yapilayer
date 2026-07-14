# ConsentsApi

All URIs are relative to *http://localhost:8080/api/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**authorisationCallback**](ConsentsApi.md#authorisationCallback) | **GET** /callback | OAuth callback the bank redirects the customer to after authorisation |
| [**authorisationCallbackWithHttpInfo**](ConsentsApi.md#authorisationCallbackWithHttpInfo) | **GET** /callback | OAuth callback the bank redirects the customer to after authorisation |
| [**createConsent**](ConsentsApi.md#createConsent) | **POST** /consents | Create an AIS consent and obtain the bank authorisation URL |
| [**createConsentWithHttpInfo**](ConsentsApi.md#createConsentWithHttpInfo) | **POST** /consents | Create an AIS consent and obtain the bank authorisation URL |
| [**getConsent**](ConsentsApi.md#getConsent) | **GET** /consents/{consentId} | Get consent status |
| [**getConsentWithHttpInfo**](ConsentsApi.md#getConsentWithHttpInfo) | **GET** /consents/{consentId} | Get consent status |



## authorisationCallback

> void authorisationCallback(state, code, error)

OAuth callback the bank redirects the customer to after authorisation

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ConsentsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        ConsentsApi apiInstance = new ConsentsApi(defaultClient);
        String state = "state_example"; // String | 
        String code = "code_example"; // String | 
        String error = "error_example"; // String | 
        try {
            apiInstance.authorisationCallback(state, code, error);
        } catch (ApiException e) {
            System.err.println("Exception when calling ConsentsApi#authorisationCallback");
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
| **302** | Redirects to the client application&#39;s redirect URI with consentId and status |  -  |

## authorisationCallbackWithHttpInfo

> ApiResponse<Void> authorisationCallbackWithHttpInfo(state, code, error)

OAuth callback the bank redirects the customer to after authorisation

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ConsentsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        ConsentsApi apiInstance = new ConsentsApi(defaultClient);
        String state = "state_example"; // String | 
        String code = "code_example"; // String | 
        String error = "error_example"; // String | 
        try {
            ApiResponse<Void> response = apiInstance.authorisationCallbackWithHttpInfo(state, code, error);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
        } catch (ApiException e) {
            System.err.println("Exception when calling ConsentsApi#authorisationCallback");
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
| **302** | Redirects to the client application&#39;s redirect URI with consentId and status |  -  |


## createConsent

> Consent createConsent(createConsentRequest)

Create an AIS consent and obtain the bank authorisation URL

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ConsentsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        ConsentsApi apiInstance = new ConsentsApi(defaultClient);
        CreateConsentRequest createConsentRequest = new CreateConsentRequest(); // CreateConsentRequest | 
        try {
            Consent result = apiInstance.createConsent(createConsentRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ConsentsApi#createConsent");
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
| **createConsentRequest** | [**CreateConsentRequest**](CreateConsentRequest.md)|  | |

### Return type

[**Consent**](Consent.md)


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Consent created, awaiting customer authorisation |  -  |

## createConsentWithHttpInfo

> ApiResponse<Consent> createConsentWithHttpInfo(createConsentRequest)

Create an AIS consent and obtain the bank authorisation URL

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ConsentsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        ConsentsApi apiInstance = new ConsentsApi(defaultClient);
        CreateConsentRequest createConsentRequest = new CreateConsentRequest(); // CreateConsentRequest | 
        try {
            ApiResponse<Consent> response = apiInstance.createConsentWithHttpInfo(createConsentRequest);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
            System.out.println("Response body: " + response.getData());
        } catch (ApiException e) {
            System.err.println("Exception when calling ConsentsApi#createConsent");
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
| **createConsentRequest** | [**CreateConsentRequest**](CreateConsentRequest.md)|  | |

### Return type

ApiResponse<[**Consent**](Consent.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Consent created, awaiting customer authorisation |  -  |


## getConsent

> Consent getConsent(consentId)

Get consent status

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ConsentsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        ConsentsApi apiInstance = new ConsentsApi(defaultClient);
        UUID consentId = UUID.randomUUID(); // UUID | 
        try {
            Consent result = apiInstance.getConsent(consentId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ConsentsApi#getConsent");
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

[**Consent**](Consent.md)


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The consent |  -  |
| **404** | Resource not found |  -  |

## getConsentWithHttpInfo

> ApiResponse<Consent> getConsentWithHttpInfo(consentId)

Get consent status

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ConsentsApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        ConsentsApi apiInstance = new ConsentsApi(defaultClient);
        UUID consentId = UUID.randomUUID(); // UUID | 
        try {
            ApiResponse<Consent> response = apiInstance.getConsentWithHttpInfo(consentId);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
            System.out.println("Response body: " + response.getData());
        } catch (ApiException e) {
            System.err.println("Exception when calling ConsentsApi#getConsent");
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

ApiResponse<[**Consent**](Consent.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The consent |  -  |
| **404** | Resource not found |  -  |

