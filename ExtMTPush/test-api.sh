#!/bin/bash

# ExtMTPush API Test Script
# Usage: ./test-api.sh [base_url]

BASE_URL="${1:-http://localhost:8087}"

echo "=============================================="
echo "   ExtMTPush API Test Suite"
echo "   Base URL: $BASE_URL"
echo "=============================================="
echo ""

# Color codes
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test counter
TOTAL=0
PASSED=0
FAILED=0

# Function to run test
run_test() {
    local name=$1
    local method=$2
    local endpoint=$3
    local data=$4

    TOTAL=$((TOTAL + 1))
    echo -n "[$TOTAL] Testing: $name... "

    if [ "$method" == "GET" ]; then
        response=$(curl -s -w "\n%{http_code}" "$BASE_URL$endpoint")
    elif [ "$method" == "POST" ] && [ -n "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X POST -H "Content-Type: application/json" -d "$data" "$BASE_URL$endpoint")
    else
        response=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL$endpoint")
    fi

    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')

    if [ "$http_code" -ge 200 ] && [ "$http_code" -lt 300 ]; then
        echo -e "${GREEN}PASSED${NC} (HTTP $http_code)"
        PASSED=$((PASSED + 1))
    else
        echo -e "${RED}FAILED${NC} (HTTP $http_code)"
        echo "  Response: $body"
        FAILED=$((FAILED + 1))
    fi
}

echo "========== Health Check =========="
run_test "Health Check" "GET" "/api/sms/health"
echo ""

echo "========== SMS APIs =========="
run_test "Get User Credit" "GET" "/api/sms/credit/TEST001"
run_test "Check Delivery Status" "GET" "/api/sms/status/MSG001"

SMS_REQUEST='{
    "shortcode": "12345",
    "custid": "TEST001",
    "rmsisdn": "60123456789",
    "smsisdn": "SENDER",
    "mtid": "MSG'$(date +%s)'",
    "price": "000",
    "dataStr": "Test message from API test script",
    "keyword": "TEST"
}'
run_test "Send SMS" "POST" "/api/sms/send" "$SMS_REQUEST"

BULK_REQUEST='{
    "billFlag": "1",
    "shortcode": "12345",
    "custid": "TEST001",
    "rmsisdn": "60123456789",
    "smsisdn": "SENDER",
    "mtid": "BULK'$(date +%s)'",
    "mtprice": "000",
    "productType": 4,
    "dataEncoding": 0,
    "dataStr": "Test bulk message",
    "dnrep": 1
}'
run_test "Send Bulk SMS" "POST" "/api/sms/bulk/send" "$BULK_REQUEST"

run_test "Deduct Credit" "POST" "/api/sms/credit/deduct?custid=TEST001&amount=1"
echo ""

echo "========== HLR APIs =========="
run_test "HLR Lookup" "GET" "/api/hlr/lookup/60123456789"
run_test "Get Network Operator (Maxis)" "GET" "/api/hlr/network/60123456789"
run_test "Get Network Operator (Digi)" "GET" "/api/hlr/network/60163456789"
run_test "Get Network Operator (Celcom)" "GET" "/api/hlr/network/60193456789"
run_test "Validate MSISDN" "GET" "/api/hlr/validate/60123456789"

BATCH_REQUEST='["60123456789", "60163456789", "60173456789"]'
run_test "Batch HLR Lookup" "POST" "/api/hlr/batch" "$BATCH_REQUEST"
echo ""

echo "========== Delivery Notification APIs =========="
run_test "Celcom DN (GET)" "GET" "/dn/celcom?SMP_Txid=TX$(date +%s)&SUB_Mobtel=60133456789&ErrorCode=DeliveredToTerminal&DNStatus=DELIVERED"
run_test "Celcom DN (POST)" "POST" "/dn/celcom?SMP_Txid=TX$(date +%s)&SUB_Mobtel=60133456789&ErrorCode=DeliveredToTerminal"
run_test "Digi DN" "POST" "/dn/digi?messageId=MSG$(date +%s)&status=DELIVERED&msisdn=60163456789&errorCode=0"
run_test "Maxis DN" "POST" "/dn/maxis?messageId=MSG$(date +%s)&status=DELIVRD&msisdn=60173456789&errorCode=0"
run_test "SilverStreet DN" "POST" "/dn/silverstreet?messageId=MSG$(date +%s)&status=1&msisdn=60123456789&errorCode=0"
run_test "Generic DN (UMobile)" "GET" "/dn/umobile?messageId=MSG$(date +%s)&status=DELIVERED&msisdn=60183456789"
echo ""

echo "========== Test API Endpoints =========="
run_test "API List" "GET" "/api/test"
run_test "Test SMS Send" "GET" "/api/test/sms/send"
run_test "Test HLR Lookup" "GET" "/api/test/hlr/60123456789"
run_test "Test Celcom DN" "GET" "/api/test/dn/celcom"
run_test "Run All Tests" "GET" "/api/test/all"
echo ""

echo "=============================================="
echo "   Test Summary"
echo "=============================================="
echo -e "Total Tests: $TOTAL"
echo -e "Passed:      ${GREEN}$PASSED${NC}"
echo -e "Failed:      ${RED}$FAILED${NC}"
echo ""

if [ $FAILED -eq 0 ]; then
    echo -e "${GREEN}All tests passed!${NC}"
    exit 0
else
    echo -e "${RED}Some tests failed!${NC}"
    exit 1
fi

