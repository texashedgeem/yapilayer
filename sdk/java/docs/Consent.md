

# Consent


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**consentId** | **UUID** |  |  |
|**providerId** | **String** |  |  |
|**status** | [**StatusEnum**](#StatusEnum) |  |  |
|**permissions** | **List&lt;String&gt;** |  |  |
|**expiresAt** | **OffsetDateTime** |  |  |
|**authorisationUrl** | **URI** | Present on creation only — where to send the customer to authorise |  [optional] |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| AWAITING_AUTHORISATION | &quot;AWAITING_AUTHORISATION&quot; |
| AUTHORISED | &quot;AUTHORISED&quot; |
| REJECTED | &quot;REJECTED&quot; |
| REVOKED | &quot;REVOKED&quot; |
| EXPIRED | &quot;EXPIRED&quot; |



