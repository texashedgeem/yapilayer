# WebhooksApi

All URIs are relative to *http://localhost:8080/api/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createWebhookSubscription**](WebhooksApi.md#createWebhookSubscription) | **POST** /webhooks | Register a webhook endpoint for payment events |
| [**createWebhookSubscriptionWithHttpInfo**](WebhooksApi.md#createWebhookSubscriptionWithHttpInfo) | **POST** /webhooks | Register a webhook endpoint for payment events |
| [**listWebhookSubscriptions**](WebhooksApi.md#listWebhookSubscriptions) | **GET** /webhooks | List webhook subscriptions (secrets never returned) |
| [**listWebhookSubscriptionsWithHttpInfo**](WebhooksApi.md#listWebhookSubscriptionsWithHttpInfo) | **GET** /webhooks | List webhook subscriptions (secrets never returned) |



## createWebhookSubscription

> WebhookSubscription createWebhookSubscription(createWebhookSubscriptionRequest)

Register a webhook endpoint for payment events

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.WebhooksApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        WebhooksApi apiInstance = new WebhooksApi(defaultClient);
        CreateWebhookSubscriptionRequest createWebhookSubscriptionRequest = new CreateWebhookSubscriptionRequest(); // CreateWebhookSubscriptionRequest | 
        try {
            WebhookSubscription result = apiInstance.createWebhookSubscription(createWebhookSubscriptionRequest);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling WebhooksApi#createWebhookSubscription");
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
| **createWebhookSubscriptionRequest** | [**CreateWebhookSubscriptionRequest**](CreateWebhookSubscriptionRequest.md)|  | |

### Return type

[**WebhookSubscription**](WebhookSubscription.md)


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Subscription created (secret is write-only) |  -  |

## createWebhookSubscriptionWithHttpInfo

> ApiResponse<WebhookSubscription> createWebhookSubscriptionWithHttpInfo(createWebhookSubscriptionRequest)

Register a webhook endpoint for payment events

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.WebhooksApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        WebhooksApi apiInstance = new WebhooksApi(defaultClient);
        CreateWebhookSubscriptionRequest createWebhookSubscriptionRequest = new CreateWebhookSubscriptionRequest(); // CreateWebhookSubscriptionRequest | 
        try {
            ApiResponse<WebhookSubscription> response = apiInstance.createWebhookSubscriptionWithHttpInfo(createWebhookSubscriptionRequest);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
            System.out.println("Response body: " + response.getData());
        } catch (ApiException e) {
            System.err.println("Exception when calling WebhooksApi#createWebhookSubscription");
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
| **createWebhookSubscriptionRequest** | [**CreateWebhookSubscriptionRequest**](CreateWebhookSubscriptionRequest.md)|  | |

### Return type

ApiResponse<[**WebhookSubscription**](WebhookSubscription.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Subscription created (secret is write-only) |  -  |


## listWebhookSubscriptions

> List<WebhookSubscription> listWebhookSubscriptions()

List webhook subscriptions (secrets never returned)

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.WebhooksApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        WebhooksApi apiInstance = new WebhooksApi(defaultClient);
        try {
            List<WebhookSubscription> result = apiInstance.listWebhookSubscriptions();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling WebhooksApi#listWebhookSubscriptions");
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

[**List&lt;WebhookSubscription&gt;**](WebhookSubscription.md)


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Subscriptions |  -  |

## listWebhookSubscriptionsWithHttpInfo

> ApiResponse<List<WebhookSubscription>> listWebhookSubscriptionsWithHttpInfo()

List webhook subscriptions (secrets never returned)

### Example

```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.WebhooksApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:8080/api/v1");

        WebhooksApi apiInstance = new WebhooksApi(defaultClient);
        try {
            ApiResponse<List<WebhookSubscription>> response = apiInstance.listWebhookSubscriptionsWithHttpInfo();
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response headers: " + response.getHeaders());
            System.out.println("Response body: " + response.getData());
        } catch (ApiException e) {
            System.err.println("Exception when calling WebhooksApi#listWebhookSubscriptions");
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

ApiResponse<[**List&lt;WebhookSubscription&gt;**](WebhookSubscription.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Subscriptions |  -  |

