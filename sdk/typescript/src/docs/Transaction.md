
# Transaction


## Properties

Name | Type
------------ | -------------
`transactionId` | string
`amount` | string
`currency` | string
`status` | string
`bookingDate` | Date
`reference` | string
`merchantName` | string

## Example

```typescript
import type { Transaction } from ''

// TODO: Update the object below with actual values
const example = {
  "transactionId": null,
  "amount": -32.40,
  "currency": null,
  "status": null,
  "bookingDate": null,
  "reference": null,
  "merchantName": null,
} satisfies Transaction

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as Transaction
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


