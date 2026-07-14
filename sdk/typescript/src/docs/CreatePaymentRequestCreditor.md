
# CreatePaymentRequestCreditor


## Properties

Name | Type
------------ | -------------
`name` | string
`scheme` | string
`identification` | string

## Example

```typescript
import type { CreatePaymentRequestCreditor } from ''

// TODO: Update the object below with actual values
const example = {
  "name": null,
  "scheme": UK.OBIE.SortCodeAccountNumber,
  "identification": null,
} satisfies CreatePaymentRequestCreditor

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as CreatePaymentRequestCreditor
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


