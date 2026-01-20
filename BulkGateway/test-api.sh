#!/bin/bash

# BulkGateway API Test Script
# Port: 9090
# Base URL: http://localhost:9090/api

BASE_URL="http://localhost:9090/api"

echo "=========================================="
echo "  BulkGateway API - Complete Test Suite"
echo "=========================================="
echo ""

echo "1. HEALTH CHECK"
echo "----------------"
curl -s "$BASE_URL/health" | python3 -m json.tool
echo ""

echo "2. GET ALL MESSAGES (BEFORE INSERT)"
echo "------------------------------------"
curl -s "$BASE_URL/messages" | python3 -m json.tool
echo ""

echo "3. INSERT SINGLE MESSAGE (POST)"
echo "--------------------------------"
curl -s -X POST "$BASE_URL/messages" \
  -H "Content-Type: application/json" \
  -d '{
    "sender": "test_user@domain.com",
    "recipient": "receiver@domain.com",
    "content": "Testing insert via REST API"
  }' | python3 -m json.tool
echo ""

echo "4. INSERT BULK MESSAGES (POST)"
echo "-------------------------------"
curl -s -X POST "$BASE_URL/messages/bulk" \
  -H "Content-Type: application/json" \
  -d '[
    {"sender": "bulk1@company.com", "recipient": "user1@test.com", "content": "Bulk message 1"},
    {"sender": "bulk2@company.com", "recipient": "user2@test.com", "content": "Bulk message 2"},
    {"sender": "bulk3@company.com", "recipient": "user3@test.com", "content": "Bulk message 3"}
  ]' | python3 -m json.tool
echo ""

echo "5. GET ALL MESSAGES (AFTER INSERT)"
echo "------------------------------------"
curl -s "$BASE_URL/messages" | python3 -m json.tool
echo ""

echo "6. GET MESSAGE BY ID"
echo "---------------------"
curl -s "$BASE_URL/messages/1" | python3 -m json.tool
echo ""

echo "7. UPDATE MESSAGE STATUS"
echo "-------------------------"
curl -s -X PATCH "$BASE_URL/messages/1/status?status=SENT" | python3 -m json.tool
echo ""

echo "8. GET MESSAGES BY STATUS"
echo "--------------------------"
curl -s "$BASE_URL/messages/status/PENDING" | python3 -m json.tool
echo ""

echo "9. COUNT MESSAGES BY STATUS"
echo "----------------------------"
curl -s "$BASE_URL/messages/count/status/PENDING" | python3 -m json.tool
echo ""

echo "10. VERIFY IN MySQL DATABASE"
echo "-----------------------------"
mysql -u root -parun -e "SELECT id, sender, recipient, status FROM bulkgateway.messages;" 2>/dev/null
echo ""

echo "=========================================="
echo "  ALL TESTS COMPLETED!"
echo "=========================================="

