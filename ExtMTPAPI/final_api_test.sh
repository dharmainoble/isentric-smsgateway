#!/bin/bash

echo "=========================================="
echo "Final API Testing - All 4 APIs"
echo "=========================================="
echo ""

BASE_URL="http://localhost:8083/ExtMTPush"
DB_USER="root"
DB_PASS="arun"

# Clear old test data
echo "1. Clearing old test data..."
mysql -u$DB_USER -p$DB_PASS extmt -e "DELETE FROM extmtpush_receive_bulk WHERE custid='CUST001';" 2>&1 | grep -v Warning
mysql -u$DB_USER -p$DB_PASS extmt -e "DELETE FROM extmt_mtid WHERE custid='CUST001';" 2>&1 | grep -v Warning
echo "   ✓ Old data cleared"
echo ""

# Wait for app to be ready
echo "2. Waiting for application..."
MAX_WAIT=30
COUNT=0
while [ $COUNT -lt $MAX_WAIT ]; do
    if curl -s "$BASE_URL/actuator/health" 2>&1 | grep -q "UP"; then
        echo "   ✓ Application is UP"
        break
    fi
    COUNT=$((COUNT + 1))
    sleep 1
done

if [ $COUNT -eq $MAX_WAIT ]; then
    echo "   ✗ Application not responding"
    exit 1
fi
echo ""

echo "3. Testing All 4 APIs..."
echo ""

# Test API 1 - Single SMS
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "API 1: Single SMS"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
RESPONSE1=$(curl -s -X POST "$BASE_URL/extmtpush" \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode": "66399",
    "custid": "CUST001",
    "rmsisdn": "60192782366",
    "smsisdn": "62003",
    "mtid": "TEST_API1_'$(date +%s)'",
    "productType": 4,
    "dataEncoding": 0,
    "dataStr": "API 1 - Single SMS Test",
    "dnRep": 1
  }')
echo "$RESPONSE1"
if echo "$RESPONSE1" | grep -q "returnCode = 0"; then
    echo "✅ API 1 PASSED"
else
    echo "❌ API 1 FAILED"
fi
echo ""

# Test API 2 - Multiple SMS
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "API 2: Multiple SMS (Bulk)"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
RESPONSE2=$(curl -s -X POST "$BASE_URL/extmtpush" \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode": "66399",
    "custid": "CUST001",
    "rmsisdn": "60192782366,60122786404,60122879404",
    "mtid": "TEST_API2_'$(date +%s)'",
    "productType": 4,
    "dataEncoding": 0,
    "dataStr": "API 2 - Bulk SMS Test",
    "dnRep": 0
  }')
echo "$RESPONSE2"
if echo "$RESPONSE2" | grep -q "returnCode = 0"; then
    echo "✅ API 2 PASSED"
else
    echo "❌ API 2 FAILED"
fi
echo ""

# Test API 3 - Unicode SMS
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "API 3: Unicode/Chinese SMS"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
RESPONSE3=$(curl -s -X POST "$BASE_URL/extmtpush" \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode": "66399",
    "custid": "CUST001",
    "rmsisdn": "60192782366",
    "smsisdn": "62003",
    "mtid": "TEST_API3_'$(date +%s)'",
    "productType": 10,
    "dataEncoding": 8,
    "dataStr": "4f60597d4e16754c",
    "dnRep": 0
  }')
echo "$RESPONSE3"
if echo "$RESPONSE3" | grep -q "returnCode = 0"; then
    echo "✅ API 3 PASSED"
else
    echo "❌ API 3 FAILED"
fi
echo ""

# Test API 4 - Credit Check
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "API 4: Check Credit Balance"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
RESPONSE4=$(curl -s "$BASE_URL/CheckSMSUserCredit?custid=CUST001")
echo "$RESPONSE4"
if echo "$RESPONSE4" | grep -q "Balance"; then
    echo "✅ API 4 PASSED"
else
    echo "❌ API 4 FAILED"
fi
echo ""

echo "=========================================="
echo "Database Verification"
echo "=========================================="
echo ""

# Check records in database
SMS_COUNT=$(mysql -u$DB_USER -p$DB_PASS extmt -sN -e "SELECT COUNT(*) FROM extmtpush_receive_bulk WHERE custid='CUST001';" 2>&1 | grep -v Warning | tail -1)
echo "Total SMS records inserted: $SMS_COUNT"
echo ""

if [ "$SMS_COUNT" -gt 0 ]; then
    echo "Recent SMS records:"
    mysql -u$DB_USER -p$DB_PASS extmt -e "
      SELECT
        mtid,
        rmsisdn,
        LEFT(data_str, 40) as message,
        product_type,
        received_date
      FROM extmtpush_receive_bulk
      WHERE custid='CUST001'
      ORDER BY received_date DESC
      LIMIT 10;" 2>&1 | grep -v Warning
fi

echo ""
echo "=========================================="
echo "Test Summary"
echo "=========================================="
echo ""

# Count results
PASS_COUNT=0
echo "$RESPONSE1" | grep -q "returnCode = 0" && PASS_COUNT=$((PASS_COUNT + 1))
echo "$RESPONSE2" | grep -q "returnCode = 0" && PASS_COUNT=$((PASS_COUNT + 1))
echo "$RESPONSE3" | grep -q "returnCode = 0" && PASS_COUNT=$((PASS_COUNT + 1))
echo "$RESPONSE4" | grep -q "Balance" && PASS_COUNT=$((PASS_COUNT + 1))

echo "Tests Passed: $PASS_COUNT / 4"
echo "SMS Records in DB: $SMS_COUNT"
echo ""

if [ "$PASS_COUNT" -eq 4 ] && [ "$SMS_COUNT" -gt 0 ]; then
    echo "🎉 SUCCESS! All APIs working and data inserted!"
    echo ""
    echo "✅ Postman collection is ready to use!"
    echo "   File: ExtMTPush_Complete_Postman_Collection.json"
else
    echo "⚠️  Some tests failed. Check responses above."
fi

echo ""
echo "=========================================="

