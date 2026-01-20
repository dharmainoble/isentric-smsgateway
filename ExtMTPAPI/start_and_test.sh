#!/bin/bash

# Start ExtMTPush Application and Test
echo "=========================================="
echo "Starting ExtMTPush SMS Gateway"
echo "=========================================="
echo ""

# Kill any existing instances
echo "1. Stopping any existing instances..."
pkill -f "java.*extmtpush"
sleep 2

# Start application
echo "2. Starting application..."
cd /home/arun/Documents/rec/ExtMTPush-SpringBoot
java -jar target/extmtpush-springboot-1.0.0.jar > logs/application.log 2>&1 &
APP_PID=$!
echo "   Started with PID: $APP_PID"

# Wait for startup
echo "3. Waiting 20 seconds for startup..."
sleep 20

# Check if running
if ps -p $APP_PID > /dev/null; then
    echo "   ✓ Application is running"
else
    echo "   ✗ Application stopped unexpectedly"
    echo "   Check logs/application.log for errors"
    exit 1
fi

# Test health endpoint
echo ""
echo "4. Testing health endpoint..."
HEALTH=$(curl -s http://localhost:8083/ExtMTPush/actuator/health)
if echo "$HEALTH" | grep -q "UP"; then
    echo "   ✓ Health check passed"
else
    echo "   ✗ Health check failed"
    echo "   Response: $HEALTH"
fi

# Test SMS endpoint
echo ""
echo "5. Testing SMS endpoint..."
RESPONSE=$(curl -s -X POST "http://localhost:8083/ExtMTPush/extmtpush" \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode": "66399",
    "custid": "CUST001",
    "rmsisdn": "60123456789",
    "mtid": "MSG_TEST_'$(date +%s)'",
    "productType": 4,
    "dataEncoding": 0,
    "dataStr": "Test SMS from startup script",
    "dnRep": 1
  }')

echo "   Response: $RESPONSE"

if echo "$RESPONSE" | grep -q "returnCode = 0"; then
    echo "   ✓ SUCCESS! Data inserted"
elif echo "$RESPONSE" | grep -q "returnCode"; then
    CODE=$(echo "$RESPONSE" | grep -o "returnCode = [0-9]*" | grep -o "[0-9]*")
    MSG=$(echo "$RESPONSE" | grep -o "returnMsg = [^,]*" | sed 's/returnMsg = //')
    echo "   ✗ ERROR: Return Code $CODE - $MSG"
else
    echo "   ✗ No response from endpoint"
fi

# Verify in database
echo ""
echo "6. Checking database..."
DB_COUNT=$(mysql -uroot -parun extmt -sN -e "SELECT COUNT(*) FROM extmtpush_receive_bulk WHERE custid='CUST001';" 2>&1 | grep -v Warning)
echo "   Total SMS records for CUST001: $DB_COUNT"

if [ "$DB_COUNT" -gt 0 ]; then
    echo ""
    echo "   Recent SMS records:"
    mysql -uroot -parun extmt -e "
      SELECT mtid, rmsisdn, data_str, received_date
      FROM extmtpush_receive_bulk
      WHERE custid='CUST001'
      ORDER BY received_date DESC
      LIMIT 3;" 2>&1 | grep -v Warning
fi

echo ""
echo "=========================================="
echo "Application PID: $APP_PID"
echo "To stop: kill $APP_PID"
echo "To view logs: tail -f logs/application.log"
echo "=========================================="

