
# CreateConsentRequest


## Properties

Name | Type
------------ | -------------
`providerId` | string
`permissions` | Array&lt;string&gt;
`redirectUri` | string

## Example

```typescript
import type { CreateConsentRequest } from ''

// TODO: Update the object below with actual values
const example = {
  "providerId": mock-bank,
  "permissions": null,
  "redirectUri": null,
} satisfies CreateConsentRequest

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as CreateConsentRequest
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


