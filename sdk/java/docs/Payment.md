

# Payment


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**paymentId** | **UUID** |  |  |
|**providerId** | **String** |  |  |
|**status** | [**StatusEnum**](#StatusEnum) |  |  |
|**amount** | **String** |  |  |
|**currency** | **String** |  |  |
|**creditorName** | **String** |  |  |
|**reference** | **String** |  |  |
|**createdAt** | **OffsetDateTime** |  |  [optional] |
|**updatedAt** | **OffsetDateTime** |  |  [optional] |
|**authorisationUrl** | **URI** | Present on creation only |  [optional] |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| PENDING | &quot;PENDING&quot; |
| AUTHORISED | &quot;AUTHORISED&quot; |
| REJECTED | &quot;REJECTED&quot; |
| COMPLETED | &quot;COMPLETED&quot; |
| CANCELLED | &quot;CANCELLED&quot; |



