# Quick API Test Commands

## Before Testing
Ensure application is running:
```bash
cd /home/arun/Documents/rec/ExtMTPush-SpringBoot
ps aux | grep "java.*extmtpush" | grep -v grep

# If not running, start it:
java -jar target/extmtpush-springboot-1.0.0.jar &
sleep 20
```

## Test Commands (Copy & Paste)

### Test 1: Single Mobile Number
```bash
curl "http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60192782366&smsisdn=62003&mtid=MSG_$(date +%s)_1&mtprice=000&productCode=&productType=4&keyword=&dataEncoding=0&dataStr=Test%20message%20single&dataUrl=&dnRep=0&groupTag=1"
```

Expected: `returnCode = 0`

---

### Test 2: Multiple Mobile Numbers  
```bash
curl "http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60192782366,60122786404,60122879404&smsisdn=62003&mtid=MSG_$(date +%s)_2&mtprice=000&productCode=&productType=4&keyword=&dataEncoding=0&dataStr=Test%20message%20multiple&dataUrl=&dnRep=0&groupTag=10"
```

Expected: `returnCode = 0`

---

### Test 3: Chinese Text (Unicode)
```bash
curl "http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60192782366&smsisdn=62003&mtid=MSG_$(date +%s)_3&mtprice=000&productCode=&productType=10&keyword=&dataEncoding=8&dataStr=002854094e509a6c00297b2c0032573aff1a0038002900207a7a4e2d4f208bf4002000200053006b00790020004c006500670065006e0064002000209ad85ea663a88350002a002a002a002000370029002091d182729ece660e00200047006f006c00640065006e0020004c0061006b00730068006d00690020&dataUrl=&dnRep=0&groupTag=10"
```

Expected: `returnCode = 0`

---

### Test 4: Check Credit Balance
```bash
curl "http://localhost:8083/ExtMTPush/CheckSMSUserCredit?custid=CUST001"
```

Expected: `Customer: CUST001, Balance: 1000.00, Has Credit: YES`

---

## Verify in Database
```bash
mysql -uroot -parun extmt -e "SELECT mtid, rmsisdn, LEFT(data_str, 50) as message, received_date FROM extmtpush_receive_bulk WHERE custid='CUST001' ORDER BY received_date DESC LIMIT 5;" 2>&1 | grep -v Warning
```

---

## Automated Test
```bash
cd /home/arun/Documents/rec/ExtMTPush-SpringBoot
./start_and_test_apis.sh
```

---

## Troubleshooting

### If you get "Connection refused":
```bash
# Start the application
cd /home/arun/Documents/rec/ExtMTPush-SpringBoot
java -jar target/extmtpush-springboot-1.0.0.jar &
sleep 20

# Check if it started
curl -s http://localhost:8083/ExtMTPush/actuator/health
```

### If you get returnCode = 2 (IP not authorized):
```bash
./setup_test_data.sh
```

### If you get returnCode = 5 (Insufficient credit):
```bash
mysql -uroot -parun bulk_config -e "UPDATE customer_credit SET credit_balance = 10000.00, active = '1' WHERE custid = 'CUST001';"
```

### Check logs:
```bash
tail -50 logs/test.log | grep -E "(ERROR|Exception|Started)"
```

