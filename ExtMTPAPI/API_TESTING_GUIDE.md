# ExtMTPush API Testing Guide

## Base URL
```
http://localhost:8083/ExtMTPush
```

## Prerequisites
- Application must be running
- Database tables created (run `setup_test_data.sh`)
- Customer `CUST001` configured with credit

## API 1: Send Text Message - Single Mobile Number

### Endpoint
```
GET /extmtpush
```

### Description
Send SMS to a single mobile number.

### Parameters
| Parameter | Required | Type | Description | Example |
|-----------|----------|------|-------------|---------|
| shortcode | Yes | String | Service shortcode | 66399 |
| custid | Yes | String | Customer ID | CUST001 |
| rmsisdn | Yes | String | Recipient mobile number | 60192782366 |
| smsisdn | No | String | Sender name/number | 62003 |
| mtid | Yes | String | Unique message ID | MSG_001 |
| mtprice | No | String | Message price | 000 |
| productCode | No | String | Product code | |
| productType | Yes | Integer | Message type (4=text) | 4 |
| keyword | No | String | Keyword | |
| dataEncoding | Yes | Integer | Encoding (0=ASCII, 8=Unicode) | 0 |
| dataStr | Yes | String | Message content | Hello |
| dataUrl | No | String | URL for rich content | |
| dnRep | Yes | Integer | Delivery notification (0/1) | 0 |
| groupTag | No | String | Group identifier | 1 |

### Example Request
```bash
curl "http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60192782366&smsisdn=62003&mtid=MSG_$(date +%s)&mtprice=000&productCode=&productType=4&keyword=&dataEncoding=0&dataStr=Hello%20World&dataUrl=&dnRep=0&groupTag=1"
```

### Success Response
```
MT Receive Result : returnCode = 0,messageID = MSG_1234567890,MSISDN = 60192782366,returnMsg = Success
 ------------- 
```

### Using Postman
```
GET http://localhost:8083/ExtMTPush/extmtpush
```

**Query Parameters:**
```
shortcode: 66399
custid: CUST001
rmsisdn: 60192782366
smsisdn: 62003
mtid: MSG_001
mtprice: 000
productType: 4
dataEncoding: 0
dataStr: Hello World
dnRep: 0
groupTag: 1
```

---

## API 2: Send Text Message - Multiple Mobile Numbers

### Endpoint
```
GET /extmtpush
```

### Description
Send the same SMS to multiple mobile numbers in a single request.

### Key Difference
The `rmsisdn` parameter accepts multiple numbers separated by comma (`,`)

### Example Request
```bash
curl "http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60192782366,60122786404,60122879404&smsisdn=62003&mtid=MSG_$(date +%s)&mtprice=000&productCode=&productType=4&keyword=&dataEncoding=0&dataStr=Bulk%20SMS%20message&dataUrl=&dnRep=0&groupTag=10"
```

### Success Response
```
MT Receive Result : returnCode = 0,messageID = MSG_1234567890,MSISDN = 60192782366,60122786404,60122879404,returnMsg = Success
 ------------- 
```

### Notes
- Multiple numbers are separated by comma (`,`)
- No spaces between numbers
- All numbers receive the same message
- Each number is inserted as a separate record in database

### Using Postman
```
GET http://localhost:8083/ExtMTPush/extmtpush
```

**Query Parameters:**
```
shortcode: 66399
custid: CUST001
rmsisdn: 60192782366,60122786404,60122879404
smsisdn: 62003
mtid: MSG_002
mtprice: 000
productType: 4
dataEncoding: 0
dataStr: Bulk message
dnRep: 0
groupTag: 10
```

---

## API 3: Send Chinese Text (Unicode)

### Endpoint
```
GET /extmtpush
```

### Description
Send SMS with Chinese or other Unicode characters.

### Key Parameters
- `dataEncoding: 8` (Unicode encoding)
- `productType: 10` (Unicode message type)
- `dataStr`: Hex-encoded Unicode string

### Unicode Encoding
Chinese text must be hex-encoded:
- Original: "你好世界"
- Encoded: "4f60597d4e16754c"

### Example Request
```bash
curl "http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60192782366&smsisdn=62003&mtid=MSG_$(date +%s)&mtprice=000&productCode=&productType=10&keyword=&dataEncoding=8&dataStr=002854094e509a6c00297b2c0032573aff1a0038002900207a7a4e2d4f208bf4002000200053006b00790020004c006500670065006e0064002000209ad85ea663a88350002a002a002a002000370029002091d182729ece660e00200047006f006c00640065006e0020004c0061006b00730068006d00690020&dataUrl=&dnRep=0&groupTag=10"
```

### Success Response
```
MT Receive Result : returnCode = 0,messageID = MSG_1234567890,MSISDN = 60192782366,returnMsg = Success
 ------------- 
```

### Notes
- `dataEncoding=8` for Unicode
- `productType=10` for Unicode messages
- Message content must be hex-encoded
- Supports Chinese, Arabic, Thai, and other Unicode languages

### Using Postman
```
GET http://localhost:8083/ExtMTPush/extmtpush
```

**Query Parameters:**
```
shortcode: 66399
custid: CUST001
rmsisdn: 60192782366
smsisdn: 62003
mtid: MSG_003
mtprice: 000
productType: 10
dataEncoding: 8
dataStr: 002854094e509a6c00297b2c0032573aff1a...
dnRep: 0
groupTag: 10
```

---

## API 4: Check Credit Balance

### Endpoint
```
GET /CheckSMSUserCredit
```

### Description
Check the credit balance for a customer account.

### Parameters
| Parameter | Required | Type | Description | Example |
|-----------|----------|------|-------------|---------|
| custid | Yes | String | Customer ID | CUST001 |

### Example Request
```bash
curl "http://localhost:8083/ExtMTPush/CheckSMSUserCredit?custid=CUST001"
```

### Success Response
```
Customer: CUST001, Balance: 1000.00, Has Credit: YES
```

### JSON Response (Alternative)
```bash
curl "http://localhost:8083/ExtMTPush/CheckSMSUserCredit/details?custid=CUST001"
```

Response:
```json
{
  "custid": "CUST001",
  "creditBalance": 1000.00,
  "hasCredit": true
}
```

### Using Postman
```
GET http://localhost:8083/ExtMTPush/CheckSMSUserCredit
```

**Query Parameters:**
```
custid: CUST001
```

---

## Return Codes

| Code | Status | Description |
|------|--------|-------------|
| 0 | ✅ Success | SMS inserted successfully |
| 1 | ❌ Error | Invalid parameters |
| 2 | ❌ Error | IP not authorized |
| 3 | ❌ Error | Duplicate MTID |
| 4 | ❌ Error | Destination validation failed |
| 5 | ❌ Error | Insufficient credit |
| 6 | ❌ Error | Blacklisted number |
| 7 | ❌ Error | Invalid masking ID |
| 99 | ❌ Error | Internal server error |

---

## Testing All APIs

### Quick Test Script
```bash
cd /home/arun/Documents/rec/ExtMTPush-SpringBoot
chmod +x start_and_test_apis.sh
./start_and_test_apis.sh
```

This will:
1. Start the application
2. Wait for it to be ready
3. Test all 4 APIs automatically
4. Show results

### Manual Testing

#### Start Application
```bash
cd /home/arun/Documents/rec/ExtMTPush-SpringBoot
java -jar target/extmtpush-springboot-1.0.0.jar &
sleep 15
```

#### Test API 1 - Single Number
```bash
curl "http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60192782366&smsisdn=62003&mtid=MSG_1&mtprice=000&productType=4&dataEncoding=0&dataStr=Test1&dnRep=0&groupTag=1"
```

#### Test API 2 - Multiple Numbers
```bash
curl "http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60192782366,60122786404&smsisdn=62003&mtid=MSG_2&mtprice=000&productType=4&dataEncoding=0&dataStr=Test2&dnRep=0&groupTag=10"
```

#### Test API 3 - Unicode
```bash
curl "http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60192782366&smsisdn=62003&mtid=MSG_3&mtprice=000&productType=10&dataEncoding=8&dataStr=4f60597d4e16754c&dnRep=0&groupTag=10"
```

#### Test API 4 - Credit Check
```bash
curl "http://localhost:8083/ExtMTPush/CheckSMSUserCredit?custid=CUST001"
```

---

## Verify Results in Database

```bash
mysql -uroot -parun extmt -e "
  SELECT mtid, rmsisdn, LEFT(data_str, 30) as message, received_date 
  FROM extmtpush_receive_bulk 
  WHERE custid='CUST001'
  ORDER BY received_date DESC 
  LIMIT 10;
"
```

---

## Common Issues

### Issue 1: Connection Refused
**Problem:** `curl: (7) Failed to connect to localhost port 8083`

**Solution:**
```bash
# Check if app is running
ps aux | grep "java.*extmtpush" | grep -v grep

# Start if not running
cd /home/arun/Documents/rec/ExtMTPush-SpringBoot
java -jar target/extmtpush-springboot-1.0.0.jar &
```

### Issue 2: Return Code 2 (IP Not Authorized)
**Problem:** `returnCode = 2, returnMsg = IP not authorized`

**Solution:**
```bash
# Run database setup
cd /home/arun/Documents/rec/ExtMTPush-SpringBoot
./setup_test_data.sh
```

### Issue 3: Return Code 5 (Insufficient Credit)
**Problem:** `returnCode = 5, returnMsg = Insufficient credit`

**Solution:**
```bash
# Add credit to customer
mysql -uroot -parun bulk_config -e "
  UPDATE customer_credit 
  SET credit_balance = 10000.00, active = '1' 
  WHERE custid = 'CUST001';
"
```

### Issue 4: Return Code 3 (Duplicate MTID)
**Problem:** `returnCode = 3, returnMsg = Duplicate message ID`

**Solution:** Use unique MTID for each request:
```bash
# Use timestamp
MTID="MSG_$(date +%s)"

# Or use UUID
MTID="MSG_$(uuidgen | cut -d'-' -f1)"
```

---

## Postman Collection

Import the provided Postman collection:
```
ExtMTPush_Postman_Collection.json
```

The collection includes:
- All 4 API endpoints pre-configured
- Variable {{$timestamp}} for unique MTIDs
- Sample requests ready to use

---

## Performance Testing

### Load Test Example
```bash
# Send 100 messages
for i in {1..100}; do
  curl -s "http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60192782366&mtid=MSG_$i&productType=4&dataEncoding=0&dataStr=Test$i&dnRep=0" &
done

# Wait for all to complete
wait

# Check results
mysql -uroot -parun extmt -e "SELECT COUNT(*) as total FROM extmtpush_receive_bulk WHERE custid='CUST001';"
```

---

## Summary

✅ **API 1:** Single SMS - Works with GET request  
✅ **API 2:** Bulk SMS - Use comma-separated numbers  
✅ **API 3:** Unicode SMS - Use encoding=8 and hex-encoded text  
✅ **API 4:** Credit Check - Simple GET with custid

All APIs support both:
- GET requests with query parameters (shown above)
- POST requests with JSON body (alternative)

---

**Created:** December 17, 2025  
**Status:** All APIs documented and ready for testing  
**Test Script:** `./start_and_test_apis.sh`

