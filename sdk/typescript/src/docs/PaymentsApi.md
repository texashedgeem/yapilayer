# PaymentsApi

All URIs are relative to *http://localhost:8080/api/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createPayment**](PaymentsApi.md#createpaymentoperation) | **POST** /payments | Create a single domestic payment and obtain the bank authorisation URL |
| [**getPayment**](PaymentsApi.md#getpayment) | **GET** /payments/{paymentId} | Get payment status (refreshes from the provider while in flight) |
| [**paymentAuthorisationCallback**](PaymentsApi.md#paymentauthorisationcallback) | **GET** /payments/callback | OAuth callback for payment authorisation journeys |



## createPayment

> Payment createPayment(createPaymentRequest)

Create a single domestic payment and obtain the bank authorisation URL

### Example

```ts
import {
  Configuration,
  PaymentsApi,
} from '';
import type { CreatePaymentOperationRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new PaymentsApi();

  const body = {
    // CreatePaymentRequest
    createPaymentRequest: ...,
  } satisfies CreatePaymentOperationRequest;

  try {
    const data = await api.createPayment(body);
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
| **createPaymentRequest** | [CreatePaymentRequest](CreatePaymentRequest.md) |  | |

### Return type

[**Payment**](Payment.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: `application/json`
- **Accept**: `application/json`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Payment created, awaiting customer authorisation |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


## getPayment

> Payment getPayment(paymentId)

Get payment status (refreshes from the provider while in flight)

### Example

```ts
import {
  Configuration,
  PaymentsApi,
} from '';
import type { GetPaymentRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new PaymentsApi();

  const body = {
    // string
    paymentId: 38400000-8cf0-11bd-b23e-10b96e4ef00d,
  } satisfies GetPaymentRequest;

  try {
    const data = await api.getPayment(body);
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
| **paymentId** | `string` |  | [Defaults to `undefined`] |

### Return type

[**Payment**](Payment.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `application/json`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The payment |  -  |
| **404** | Resource not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


## paymentAuthorisationCallback

> paymentAuthorisationCallback(state, code, error)

OAuth callback for payment authorisation journeys

### Example

```ts
import {
  Configuration,
  PaymentsApi,
} from '';
import type { PaymentAuthorisationCallbackRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new PaymentsApi();

  const body = {
    // string
    state: state_example,
    // string (optional)
    code: code_example,
    // string (optional)
    error: error_example,
  } satisfies PaymentAuthorisationCallbackRequest;

  try {
    const data = await api.paymentAuthorisationCallback(body);
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
| **state** | `string` |  | [Defaults to `undefined`] |
| **code** | `string` |  | [Optional] [Defaults to `undefined`] |
| **error** | `string` |  | [Optional] [Defaults to `undefined`] |

### Return type

`void` (Empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **302** | Redirects to the client application\&#39;s redirect URI with paymentId and status |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)

