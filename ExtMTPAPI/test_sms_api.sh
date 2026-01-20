#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

BASE_URL="http://localhost:8083/ExtMTPush/extmtpush"
TIMESTAMP=$(date +%s)

echo -e "${GREEN}======================================${NC}"
echo -e "${GREEN}ExtMTPush SMS API - Quick Test${NC}"
echo -e "${GREEN}======================================${NC}"
echo ""

# Check if application is running
echo -e "${YELLOW}Checking if application is running...${NC}"
if curl -s http://localhost:8083/ExtMTPush/actuator/health > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Application is UP${NC}"
else
    echo -e "${RED}✗ Application is not running!${NC}"
    echo "Start the application first with: mvn spring-boot:run"
    exit 1
fi
echo ""

# Test 1: Simple GET request
echo -e "${YELLOW}Test 1: Simple SMS via GET${NC}"
echo "-----------------------------------"
RESPONSE1=$(curl -s -X GET "${BASE_URL}?shortcode=66399&custid=CUST001&rmsisdn=60123456789&mtid=MSG${TIMESTAMP}_GET&productType=4&dataEncoding=0&dnRep=1&dataStr=Test%20from%20GET")
echo "$RESPONSE1"
echo ""

# Test 2: POST with JSON
echo -e "${YELLOW}Test 2: SMS via POST (JSON)${NC}"
echo "-----------------------------------"
RESPONSE2=$(curl -s -X POST "${BASE_URL}" \
  -H "Content-Type: application/json" \
  -d "{
    \"shortcode\": \"66399\",
    \"custid\": \"CUST001\",
    \"rmsisdn\": \"60123456789\",
    \"mtid\": \"MSG${TIMESTAMP}_POST\",
    \"smsisdn\": \"TestSender\",
    \"productType\": 4,
    \"dataEncoding\": 0,
    \"dataStr\": \"Hello from POST request\",
    \"dnRep\": 1
  }")
echo "$RESPONSE2"
echo ""

# Test 3: Multiple recipients
echo -e "${YELLOW}Test 3: Bulk SMS (Multiple Recipients)${NC}"
echo "-----------------------------------"
RESPONSE3=$(curl -s -X POST "${BASE_URL}" \
  -H "Content-Type: application/json" \
  -d "{
    \"shortcode\": \"66399\",
    \"custid\": \"CUST001\",
    \"rmsisdn\": \"60123456789,60129876543\",
    \"mtid\": \"MSG${TIMESTAMP}_BULK\",
    \"productType\": 4,
    \"dataEncoding\": 0,
    \"dataStr\": \"Bulk SMS test\",
    \"dnRep\": 1
  }")
echo "$RESPONSE3"
echo ""

# Test 4: SMS with all parameters
echo -e "${YELLOW}Test 4: Full SMS with all parameters${NC}"
echo "-----------------------------------"
RESPONSE4=$(curl -s -X POST "${BASE_URL}" \
  -H "Content-Type: application/json" \
  -d "{
    \"shortcode\": \"66399\",
    \"custid\": \"CUST001\",
    \"rmsisdn\": \"60123456789\",
    \"smsisdn\": \"MyCompany\",
    \"mtid\": \"MSG${TIMESTAMP}_FULL\",
    \"mtprice\": \"0.10\",
    \"productCode\": \"PROD123\",
    \"productType\": 4,
    \"keyword\": \"INFO\",
    \"dataEncoding\": 0,
    \"dataStr\": \"Complete SMS with all fields\",
    \"dataUrl\": \"http://example.com\",
    \"urlTitle\": \"Visit Us\",
    \"dnRep\": 1,
    \"groupTag\": \"TEST_CAMPAIGN\",
    \"ewigFlag\": \"0\",
    \"cFlag\": \"1\"
  }")
echo "$RESPONSE4"
echo ""

echo -e "${GREEN}======================================${NC}"
echo -e "${GREEN}Testing Complete!${NC}"
echo -e "${GREEN}======================================${NC}"
echo ""
echo "To check the database records, run:"
echo "mysql -uroot -parun -e 'USE extmt; SELECT row_id, mtid, rmsisdn, data_str, received_date FROM extmtpush_receive_bulk ORDER BY received_date DESC LIMIT 5;'"
echo ""
echo "To view logs:"
echo "tail -f logs/extmtpush.log"

