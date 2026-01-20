# ExtMTPush REST API Testing Guide

## 📍 Endpoint Information

**Base URL**: `http://localhost:8083/ExtMTPush`  
**SMS Endpoint**: `/extmtpush`  
**Full URL**: `http://localhost:8083/ExtMTPush/extmtpush`

## 🔧 Supported Methods

The endpoint supports both **GET** and **POST** methods for backward compatibility.

---

## 📝 Request Parameters

### Required Parameters
| Parameter | Type | Description | Example |
|-----------|------|-------------|---------|
| `shortcode` | String | SMS shortcode (max 10 chars) | "66399" |
| `custid` | String | Customer ID (max 50 chars) | "CUST001" |
| `rmsisdn` | String | Recipient MSISDN/phone number | "60123456789" |
| `mtid` | String | Message Transaction ID (max 100 chars) | "MSG001" |

### Optional Parameters
| Parameter | Type | Description | Default | Example |
|-----------|------|-------------|---------|---------|
| `smsisdn` | String | Sender MSISDN/Masking ID | null | "MyCompany" |
| `mtprice` | String | Message price | null | "0.10" |
| `productCode` | String | Product code | null | "PROD123" |
| `productType` | Integer | Product type (0-15) | null | 4 |
| `keyword` | String | SMS keyword | null | "INFO" |
| `dataEncoding` | Integer | Data encoding type | null | 0 |
| `dataStr` | String | SMS message text | null | "Hello World" |
| `dataUrl` | String | Content URL | null | "http://example.com" |
| `urlTitle` | String | URL title | null | "Click here" |
| `dnRep` | Integer | Delivery notification report | null | 1 |
| `groupTag` | String | Group tag for bulk messages | null | "PROMO2024" |
| `ewigFlag` | String | EWIG flag | null | "0" |
| `cFlag` | String | Custom flag | "0" | "1" |

### Product Type Values
```
0  = Remix Ringtone
1  = Ringtone
2  = Logo
3  = Picture
4  = Text (Standard SMS)
5  = WAP Polytone
6  = WAP Truetone
7  = WAP Picture
8  = WAP Java Game
11 = WAP Theme
12 = WAP eBook
13 = WAP Animation
14 = WAP Karaoke
15 = WAP Video
```

---

## 🧪 Test Examples

### 1. Simple SMS (GET Request)

#### Using curl:
```bash
curl -X GET "http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60123456789&mtid=MSG001&productType=4&dataEncoding=0&dnRep=1&dataStr=Hello%20from%20SMS%20Gateway"
```

#### Using wget:
```bash
wget -qO- "http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60123456789&mtid=MSG001&productType=4&dataEncoding=0&dnRep=1&dataStr=Test%20Message"
```

#### Browser URL:
```
http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60123456789&mtid=MSG001&productType=4&dataEncoding=0&dnRep=1&dataStr=Hello
```

---

### 2. Full SMS with All Parameters (GET)

```bash
curl -X GET "http://localhost:8083/ExtMTPush/extmtpush?\
shortcode=66399&\
custid=CUST001&\
rmsisdn=60123456789&\
smsisdn=MyCompany&\
mtid=MSG002&\
mtprice=0.10&\
productCode=PROD123&\
productType=4&\
keyword=INFO&\
dataEncoding=0&\
dataStr=Welcome%20to%20our%20service&\
dataUrl=http://example.com&\
urlTitle=Visit%20Us&\
dnRep=1&\
groupTag=PROMO2024&\
ewigFlag=0&\
cFlag=1"
```

---

### 3. SMS with POST (JSON)

#### Simple POST:
```bash
curl -X POST "http://localhost:8083/ExtMTPush/extmtpush" \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode": "66399",
    "custid": "CUST001",
    "rmsisdn": "60123456789",
    "mtid": "MSG003",
    "productType": 4,
    "dataEncoding": 0,
    "dataStr": "Hello from JSON POST",
    "dnRep": 1
  }'
```

#### Full POST with all fields:
```bash
curl -X POST "http://localhost:8083/ExtMTPush/extmtpush" \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode": "66399",
    "custid": "CUST001",
    "rmsisdn": "60123456789",
    "smsisdn": "MyCompany",
    "mtid": "MSG004",
    "mtprice": "0.10",
    "productCode": "PROD123",
    "productType": 4,
    "keyword": "INFO",
    "dataEncoding": 0,
    "dataStr": "Welcome to our premium service. Reply STOP to unsubscribe.",
    "dataUrl": "http://example.com/offer",
    "urlTitle": "Special Offer",
    "dnRep": 1,
    "groupTag": "CAMPAIGN_DEC2024",
    "ewigFlag": "0",
    "cFlag": "1"
  }'
```

---

### 4. Multiple Recipients (Comma-separated)

```bash
curl -X POST "http://localhost:8083/ExtMTPush/extmtpush" \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode": "66399",
    "custid": "CUST001",
    "rmsisdn": "60123456789,60129876543,60187654321",
    "mtid": "MSG005",
    "productType": 4,
    "dataEncoding": 0,
    "dataStr": "Bulk SMS to multiple recipients",
    "dnRep": 1
  }'
```

---

## 📊 Response Format

### Success Response:
```
MT Receive Result : returnCode = 0,messageID = MSG001,MSISDN = 60123456789,returnMsg = Success
 ------------- 
```

### Error Responses:

| Return Code | Message | Description |
|-------------|---------|-------------|
| 0 | Success | SMS queued successfully |
| 1 | Invalid parameters | Missing or invalid parameters |
| 2 | Package validation failed | IP not authorized |
| 3 | Duplicate message ID | MTID already exists |
| 4 | Destination validation failed | Invalid MSISDN |
| 5 | Insufficient credit | Customer has no credit |
| 6 | Blocked - blacklisted number | Number is blacklisted |
| 7 | Invalid masking ID | Sender ID not allowed |
| 99 | Internal error | System error |

---

## 🧪 Postman Collection

### Import this JSON into Postman:

```json
{
  "info": {
    "name": "ExtMTPush SMS API",
    "description": "SMS Gateway API Collection",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Send SMS - GET",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60123456789&mtid=MSG{{$timestamp}}&productType=4&dataEncoding=0&dnRep=1&dataStr=Test Message",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8083",
          "path": ["ExtMTPush", "extmtpush"],
          "query": [
            {"key": "shortcode", "value": "66399"},
            {"key": "custid", "value": "CUST001"},
            {"key": "rmsisdn", "value": "60123456789"},
            {"key": "mtid", "value": "MSG{{$timestamp}}"},
            {"key": "productType", "value": "4"},
            {"key": "dataEncoding", "value": "0"},
            {"key": "dnRep", "value": "1"},
            {"key": "dataStr", "value": "Test Message"}
          ]
        }
      }
    },
    {
      "name": "Send SMS - POST JSON",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"shortcode\": \"66399\",\n  \"custid\": \"CUST001\",\n  \"rmsisdn\": \"60123456789\",\n  \"mtid\": \"MSG{{$timestamp}}\",\n  \"productType\": 4,\n  \"dataEncoding\": 0,\n  \"dataStr\": \"Hello from Postman\",\n  \"dnRep\": 1\n}"
        },
        "url": {
          "raw": "http://localhost:8083/ExtMTPush/extmtpush",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8083",
          "path": ["ExtMTPush", "extmtpush"]
        }
      }
    }
  ]
}
```

---

## 🐍 Python Example

```python
import requests
import time

# POST request
url = "http://localhost:8083/ExtMTPush/extmtpush"

payload = {
    "shortcode": "66399",
    "custid": "CUST001",
    "rmsisdn": "60123456789",
    "mtid": f"MSG{int(time.time())}",
    "productType": 4,
    "dataEncoding": 0,
    "dataStr": "Hello from Python",
    "dnRep": 1
}

headers = {
    "Content-Type": "application/json"
}

response = requests.post(url, json=payload, headers=headers)
print(f"Status Code: {response.status_code}")
print(f"Response: {response.text}")
```

---

## 🟢 Node.js Example

```javascript
const axios = require('axios');

async function sendSMS() {
  const url = 'http://localhost:8083/ExtMTPush/extmtpush';
  
  const data = {
    shortcode: '66399',
    custid: 'CUST001',
    rmsisdn: '60123456789',
    mtid: `MSG${Date.now()}`,
    productType: 4,
    dataEncoding: 0,
    dataStr: 'Hello from Node.js',
    dnRep: 1
  };

  try {
    const response = await axios.post(url, data);
    console.log('Status:', response.status);
    console.log('Response:', response.data);
  } catch (error) {
    console.error('Error:', error.message);
  }
}

sendSMS();
```

---

## 📱 PHP Example

```php
<?php
$url = 'http://localhost:8083/ExtMTPush/extmtpush';

$data = array(
    'shortcode' => '66399',
    'custid' => 'CUST001',
    'rmsisdn' => '60123456789',
    'mtid' => 'MSG' . time(),
    'productType' => 4,
    'dataEncoding' => 0,
    'dataStr' => 'Hello from PHP',
    'dnRep' => 1
);

$options = array(
    'http' => array(
        'header'  => "Content-Type: application/json\r\n",
        'method'  => 'POST',
        'content' => json_encode($data)
    )
);

$context  = stream_context_create($options);
$result = file_get_contents($url, false, $context);

if ($result === FALSE) {
    echo "Error sending SMS\n";
} else {
    echo "Response: " . $result . "\n";
}
?>
```

---

## 🔍 Testing Checklist

### Before Testing:
- [ ] Application is running (`ps aux | grep extmtpush`)
- [ ] MySQL databases are accessible
- [ ] Required database tables exist
- [ ] Customer ID exists in the database (or validation is disabled)

### Test Scenarios:
- [ ] Simple GET request with minimal parameters
- [ ] POST request with JSON body
- [ ] Multiple recipients (comma-separated)
- [ ] Special characters in message text
- [ ] Different product types
- [ ] Invalid parameters (test error handling)
- [ ] Duplicate MTID (should fail)

---

## 📝 Quick Test Script

Save this as `test_sms_api.sh`:

```bash
#!/bin/bash

BASE_URL="http://localhost:8083/ExtMTPush/extmtpush"
TIMESTAMP=$(date +%s)

echo "Testing ExtMTPush API..."
echo "========================"
echo ""

echo "Test 1: Simple SMS (GET)"
curl -X GET "${BASE_URL}?shortcode=66399&custid=CUST001&rmsisdn=60123456789&mtid=MSG${TIMESTAMP}&productType=4&dataEncoding=0&dnRep=1&dataStr=Test"
echo -e "\n"

echo "Test 2: SMS with POST (JSON)"
curl -X POST "${BASE_URL}" \
  -H "Content-Type: application/json" \
  -d "{
    \"shortcode\": \"66399\",
    \"custid\": \"CUST001\",
    \"rmsisdn\": \"60123456789\",
    \"mtid\": \"MSG${TIMESTAMP}_POST\",
    \"productType\": 4,
    \"dataEncoding\": 0,
    \"dataStr\": \"Hello from POST\",
    \"dnRep\": 1
  }"
echo -e "\n"

echo "========================"
echo "Testing complete!"
```

Make it executable:
```bash
chmod +x test_sms_api.sh
./test_sms_api.sh
```

---

## 📊 Check Database After Insertion

```sql
-- View recent SMS records
USE extmt;

SELECT 
    row_id,
    shortcode,
    custid,
    rmsisdn,
    mtid,
    data_str,
    process_flag,
    received_date
FROM extmtpush_receive_bulk
ORDER BY received_date DESC
LIMIT 10;
```

---

## 🔐 Important Notes

1. **JMS Status**: Currently disabled - messages are saved to database but not queued
2. **IP Validation**: May need to configure allowed IPs in `cp_ip` table
3. **Customer Credit**: Check if credit validation is enforced
4. **MTID**: Must be unique per customer to avoid duplicates
5. **MSISDN Format**: Accepts various formats, will be normalized (e.g., 60123456789)

---

## 🆘 Troubleshooting

### Issue: "Package validation failed"
**Solution**: Add your IP to the `cp_ip` table in `bulk_config` database

### Issue: "Insufficient credit"
**Solution**: Add credit to customer in `customer_credit` table or disable credit check

### Issue: "Duplicate message ID"
**Solution**: Use unique MTID for each request (e.g., append timestamp)

### Issue: Connection refused
**Solution**: Verify application is running: `curl http://localhost:8083/ExtMTPush/actuator/health`

---

**Ready to test!** Start with the simple curl examples above. 🚀

