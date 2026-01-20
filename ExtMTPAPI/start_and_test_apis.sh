#!/bin/bash

# Start Application and Run API Tests
echo "============================================"
echo "Starting ExtMTPush Application"
echo "============================================"
echo ""

cd /home/arun/Documents/rec/ExtMTPush-SpringBoot

# Stop any existing instance
echo "1. Stopping existing instances..."
pkill -f "java.*extmtpush"
sleep 2

# Start application
echo "2. Starting application..."
java -jar target/extmtpush-springboot-1.0.0.jar > logs/app.log 2>&1 &
APP_PID=$!
echo "   Started with PID: $APP_PID"

# Wait for startup
echo "3. Waiting for application to be ready..."
MAX_WAIT=30
COUNT=0

while [ $COUNT -lt $MAX_WAIT ]; do
    if curl -s http://localhost:8083/ExtMTPush/actuator/health | grep -q "UP"; then
        echo "   ✓ Application is ready!"
        break
    fi
    COUNT=$((COUNT + 1))
    echo -n "."
    sleep 1
done
echo ""

if [ $COUNT -eq $MAX_WAIT ]; then
    echo "   ✗ Application failed to start"
    echo "   Check logs: tail -50 logs/app.log"
    exit 1
fi

echo ""
echo "4. Running API tests..."
echo ""

# Make test script executable and run it
chmod +x test_all_apis.sh
./test_all_apis.sh

echo ""
echo "============================================"
echo "Application is running with PID: $APP_PID"
echo ""
echo "To stop: kill $APP_PID"
echo "To view logs: tail -f logs/app.log"
echo "============================================"

