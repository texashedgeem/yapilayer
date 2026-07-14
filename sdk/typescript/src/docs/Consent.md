
# Consent


## Properties

Name | Type
------------ | -------------
`consentId` | string
`providerId` | string
`status` | string
`permissions` | Array&lt;string&gt;
`expiresAt` | Date
`authorisationUrl` | string

## Example

```typescript
import type { Consent } from ''

// TODO: Update the object below with actual values
const example = {
  "consentId": null,
  "providerId": null,
  "status": null,
  "permissions": null,
  "expiresAt": null,
  "authorisationUrl": null,
} satisfies Consent

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as Consent
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


