
# Payment


## Properties

Name | Type
------------ | -------------
`paymentId` | string
`providerId` | string
`status` | string
`amount` | string
`currency` | string
`creditorName` | string
`reference` | string
`createdAt` | Date
`updatedAt` | Date
`authorisationUrl` | string

## Example

```typescript
import type { Payment } from ''

// TODO: Update the object below with actual values
const example = {
  "paymentId": null,
  "providerId": null,
  "status": null,
  "amount": null,
  "currency": null,
  "creditorName": null,
  "reference": null,
  "createdAt": null,
  "updatedAt": null,
  "authorisationUrl": null,
} satisfies Payment

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as Payment
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


