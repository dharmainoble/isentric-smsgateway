# ✅ BulkGateway API Integration Implemented

## Overview
The ExtMTPAPI SMS service now integrates with the BulkGateway REST endpoint to send SMS messages. When an SMS is received and validated, it's forwarded to BulkGateway's `/api/v1/messages/send` endpoint.

## Files Modified/Created

### Created: ExternalApiService.java
**Location:** `ExtMTPAPI/src/main/java/com/isentric/smsserver/service/ExternalApiService.java`

**Functionality:**
- Converts `ExtMtPushReceive` data to BulkGateway API format
- Sends HTTP POST requests to `http://localhost:8090/api/v1/messages/send`
- Handles JSON serialization and error handling
- Includes telco mapping for shortcodes

**Key Methods:**
- `sendSmsToBulkGateway(ExtMtPushReceive mtRecord)` - Main method to send SMS
- `createBulkGatewayPayload(ExtMtPushReceive mtRecord)` - Maps database record to API payload
- `getTelcoFromShortcode(String shortcode)` - Maps shortcode to telco name

### Modified: SmsService.java
**Location:** `ExtMTPAPI/src/main/java/com/isentric/smsserver/service/SmsService.java`

**Changes:**
1. Added `ExternalApiService` injection
2. Replaced line 117 placeholder with actual call to `externalApiService.sendSmsToBulkGateway(mtRecord)`
3. Removed unused imports and fields
4. Improved logging for BulkGateway integration

## API Request/Response Flow

### Request Flow
```
1. SMS Request comes to ExtMTPAPI endpoint
   ↓
2. Validation & checks (package, credit, blacklist, etc.)
   ↓
3. Create ExtMtPushReceive record in avatar.extmtpush_receive_bulk table
   ↓
4. Call ExternalApiService.sendSmsToBulkGateway(mtRecord)
   ↓
5. Service creates JSON payload from mtRecord fields
   ↓
6. POST request sent to: http://localhost:8090/api/v1/messages/send
   ↓
7. BulkGateway processes SMS and returns response
   ↓
8. Record success/failure in log
```

### Request Payload Format

**URL:** `http://localhost:8090/api/v1/messages/send`  
**Method:** POST  
**Content-Type:** application/json

**Payload Structure:**
```json
{
  "guid": "message-uuid-or-mtid",
  "groupId": "api_request",
  "ip": "127.0.0.1",
  "smsc": "smpp",
  "telco": "Digi|Maxis|Celcom|U-Mobile",
  "smppName": "WSDL_DEFAULT",
  "smppConfig": "default",
  "moid": "mtid",
  "sender": "masking-id",
  "recipient": "destination-msisdn",
  "senderType": 0,
  "keyword": "message-keyword",
  "message": "sms-content",
  "messageType": 0,
  "date": timestamp,
  "price": "0.00",
  "callbackURL": "",
  "shortcode": "10086",
  "userGroup": "custid",
  "queueSequence": 0,
  "cFlag": 0
}
```

### Field Mapping

| API Field | Source Field | Notes |
|-----------|--------------|-------|
| guid | mtRecord.mtid | Message tracking ID |
| groupId | (static) | "api_request" |
| ip | (static) | "127.0.0.1" |
| smsc | (static) | "smpp" |
| telco | mtRecord.shortcode | Mapped using getTelcoFromShortcode() |
| smppName | (static) | "WSDL_DEFAULT" |
| smppConfig | (static) | "default" |
| moid | mtRecord.mtid | Message Object ID |
| sender | mtRecord.smsisdn | Masking/Sender ID |
| recipient | mtRecord.rmsisdn | Destination MSISDN |
| senderType | (static) | 0 |
| keyword | mtRecord.keyword | SMS keyword/category |
| message | mtRecord.dataStr | SMS message content |
| messageType | mtRecord.productType | Message type (0, 1, etc.) |
| date | Current time | System timestamp |
| price | mtRecord.mtprice | Message price |
| callbackURL | (empty) | Callback URL if needed |
| shortcode | mtRecord.shortcode | Shortcode used |
| userGroup | mtRecord.custid | Customer ID |
| queueSequence | (static) | 0 |
| cFlag | (static) | 0 |

### Telco Mapping

Shortcodes are mapped to telcos as follows:

| Shortcodes | Telco |
|------------|-------|
| 10086, 10010 | Digi |
| 10088, 10011 | Maxis |
| 10089, 10012 | Celcom |
| 10087, 10013 | U-Mobile |
| Others | Unknown |

## Error Handling

### Success Response
- **Status:** 200 or 201
- **Log:** `SMS sent successfully to BulkGateway - MTID: {}, Status: {}`
- **Return:** `true`

### Error Cases
- **Network Error:** Logs error message and returns `false`
- **Non-2xx Response:** Logs warning with status code and response body, returns `false`
- **Timeout:** HTTP client timeout set to 30 seconds
- **Exception:** Catches and logs all exceptions without breaking the flow

### Logging

```java
// Success
log.info("SMS sent successfully to BulkGateway - MTID: {}, Status: {}", 
         mtRecord.getMtid(), response.statusCode());

// Failure (non-2xx)
log.warn("BulkGateway returned status {} - Response: {}", 
         response.statusCode(), response.body());

// Network/IO errors
log.error("Error sending SMS to BulkGateway for MTID: {}", 
          mtRecord.getMtid(), e);

// Within SmsService
log.warn("Failed to send SMS to BulkGateway - MTID: {}, MSISDN: {}", 
         currentMtId, currentMsisdn);
```

## Processing Flow in SmsService

```java
// Step 1: Create MT record from request
ExtMtPushReceive mtRecord = createMtRecord(request, currentMsisdn, currentMtId, "P");
extMtPushReceiveRepository.save(mtRecord);

// Step 2: Create MTID tracking record
ExtMtId mtId = new ExtMtId();
mtId.setMtid(currentMtId);
mtId.setCustid(request.getCustid());
mtId.setDate(LocalDateTime.now());
extMtIdRepository.save(mtId);

// Step 3: Send to BulkGateway API
boolean bulkGatewaySuccess = externalApiService.sendSmsToBulkGateway(mtRecord);
if (!bulkGatewaySuccess) {
    log.warn("Failed to send SMS to BulkGateway - MTID: {}, MSISDN: {}", 
             currentMtId, currentMsisdn);
}

// Step 4: Queue to JMS (if enabled)
if (jmsTemplate != null) {
    jmsTemplate.convertAndSend("extmt.send.queue", mtRecord);
    log.info("SMS queued successfully - MTID: {}, MSISDN: {}", 
             currentMtId, currentMsisdn);
}
```

## Testing

### Test Endpoint
**URL:** `POST http://localhost:8081/ExtMTPush/extmtpush`

### Sample Request
```json
{
  "shortcode": "10086",
  "custid": "test_customer",
  "rmsisdn": "60163380820",
  "smsisdn": "62003",
  "mtid": "MSG001",
  "mtprice": "1.00",
  "productCode": "SC",
  "productType": 0,
  "keyword": "isentric_demo",
  "dataEncoding": 1,
  "dataStr": "Hello from Arun-test",
  "dataUrl": null,
  "dnRep": 0,
  "groupTag": null,
  "urlTitle": null,
  "ewigFlag": null,
  "cFlag": 0
}
```

### Expected Flow
1. ExtMTPAPI receives request on port 8081
2. Validates parameters and package
3. Creates ExtMtPushReceive record
4. Calls BulkGateway on port 8090
5. Returns success response

### Checking Logs
```bash
# Watch logs for API calls
tail -f /tmp/extmtpush/extmtpush.log | grep "BulkGateway\|SMS sent\|Failed to send"
```

## Configuration

### application.properties
The endpoint is hardcoded in ExternalApiService:
```properties
BULKGATEWAY_MESSAGES_ENDPOINT = "http://localhost:8090/api/v1/messages/send"
```

To make it configurable, add to `application.properties`:
```properties
bulkgateway.api.endpoint=http://localhost:8090/api/v1/messages/send
bulkgateway.api.timeout=30
```

And update ExternalApiService to use `@Value` annotation.

## Dependencies

The implementation uses:
- **HttpClient** - Java 11+ built-in HTTP client
- **Jackson** - JSON serialization (ObjectMapper)
- **Lombok** - Annotations (@Slf4j)
- **Spring** - Service annotation and dependency injection

No additional Maven dependencies needed!

## Compilation Status

✅ **SmsService.java:** No errors (4 warnings - code quality suggestions)  
✅ **ExternalApiService.java:** No errors (1 warning - switch statement enhancement)

Both files compile successfully without errors.

## Next Steps

1. **Build:** `mvn clean package -DskipTests`
2. **Run BulkGateway:** `mvn spring-boot:run` (on port 8090)
3. **Run ExtMTPAPI:** `mvn spring-boot:run` (on port 8081)
4. **Test:** Send SMS via ExtMTPAPI endpoint
5. **Verify:** Check logs and BulkGateway endpoint response

## Monitoring

Monitor these log patterns:
- `SMS sent successfully to BulkGateway` - Success
- `Failed to send SMS to BulkGateway` - Failure
- `Error sending SMS to BulkGateway` - Exception
- `BulkGateway returned status` - Non-2xx response

---

**Status:** ✅ COMPLETE & INTEGRATED
**Date:** 2026-02-03
**Version:** 1.0

