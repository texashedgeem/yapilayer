# ConsentsApi

All URIs are relative to *http://localhost:8080/api/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**authorisationCallback**](ConsentsApi.md#authorisationcallback) | **GET** /callback | OAuth callback the bank redirects the customer to after authorisation |
| [**createConsent**](ConsentsApi.md#createconsentoperation) | **POST** /consents | Create an AIS consent and obtain the bank authorisation URL |
| [**getConsent**](ConsentsApi.md#getconsent) | **GET** /consents/{consentId} | Get consent status |



## authorisationCallback

> authorisationCallback(state, code, error)

OAuth callback the bank redirects the customer to after authorisation

### Example

```ts
import {
  Configuration,
  ConsentsApi,
} from '';
import type { AuthorisationCallbackRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new ConsentsApi();

  const body = {
    // string
    state: state_example,
    // string (optional)
    code: code_example,
    // string (optional)
    error: error_example,
  } satisfies AuthorisationCallbackRequest;

  try {
    const data = await api.authorisationCallback(body);
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
| **302** | Redirects to the client application\&#39;s redirect URI with consentId and status |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


## createConsent

> Consent createConsent(createConsentRequest)

Create an AIS consent and obtain the bank authorisation URL

### Example

```ts
import {
  Configuration,
  ConsentsApi,
} from '';
import type { CreateConsentOperationRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new ConsentsApi();

  const body = {
    // CreateConsentRequest
    createConsentRequest: ...,
  } satisfies CreateConsentOperationRequest;

  try {
    const data = await api.createConsent(body);
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
| **createConsentRequest** | [CreateConsentRequest](CreateConsentRequest.md) |  | |

### Return type

[**Consent**](Consent.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: `application/json`
- **Accept**: `application/json`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Consent created, awaiting customer authorisation |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


## getConsent

> Consent getConsent(consentId)

Get consent status

### Example

```ts
import {
  Configuration,
  ConsentsApi,
} from '';
import type { GetConsentRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new ConsentsApi();

  const body = {
    // string
    consentId: 38400000-8cf0-11bd-b23e-10b96e4ef00d,
  } satisfies GetConsentRequest;

  try {
    const data = await api.getConsent(body);
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

[**Consent**](Consent.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `application/json`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The consent |  -  |
| **404** | Resource not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)

