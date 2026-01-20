# ExtMTPush Postman Collection

This folder contains Postman collection and environment files for testing the ExtMTPush SMS Gateway API.

## Files

| File | Description |
|------|-------------|
| `ExtMTPush_API_Collection.postman_collection.json` | Complete API collection with all endpoints |
| `ExtMTPush_Local.postman_environment.json` | Environment for local development (localhost:8087) |
| `ExtMTPush_Production.postman_environment.json` | Environment for production server |

## How to Import

### Import Collection
1. Open Postman
2. Click **Import** button (top left)
3. Select `ExtMTPush_API_Collection.postman_collection.json`
4. Click **Import**

### Import Environment
1. Click the gear icon (⚙️) in the top right corner
2. Click **Import**
3. Select `ExtMTPush_Local.postman_environment.json`
4. Click **Import**
5. Select the environment from the dropdown in top right corner

## API Categories

### 1. Health & Info
- `GET /api/sms/health` - Health check
- `GET /` - Application info
- `GET /api-docs` - API documentation
- `GET /api/test` - List all APIs

### 2. SMS APIs
- `POST /api/sms/send` - Send single SMS
- `POST /api/sms/bulk/send` - Send bulk SMS
- `GET /api/sms/status/{mtid}` - Check delivery status
- `GET /api/sms/credit/{custid}` - Get user credit
- `POST /api/sms/credit/deduct` - Deduct credit

### 3. HLR Lookup APIs
- `GET /api/hlr/lookup/{msisdn}` - Single HLR lookup
- `POST /api/hlr/batch` - Batch HLR lookup
- `GET /api/hlr/network/{msisdn}` - Get network operator
- `GET /api/hlr/validate/{msisdn}` - Validate MSISDN

### 4. Delivery Notification (DN)
- `POST /dn/celcom` - Celcom DN
- `GET /dn/celcom` - Celcom DN (GET)
- `POST /dn/digi` - Digi DN
- `POST /dn/maxis` - Maxis DN
- `POST /dn/silverstreet` - SilverStreet DN
- `GET /dn/{operator}` - Generic DN

### 5. Test Endpoints
- `GET /api/test/sms/send` - Test SMS send info
- `GET /api/test/sms/bulk` - Test bulk SMS info
- `GET /api/test/hlr/{msisdn}` - Test HLR lookup info
- `GET /api/test/dn/celcom` - Test Celcom DN info
- `GET /api/test/all` - Run all tests

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `baseUrl` | Base URL of the API server | `http://localhost:8087` |
| `custid` | Customer ID for testing | `TEST001` |
| `msisdn` | MSISDN for testing | `60123456789` |

## Sample Request Bodies

### Send SMS
```json
{
    "shortcode": "12345",
    "custid": "TEST001",
    "rmsisdn": "60123456789",
    "smsisdn": "SENDER",
    "mtid": "MSG1234567890",
    "price": "000",
    "dataStr": "Hello! This is a test message.",
    "keyword": "TEST"
}
```

### Send Bulk SMS
```json
{
    "billFlag": "1",
    "shortcode": "12345",
    "custid": "TEST001",
    "rmsisdn": "60123456789",
    "smsisdn": "SENDER",
    "mtid": "BULK1234567890",
    "mtprice": "000",
    "productType": 4,
    "productCode": "",
    "keyword": "TEST",
    "dataEncoding": 0,
    "dataStr": "This is a bulk SMS test message.",
    "dataUrl": "",
    "urlTitle": "",
    "dnrep": 1,
    "groupTag": "GROUP1",
    "ewigFlag": "0"
}
```

### Batch HLR Lookup
```json
[
    "60123456789",
    "60163456789",
    "60173456789"
]
```

## Malaysian Telco Prefixes

| Prefix | Operator |
|--------|----------|
| 012, 017 | Maxis |
| 016, 014 | Digi |
| 019, 013, 010, 011 | Celcom |
| 018 | U Mobile |

## Running the Application

Before testing with Postman, make sure the application is running:

```bash
cd /home/arun/Documents/rec/ExtMTPush
mvn spring-boot:run
```

The API will be available at `http://localhost:8087`

## Quick Test

After importing, try these requests in order:
1. **Health Check** - Verify the server is running
2. **Get User Credit** - Check credit balance
3. **Send SMS** - Send a test message
4. **Check Delivery Status** - Check message status

