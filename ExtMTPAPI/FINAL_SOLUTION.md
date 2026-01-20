# 🎯 FINAL SOLUTION - ExtMTPush SMS API

## Current Status
- ✅ Application compiled successfully
- ✅ Database tables fixed
- ⚠️ Application needs restart to apply changes

## Quick Commands to Run

### 1. Restart Application (Run these commands in terminal)

```bash
# Stop old instance
pkill -f extmtpush-springboot

# Start new instance
cd /home/arun/Documents/rec/ExtMTPush-SpringBoot
java -jar target/extmtpush-springboot-1.0.0.jar &

# Wait for startup (15 seconds)
sleep 15

# Check if running
curl http://localhost:8083/ExtMTPush/actuator/health
```

### 2. Test SMS API

```bash
# Test with curl
curl -X POST "http://localhost:8083/ExtMTPush/extmtpush" \
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

**Expected Success Response:**
```
MT Receive Result : returnCode = 0,messageID = MSG_123456,MSISDN = 60123456789,returnMsg = Success
 ------------- 
```

### 3. Verify SMS in Database

```bash
mysql -uroot -parun extmt -e "
  SELECT mtid, custid, rmsisdn, data_str, received_date 
  FROM extmtpush_receive_bulk 
  ORDER BY received_date DESC 
  LIMIT 5;
"
```

---

## All Fixed Tables

### Database: bulk_config

#### 1. cpip (IP Authorization)
```sql
CREATE TABLE cpip (
    row_id INT AUTO_INCREMENT PRIMARY KEY,
    shortcode VARCHAR(10),
    cpidentity VARCHAR(50),
    cp_ip VARCHAR(50),
    hlr_flag CHAR(1) DEFAULT '1',
    active CHAR(1) DEFAULT '1',
    INDEX idx_shortcode_cpidentity_ip (shortcode, cpidentity, cp_ip)
);

INSERT INTO cpip (shortcode, cpidentity, cp_ip, hlr_flag, active) VALUES
('66399', 'CUST001', '127.0.0.1', '1', '1'),
('66399', 'CUST001', 'localhost', '1', '1'),
('66399', 'CUST001', '0:0:0:0:0:0:0:1', '1', '1'),
('66399', 'CUST001', '::1', '1', '1');
```

#### 2. customer_credit (Credit Balance)
```sql
CREATE TABLE customer_credit (
    row_id INT AUTO_INCREMENT PRIMARY KEY,
    custid VARCHAR(50) NOT NULL UNIQUE,
    credit_balance DECIMAL(10,2) DEFAULT 0.00,
    credit_limit DECIMAL(10,2),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active CHAR(1) DEFAULT '1',
    INDEX idx_custid (custid)
);

INSERT INTO customer_credit (custid, credit_balance, credit_limit, active)
VALUES ('CUST001', 1000.00, 10000.00, '1');
```

#### 3. bulk_destination_sms (Destination Validation)
```sql
CREATE TABLE bulk_destination_sms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    custid VARCHAR(50) NOT NULL,
    local_flag CHAR(1) DEFAULT '0',
    int_flag CHAR(1) DEFAULT '0',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_custid (custid)
);

INSERT INTO bulk_destination_sms (custid, local_flag, int_flag)
VALUES ('CUST001', '0', '0');
```

### Database: extmt

#### 4. extmtpush_receive_bulk (SMS Records)
```sql
CREATE TABLE extmtpush_receive_bulk (
    row_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bill_flag CHAR(1) DEFAULT '1',
    process_flag CHAR(1) DEFAULT 'P',
    shortcode VARCHAR(10),
    custid VARCHAR(50),
    rmsisdn VARCHAR(20),
    smsisdn VARCHAR(20),
    mtid VARCHAR(100),
    mtprice VARCHAR(10),
    product_type INT,
    product_code VARCHAR(50),
    keyword VARCHAR(255),
    data_encoding INT,
    data_str TEXT,
    data_url VARCHAR(500),
    url_title VARCHAR(255),
    dnrep INT,
    group_tag VARCHAR(50),
    ewig_flag CHAR(1),
    received_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sent_date TIMESTAMP NULL,
    delivery_status VARCHAR(20),
    INDEX idx_custid (custid),
    INDEX idx_mtid (mtid),
    INDEX idx_process_flag (process_flag),
    INDEX idx_received_date (received_date)
);
```

#### 5. extmt_mtid (MTID Tracking)
```sql
CREATE TABLE extmt_mtid (
    row_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mtid VARCHAR(100) NOT NULL UNIQUE,
    custid VARCHAR(50) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_mtid_custid (mtid, custid)
);
```

---

## Troubleshooting Return Codes

### returnCode = 0 ✅
**Success!** SMS has been saved to database.

### returnCode = 1 ❌
**Invalid parameters** - Check that all required fields are provided:
- shortcode
- custid
- rmsisdn
- mtid

### returnCode = 2 ❌
**IP not authorized**

**Fix:**
```bash
mysql -uroot -parun bulk_config -e "
SELECT shortcode, cpidentity, cp_ip, hlr_flag, active 
FROM cpip WHERE cpidentity='CUST001';
"
```
Should show 4 rows with hlr_flag='1'. If empty or wrong, run:
```bash
cd /home/arun/Documents/rec/ExtMTPush-SpringBoot
./setup_test_data.sh
```

### returnCode = 3 ❌
**Duplicate MTID** - Use unique message ID for each request

### returnCode = 4 ❌
**Destination validation failed**

**Fix:**
```bash
mysql -uroot -parun bulk_config -e "
SELECT custid, local_flag, int_flag 
FROM bulk_destination_sms WHERE custid='CUST001';
"
```
Should show local_flag='0' and int_flag='0'. If missing:
```bash
mysql -uroot -parun bulk_config -e "
INSERT INTO bulk_destination_sms (custid, local_flag, int_flag)
VALUES ('CUST001', '0', '0');
"
```

### returnCode = 5 ❌
**Insufficient credit**

**Check credit:**
```bash
mysql -uroot -parun bulk_config -e "
DESCRIBE customer_credit;
SELECT row_id, custid, credit_balance, active 
FROM customer_credit WHERE custid='CUST001';
"
```

**If table has 'id' instead of 'row_id' or balance is 0:**
```bash
mysql -uroot -parun bulk_config << 'EOF'
DROP TABLE IF EXISTS customer_credit;
CREATE TABLE customer_credit (
    row_id INT AUTO_INCREMENT PRIMARY KEY,
    custid VARCHAR(50) NOT NULL UNIQUE,
    credit_balance DECIMAL(10,2) DEFAULT 0.00,
    credit_limit DECIMAL(10,2),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active CHAR(1) DEFAULT '1',
    INDEX idx_custid (custid)
);
INSERT INTO customer_credit (custid, credit_balance, credit_limit, active)
VALUES ('CUST001', 1000.00, 10000.00, '1');
EOF

# RESTART APPLICATION after this!
pkill -f extmtpush-springboot
cd /home/arun/Documents/rec/ExtMTPush-SpringBoot
java -jar target/extmtpush-springboot-1.0.0.jar &
sleep 15
```

---

## Test Scenarios

### 1. Simple SMS
```bash
curl -X POST http://localhost:8083/ExtMTPush/extmtpush \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode":"66399",
    "custid":"CUST001",
    "rmsisdn":"60123456789",
    "mtid":"MSG_'$(date +%s)'",
    "productType":4,
    "dataEncoding":0,
    "dataStr":"Hello!",
    "dnRep":1
  }'
```

### 2. SMS with Sender Name
```bash
curl -X POST http://localhost:8083/ExtMTPush/extmtpush \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode":"66399",
    "custid":"CUST001",
    "rmsisdn":"60123456789",
    "smsisdn":"MyCompany",
    "mtid":"MSG_'$(date +%s)'",
    "productType":4,
    "dataEncoding":0,
    "dataStr":"Welcome!",
    "dnRep":1
  }'
```

### 3. Bulk SMS
```bash
curl -X POST http://localhost:8083/ExtMTPush/extmtpush \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode":"66399",
    "custid":"CUST001",
    "rmsisdn":"60123456789,60129876543",
    "mtid":"MSG_'$(date +%s)'",
    "productType":4,
    "dataEncoding":0,
    "dataStr":"Bulk SMS",
    "dnRep":1
  }'
```

### 4. GET Request (URL Parameters)
```bash
curl "http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60123456789&mtid=MSG_$(date +%s)&productType=4&dataEncoding=0&dnRep=1&dataStr=Test"
```

---

## Check Application Logs

```bash
# View recent logs
tail -50 logs/extmtpush.log

# Follow logs in real-time
tail -f logs/extmtpush.log

# Search for errors
tail -200 logs/extmtpush.log | grep -i error

# Search for credit issues
tail -200 logs/extmtpush.log | grep -i credit
```

---

## Complete Setup Script

If tables are still incorrect, run this complete setup:

```bash
#!/bin/bash
mysql -uroot -parun << 'EOSQL'

-- Database: bulk_config
USE bulk_config;

DROP TABLE IF EXISTS cpip;
CREATE TABLE cpip (
    row_id INT AUTO_INCREMENT PRIMARY KEY,
    shortcode VARCHAR(10),
    cpidentity VARCHAR(50),
    cp_ip VARCHAR(50),
    hlr_flag CHAR(1) DEFAULT '1',
    active CHAR(1) DEFAULT '1',
    INDEX idx_shortcode_cpidentity_ip (shortcode, cpidentity, cp_ip)
);

INSERT INTO cpip (shortcode, cpidentity, cp_ip, hlr_flag, active) VALUES
('66399', 'CUST001', '127.0.0.1', '1', '1'),
('66399', 'CUST001', 'localhost', '1', '1'),
('66399', 'CUST001', '0:0:0:0:0:0:0:1', '1', '1'),
('66399', 'CUST001', '::1', '1', '1');

DROP TABLE IF EXISTS customer_credit;
CREATE TABLE customer_credit (
    row_id INT AUTO_INCREMENT PRIMARY KEY,
    custid VARCHAR(50) NOT NULL UNIQUE,
    credit_balance DECIMAL(10,2) DEFAULT 0.00,
    credit_limit DECIMAL(10,2),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active CHAR(1) DEFAULT '1',
    INDEX idx_custid (custid)
);

INSERT INTO customer_credit (custid, credit_balance, credit_limit, active)
VALUES ('CUST001', 1000.00, 10000.00, '1');

DROP TABLE IF EXISTS bulk_destination_sms;
CREATE TABLE bulk_destination_sms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    custid VARCHAR(50) NOT NULL,
    local_flag CHAR(1) DEFAULT '0',
    int_flag CHAR(1) DEFAULT '0',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_custid (custid)
);

INSERT INTO bulk_destination_sms (custid, local_flag, int_flag)
VALUES ('CUST001', '0', '0');

-- Database: extmt
USE extmt;

DROP TABLE IF EXISTS extmt_mtid;
CREATE TABLE extmt_mtid (
    row_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mtid VARCHAR(100) NOT NULL UNIQUE,
    custid VARCHAR(50) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_mtid_custid (mtid, custid)
);

CREATE TABLE IF NOT EXISTS extmtpush_receive_bulk (
    row_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bill_flag CHAR(1) DEFAULT '1',
    process_flag CHAR(1) DEFAULT 'P',
    shortcode VARCHAR(10),
    custid VARCHAR(50),
    rmsisdn VARCHAR(20),
    smsisdn VARCHAR(20),
    mtid VARCHAR(100),
    mtprice VARCHAR(10),
    product_type INT,
    product_code VARCHAR(50),
    keyword VARCHAR(255),
    data_encoding INT,
    data_str TEXT,
    data_url VARCHAR(500),
    url_title VARCHAR(255),
    dnrep INT,
    group_tag VARCHAR(50),
    ewig_flag CHAR(1),
    received_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sent_date TIMESTAMP NULL,
    delivery_status VARCHAR(20),
    INDEX idx_custid (custid),
    INDEX idx_mtid (mtid),
    INDEX idx_process_flag (process_flag),
    INDEX idx_received_date (received_date)
);

SELECT 'All tables created successfully!' as '';

EOSQL

echo "✅ Database setup complete!"
echo "Now restart the application:"
echo "  pkill -f extmtpush-springboot"
echo "  cd /home/arun/Documents/rec/ExtMTPush-SpringBoot"
echo "  java -jar target/extmtpush-springboot-1.0.0.jar &"
```

---

## Your API Endpoint

**Base URL:** `http://localhost:8083/ExtMTPush`  
**SMS Endpoint:** `/extmtpush`  
**Full URL:** `http://localhost:8083/ExtMTPush/extmtpush`

**Methods:** POST, GET  
**Content-Type:** application/json (for POST)

---

## Next Steps

1. **Restart application** (see commands at top)
2. **Test SMS API** (see test commands)
3. **Check logs** if errors occur
4. **Verify database** if returnCode != 0

---

**Created:** December 17, 2025  
**Status:** Ready to test after restart  
**Documentation:** All guide files in project root

