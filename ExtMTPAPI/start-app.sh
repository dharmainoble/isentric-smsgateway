#!/bin/bash

echo "======================================"
echo "ExtMTPush Application Startup"
echo "======================================"
echo ""

# Check if databases are accessible
echo "Checking database connectivity..."
mysql -h localhost -u root -parun -e "SELECT 1;" extmt > /dev/null 2>&1
DB1=$?
mysql -h localhost -u root -parun -e "SELECT 1;" bulk_config > /dev/null 2>&1
DB2=$?

if [ $DB1 -eq 0 ] && [ $DB2 -eq 0 ]; then
    echo "✓ Databases are accessible"
else
    echo "⚠ Warning: One or both databases are not accessible"
    echo "  Please ensure MySQL is running and databases exist:"
    echo "  - extmt"
    echo "  - bulk_config"
    echo ""
    read -p "Continue anyway? (y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

echo ""
echo "Starting application..."
echo "Application will be available at: http://localhost:8083/ExtMTPush"
echo ""
echo "Note: JMS is currently DISABLED. To enable:"
echo "  1. Install and start Apache Artemis"
echo "  2. Set spring.jms.enabled=true in application.properties"
echo ""

# Start the application
mvn spring-boot:run

