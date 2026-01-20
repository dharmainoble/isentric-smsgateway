#!/bin/bash

# API Testing Script for ExtMTPush
BASE_URL="http://localhost:8083/ExtMTPush"

echo "============================================"
echo "ExtMTPush API Testing Suite"
echo "============================================"
echo "Base URL: $BASE_URL"
echo ""

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to test API
test_api() {
    local test_name="$1"
    local url="$2"
    local expected_keyword="$3"

    echo "-------------------------------------------"
    echo "Test: $test_name"
    echo "-------------------------------------------"
    echo "URL: $url"
    echo ""

    # Make request
    response=$(curl -s "$url" 2>&1)

    echo "Response:"
    echo "$response"
    echo ""

    # Check for success
    if echo "$response" | grep -q "$expected_keyword"; then
        echo -e "${GREEN}✓ Test PASSED${NC}"
    else
        echo -e "${RED}✗ Test FAILED${NC}"
    fi
    echo ""
}

# Wait for application to be ready
echo "Checking if application is ready..."
MAX_ATTEMPTS=30
ATTEMPT=0

while [ $ATTEMPT -lt $MAX_ATTEMPTS ]; do
    if curl -s "$BASE_URL/actuator/health" | grep -q "UP"; then
        echo -e "${GREEN}✓ Application is ready${NC}"
        echo ""
        break
    fi
    ATTEMPT=$((ATTEMPT + 1))
    if [ $ATTEMPT -eq $MAX_ATTEMPTS ]; then
        echo -e "${RED}✗ Application not ready after 30 attempts${NC}"
        echo "Please start the application manually:"
        echo "  cd /home/arun/Documents/rec/ExtMTPush-SpringBoot"
        echo "  java -jar target/extmtpush-springboot-1.0.0.jar &"
        exit 1
    fi
    sleep 1
done

# Test 1: Send text message to single mobile number
test_api \
    "API 1: Single Mobile Number" \
    "${BASE_URL}/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60192782366&smsisdn=62003&mtid=MSG_$(date +%s)_1&mtprice=000&productCode=&productType=4&keyword=&dataEncoding=0&dataStr=Test%20message%20single&dataUrl=&dnRep=0&groupTag=1" \
    "returnCode"

# Test 2: Send text message to multiple mobile numbers
test_api \
    "API 2: Multiple Mobile Numbers" \
    "${BASE_URL}/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60192782366,60122786404,60122879404&smsisdn=62003&mtid=MSG_$(date +%s)_2&mtprice=000&productCode=&productType=4&keyword=&dataEncoding=0&dataStr=Test%20message%20multiple&dataUrl=&dnRep=0&groupTag=10" \
    "returnCode"

# Test 3: Send Chinese text (Unicode)
test_api \
    "API 3: Chinese Text (Unicode)" \
    "${BASE_URL}/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60192782366&smsisdn=62003&mtid=MSG_$(date +%s)_3&mtprice=000&productCode=&productType=10&keyword=&dataEncoding=8&dataStr=002854094e509a6c00297b2c0032573aff1a0038002900207a7a4e2d4f208bf4002000200053006b00790020004c006500670065006e0064002000209ad85ea663a88350002a002a002a002000370029002091d182729ece660e00200047006f006c00640065006e0020004c0061006b00730068006d00690020&dataUrl=&dnRep=0&groupTag=10" \
    "returnCode"

# Test 4: Check Credit Balance
test_api \
    "API 4: Check Credit Balance" \
    "${BASE_URL}/CheckSMSUserCredit?custid=CUST001" \
    "creditBalance"

echo "============================================"
echo "Summary"
echo "============================================"
echo ""

# Check database for inserted records
echo "Checking database for SMS records..."
DB_COUNT=$(mysql -uroot -parun extmt -sN -e "SELECT COUNT(*) FROM extmtpush_receive_bulk WHERE custid='CUST001';" 2>&1 | grep -v Warning)
echo "Total SMS records for CUST001: $DB_COUNT"
echo ""

if [ "$DB_COUNT" -gt 0 ]; then
    echo "Recent SMS records:"
    mysql -uroot -parun extmt -e "
      SELECT mtid, rmsisdn, LEFT(data_str, 30) as message, received_date
      FROM extmtpush_receive_bulk
      WHERE custid='CUST001'
      ORDER BY received_date DESC
      LIMIT 5;" 2>&1 | grep -v Warning
fi

echo ""
echo "============================================"
echo "Testing Complete!"
echo "============================================"
echo ""
echo "To test manually:"
echo "  Single Number:  curl '${BASE_URL}/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60192782366&...'"
echo "  Check Credit:   curl '${BASE_URL}/CheckSMSUserCredit?custid=CUST001'"
echo ""

