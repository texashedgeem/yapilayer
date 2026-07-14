# WebhooksApi

All URIs are relative to *http://localhost:8080/api/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createWebhookSubscription**](WebhooksApi.md#createwebhooksubscriptionoperation) | **POST** /webhooks | Register a webhook endpoint for payment events |
| [**listWebhookSubscriptions**](WebhooksApi.md#listwebhooksubscriptions) | **GET** /webhooks | List webhook subscriptions (secrets never returned) |



## createWebhookSubscription

> WebhookSubscription createWebhookSubscription(createWebhookSubscriptionRequest)

Register a webhook endpoint for payment events

### Example

```ts
import {
  Configuration,
  WebhooksApi,
} from '';
import type { CreateWebhookSubscriptionOperationRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new WebhooksApi();

  const body = {
    // CreateWebhookSubscriptionRequest
    createWebhookSubscriptionRequest: ...,
  } satisfies CreateWebhookSubscriptionOperationRequest;

  try {
    const data = await api.createWebhookSubscription(body);
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
| **createWebhookSubscriptionRequest** | [CreateWebhookSubscriptionRequest](CreateWebhookSubscriptionRequest.md) |  | |

### Return type

[**WebhookSubscription**](WebhookSubscription.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: `application/json`
- **Accept**: `application/json`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Subscription created (secret is write-only) |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


## listWebhookSubscriptions

> Array&lt;WebhookSubscription&gt; listWebhookSubscriptions()

List webhook subscriptions (secrets never returned)

### Example

```ts
import {
  Configuration,
  WebhooksApi,
} from '';
import type { ListWebhookSubscriptionsRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new WebhooksApi();

  try {
    const data = await api.listWebhookSubscriptions();
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

[**Array&lt;WebhookSubscription&gt;**](WebhookSubscription.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `application/json`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Subscriptions |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)

