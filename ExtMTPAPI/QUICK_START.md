# 🚀 Quick Start Guide - Testing ExtMTPush API

## Step 1: Setup Test Data (One-time setup)

Run this script to create necessary database tables and test data:

```bash
./setup_test_data.sh
```

This will:
- ✅ Create required database tables
- ✅ Add test customer (CUST001) with credit balance
- ✅ Authorize localhost IP addresses
- ✅ Configure test shortcode (66399)

---

## Step 2: Simple Test

### Option A: Quick curl test

```bash
curl -X POST "http://localhost:8083/ExtMTPush/extmtpush" \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode": "66399",
    "custid": "CUST001",
    "rmsisdn": "60123456789",
    "mtid": "MSG001",
    "productType": 4,
    "dataEncoding": 0,
    "dataStr": "Hello World",
    "dnRep": 1
  }'
```

### Option B: Run automated tests

```bash
./test_sms_api.sh
```

This will run 4 different test scenarios and show results.

---

## Step 3: Check Results

### View SMS records in database:

```bash
mysql -uroot -parun -e "
USE extmt;
SELECT 
    row_id,
    mtid,
    custid,
    rmsisdn,
    data_str,
    process_flag,
    received_date
FROM extmtpush_receive_bulk
ORDER BY received_date DESC
LIMIT 10;
"
```

### View application logs:

```bash
tail -f logs/extmtpush.log
```

---

## 📋 Test Data Examples

### 1. Minimal SMS (Required fields only)
```bash
curl -X POST http://localhost:8083/ExtMTPush/extmtpush \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode": "66399",
    "custid": "CUST001",
    "rmsisdn": "60123456789",
    "mtid": "MSG_'$(date +%s)'",
    "productType": 4,
    "dataEncoding": 0,
    "dnRep": 1
  }'
```

### 2. SMS with Message Text
```bash
curl -X POST http://localhost:8083/ExtMTPush/extmtpush \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode": "66399",
    "custid": "CUST001",
    "rmsisdn": "60123456789",
    "mtid": "MSG_'$(date +%s)'",
    "productType": 4,
    "dataEncoding": 0,
    "dataStr": "Hello! This is a test SMS message.",
    "dnRep": 1
  }'
```

### 3. SMS with Sender Name
```bash
curl -X POST http://localhost:8083/ExtMTPush/extmtpush \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode": "66399",
    "custid": "CUST001",
    "rmsisdn": "60123456789",
    "smsisdn": "MyCompany",
    "mtid": "MSG_'$(date +%s)'",
    "productType": 4,
    "dataEncoding": 0,
    "dataStr": "Welcome to MyCompany!",
    "dnRep": 1
  }'
```

### 4. Bulk SMS (Multiple Recipients)
```bash
curl -X POST http://localhost:8083/ExtMTPush/extmtpush \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode": "66399",
    "custid": "CUST001",
    "rmsisdn": "60123456789,60129876543,60187654321",
    "mtid": "MSG_'$(date +%s)'",
    "productType": 4,
    "dataEncoding": 0,
    "dataStr": "Bulk SMS to multiple numbers",
    "dnRep": 1
  }'
```

### 5. GET Request (URL parameters)
```bash
curl "http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60123456789&mtid=MSG_$(date +%s)&productType=4&dataEncoding=0&dnRep=1&dataStr=Test%20from%20GET"
```

---

## 📊 Expected Response

### Success Response:
```
MT Receive Result : returnCode = 0,messageID = MSG001,MSISDN = 60123456789,returnMsg = Success
 ------------- 
```

### Return Codes:
| Code | Meaning |
|------|---------|
| 0 | Success - SMS queued |
| 1 | Invalid parameters |
| 2 | IP not authorized |
| 3 | Duplicate MTID |
| 4 | Invalid destination |
| 5 | Insufficient credit |
| 6 | Blacklisted number |
| 7 | Invalid sender ID |
| 99 | Internal error |

---

## 🔍 Troubleshooting

### Problem: "IP not authorized"
**Solution:**
```bash
mysql -uroot -parun bulk_config -e "
INSERT INTO cp_ip (custid, ip_address, active) 
VALUES ('CUST001', '127.0.0.1', '1');
"
```

### Problem: "Insufficient credit"
**Solution:**
```bash
mysql -uroot -parun bulk_config -e "
UPDATE customer_credit 
SET credit_balance = 1000.00 
WHERE custid = 'CUST001';
"
```

### Problem: "Duplicate message ID"
**Solution:** Use unique MTID for each request:
```bash
# Add timestamp to make it unique
"mtid": "MSG_'$(date +%s)'"
```

---

## 📱 Test with Sample Data File

Use the provided sample data:

```bash
# Simple SMS
curl -X POST http://localhost:8083/ExtMTPush/extmtpush \
  -H "Content-Type: application/json" \
  -d @<(jq '.simple_sms' sample_sms_data.json)

# Promotional SMS
curl -X POST http://localhost:8083/ExtMTPush/extmtpush \
  -H "Content-Type: application/json" \
  -d @<(jq '.promotional_sms' sample_sms_data.json)
```

---

## 🎯 Full Example with all Fields

```bash
curl -X POST http://localhost:8083/ExtMTPush/extmtpush \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode": "66399",
    "custid": "CUST001",
    "rmsisdn": "60123456789",
    "smsisdn": "MyBrand",
    "mtid": "MSG_'$(date +%s)'",
    "mtprice": "0.10",
    "productCode": "PROMO001",
    "productType": 4,
    "keyword": "OFFER",
    "dataEncoding": 0,
    "dataStr": "Special offer! 50% off today only. Reply YES to subscribe.",
    "dataUrl": "http://example.com/promo",
    "urlTitle": "Shop Now",
    "dnRep": 1,
    "groupTag": "PROMO_DEC2024",
    "ewigFlag": "0",
    "cFlag": "1"
  }'
```

---

## 📖 More Information

- **Full API Documentation**: See `REST_API_GUIDE.md`
- **Database Schema**: Check table definitions in `setup_test_data.sh`
- **Application Status**: http://localhost:8083/ExtMTPush/actuator/health

---

## ✅ Quick Commands Reference

```bash
# Setup database (one-time)
./setup_test_data.sh

# Run all tests
./test_sms_api.sh

# Send single SMS
curl -X POST http://localhost:8083/ExtMTPush/extmtpush \
  -H "Content-Type: application/json" \
  -d '{"shortcode":"66399","custid":"CUST001","rmsisdn":"60123456789","mtid":"MSG_'$(date +%s)'","productType":4,"dataEncoding":0,"dataStr":"Test","dnRep":1}'

# Check SMS records
mysql -uroot -parun extmt -e "SELECT * FROM extmtpush_receive_bulk ORDER BY received_date DESC LIMIT 5;"

# View logs
tail -f logs/extmtpush.log

# Check credit balance
mysql -uroot -parun bulk_config -e "SELECT * FROM customer_credit WHERE custid='CUST001';"
```

---

**Ready to start testing!** 🚀

Run `./setup_test_data.sh` first, then `./test_sms_api.sh` to see it in action!

