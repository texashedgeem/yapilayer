
# CreatePaymentRequest


## Properties

Name | Type
------------ | -------------
`providerId` | string
`amount` | string
`currency` | string
`creditor` | [CreatePaymentRequestCreditor](CreatePaymentRequestCreditor.md)
`reference` | string
`redirectUri` | string

## Example

```typescript
import type { CreatePaymentRequest } from ''

// TODO: Update the object below with actual values
const example = {
  "providerId": mock-bank,
  "amount": 25.00,
  "currency": GBP,
  "creditor": null,
  "reference": null,
  "redirectUri": null,
} satisfies CreatePaymentRequest

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as CreatePaymentRequest
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


