# 📱 ExtMTPush REST API - Files Summary

## ✅ All Files Created Successfully!

I've created comprehensive documentation and testing tools for inserting data into ExtMTPush via REST API.

---

## 📚 Documentation Files (24 KB total)

### 1. **QUICK_START.md** (6.1 KB)
   - Quick reference guide
   - Step-by-step instructions
   - Common examples
   - Troubleshooting

### 2. **REST_API_GUIDE.md** (12 KB) ⭐ **Most Complete**
   - Full API documentation
   - All parameters explained  
   - Examples in multiple languages:
     - curl
     - Python
     - Node.js
     - PHP
   - Response codes reference
   - Product types reference
   - Testing checklist

---

## 🧪 Testing Tools

### 3. **test_sms_api.sh** (3.3 KB) ⭐ **Automated Testing**
   ```bash
   ./test_sms_api.sh
   ```
   - Checks if application is running
   - Runs 4 different test scenarios:
     - Simple GET request
     - POST with JSON
     - Multiple recipients (bulk)
     - Full SMS with all parameters
   - Color-coded output
   - Shows results immediately

### 4. **setup_test_data.sh** (4.7 KB) ⭐ **Database Setup**
   ```bash
   ./setup_test_data.sh
   ```
   - Creates required database tables:
     - `extmtpush_receive_bulk` (SMS records)
     - `extmt_id` (MTID tracking)
     - `cp_ip` (IP authorization)
     - `customer_credit` (Credit balance)
     - `cp_package` (Package config)
   - Adds test customer: **CUST001**
   - Sets credit balance: **1000.00**
   - Authorizes localhost IPs
   - Configures shortcode: **66399**

---

## 📋 Sample Data Files

### 5. **sample_sms_data.json** (1.5 KB)
   - 5 pre-configured SMS scenarios:
     - Simple SMS
     - SMS with sender
     - Promotional SMS
     - Bulk SMS
     - WAP push
   - Ready to use with curl

### 6. **ExtMTPush_Postman_Collection.json** (6.7 KB)
   - Import into Postman
   - 7 pre-configured requests:
     - Health check
     - Simple SMS (POST)
     - SMS with sender
     - Bulk SMS
     - Promotional SMS
     - Simple SMS (GET)
     - OTP SMS example
   - Auto-generates unique message IDs with {{$timestamp}}

---

## 🚀 Getting Started (3 Steps)

### Step 1: Setup Database (One-time)
```bash
cd /home/arun/Documents/rec/ExtMTPush-SpringBoot
./setup_test_data.sh
```

**This creates:**
- ✅ All required tables
- ✅ Test customer (CUST001) with 1000 credit
- ✅ IP authorization for localhost
- ✅ Shortcode configuration (66399)

### Step 2: Run Tests
```bash
./test_sms_api.sh
```

**This will:**
- ✅ Verify application is running
- ✅ Send 4 different test SMS
- ✅ Show results with color coding
- ✅ Display response codes

### Step 3: Verify Data
```bash
mysql -uroot -parun extmt -e "
  SELECT mtid, custid, rmsisdn, data_str, received_date 
  FROM extmtpush_receive_bulk 
  ORDER BY received_date DESC 
  LIMIT 5;
"
```

---

## 📝 Quick Copy-Paste Examples

### Simple SMS
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
    "dataStr": "Hello World!",
    "dnRep": 1
  }'
```

### SMS with Sender Name
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
    "dataStr": "Welcome to our service!",
    "dnRep": 1
  }'
```

### Bulk SMS (Multiple Recipients)
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
    "dataStr": "Bulk SMS to all",
    "dnRep": 1
  }'
```

---

## 🎯 API Endpoint Details

### URL
```
http://localhost:8083/ExtMTPush/extmtpush
```

### Methods
- **POST** (recommended) - JSON body
- **GET** (backward compatibility) - URL parameters

### Content-Type
```
application/json
```

### Required Fields
| Field | Type | Example |
|-------|------|---------|
| shortcode | String | "66399" |
| custid | String | "CUST001" |
| rmsisdn | String | "60123456789" |
| mtid | String | "MSG001" |
| productType | Integer | 4 |
| dataEncoding | Integer | 0 |
| dnRep | Integer | 1 |

### Optional Fields
- **smsisdn**: Sender name/ID
- **dataStr**: SMS message text
- **mtprice**: Message price
- **productCode**: Product code
- **keyword**: SMS keyword
- **dataUrl**: Content URL
- **urlTitle**: URL title
- **groupTag**: Campaign tag
- **ewigFlag**: EWIG flag
- **cFlag**: Custom flag

---

## 📊 Response Format

### Success
```
MT Receive Result : returnCode = 0,messageID = MSG001,MSISDN = 60123456789,returnMsg = Success
 ------------- 
```

### Error
```
MT Receive Result : returnCode = 2,messageID = ,MSISDN = ,returnMsg = IP not authorized
 ------------- 
```

### Return Codes
| Code | Status | Description |
|------|--------|-------------|
| 0 | ✅ Success | SMS queued successfully |
| 1 | ❌ Error | Invalid parameters |
| 2 | ❌ Error | IP not authorized |
| 3 | ❌ Error | Duplicate message ID |
| 4 | ❌ Error | Invalid destination |
| 5 | ❌ Error | Insufficient credit |
| 6 | ❌ Error | Blacklisted number |
| 7 | ❌ Error | Invalid masking ID |
| 99 | ❌ Error | Internal error |

---

## 🔍 Database Tables

### SMS Records Table
```sql
extmt.extmtpush_receive_bulk
```
Stores all SMS records with fields:
- row_id, mtid, custid, rmsisdn, smsisdn
- data_str, product_type, received_date, etc.

### MTID Tracking
```sql
extmt.extmt_id
```
Tracks message IDs to prevent duplicates

### IP Authorization
```sql
bulk_config.cp_ip  
```
Controls which IPs can send SMS

### Customer Credit
```sql
bulk_config.customer_credit
```
Manages customer credit balances

---

## 🛠️ Troubleshooting

### Error: "IP not authorized"
**Solution**: Run setup script or manually add IP:
```bash
mysql -uroot -parun bulk_config -e "
  INSERT INTO cp_ip (custid, ip_address, active) 
  VALUES ('CUST001', '127.0.0.1', '1');
"
```

### Error: "Insufficient credit"
**Solution**: Add credit:
```bash
mysql -uroot -parun bulk_config -e "
  UPDATE customer_credit 
  SET credit_balance = 1000.00 
  WHERE custid = 'CUST001';
"
```

### Error: "Duplicate message ID"
**Solution**: Use unique MTID with timestamp:
```json
"mtid": "MSG_1734345600"
```

### Application not responding
**Solution**: Check if running:
```bash
curl http://localhost:8083/ExtMTPush/actuator/health
```

---

## 📦 Product Types Reference

```
0  = Remix Ringtone
1  = Ringtone
2  = Logo
3  = Picture
4  = Text SMS (Most common)
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

## 💡 Pro Tips

### Generate Unique MTID
```bash
# Using timestamp
"mtid": "MSG_$(date +%s)"

# Using UUID (if available)
"mtid": "MSG_$(uuidgen | cut -d'-' -f1)"
```

### Check Latest SMS
```bash
mysql -uroot -parun extmt -e "
  SELECT * FROM extmtpush_receive_bulk 
  ORDER BY received_date DESC LIMIT 1\G
"
```

### Monitor Logs in Real-time
```bash
tail -f logs/extmtpush.log | grep -E "(SMS|returnCode)"
```

### Test with Different Numbers
```bash
# Malaysian carriers
60123456789  # Celcom (6019, 60148)
60167890123  # Digi (6016, 60146, 60143)
60127890123  # Maxis (6012, 60142, 6017, etc)
60187890123  # U Mobile (6018, 60183)
```

---

## 🎯 Use Cases

### 1. OTP/Verification Codes
```json
{
  "shortcode": "66399",
  "custid": "CUST001",
  "rmsisdn": "60123456789",
  "smsisdn": "OTP-Service",
  "mtid": "OTP_1734345600",
  "productType": 4,
  "dataEncoding": 0,
  "dataStr": "Your OTP is 123456. Valid for 5 minutes.",
  "dnRep": 1
}
```

### 2. Promotional Campaigns
```json
{
  "shortcode": "66399",
  "custid": "CUST001",
  "rmsisdn": "60123456789,60129876543",
  "smsisdn": "Promo2024",
  "mtid": "PROMO_1734345600",
  "productType": 4,
  "dataEncoding": 0,
  "dataStr": "Flash Sale! 50% off. Shop now!",
  "groupTag": "DEC_PROMO",
  "dnRep": 1
}
```

### 3. Transactional SMS
```json
{
  "shortcode": "66399",
  "custid": "CUST001",
  "rmsisdn": "60123456789",
  "smsisdn": "MyBank",
  "mtid": "TXN_1734345600",
  "productType": 4,
  "dataEncoding": 0,
  "dataStr": "Your account was debited RM50.00. Balance: RM1000.00",
  "dnRep": 1
}
```

---

## 📚 Additional Resources

- **Application Status**: http://localhost:8083/ExtMTPush/actuator/health
- **SOAP WSDL**: http://localhost:8083/ExtMTPush/services/externalMTPush.wsdl
- **Logs**: `logs/extmtpush.log`
- **SMS Traffic Logs**: `logs/sms-traffic.log`

---

## ✅ Checklist

Before testing, ensure:
- [ ] Application is running
- [ ] MySQL databases are accessible  
- [ ] Setup script has been run (`./setup_test_data.sh`)
- [ ] Test customer exists with credit
- [ ] IP authorization is configured

---

**🎉 You're all set to start inserting SMS data via REST API!**

**Quick Start:**
```bash
# 1. Setup (one-time)
./setup_test_data.sh

# 2. Test
./test_sms_api.sh

# 3. Verify
mysql -uroot -parun extmt -e "SELECT * FROM extmtpush_receive_bulk ORDER BY received_date DESC LIMIT 5;"
```

Need more details? Check:
- **QUICK_START.md** for quick reference
- **REST_API_GUIDE.md** for complete documentation
- **Use Postman collection** for GUI testing

---

**Created**: December 16, 2025  
**Location**: `/home/arun/Documents/rec/ExtMTPush-SpringBoot/`

