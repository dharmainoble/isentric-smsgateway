#!/bin/bash

echo "=============================================="
echo "ExtMTPush Spring Boot - Setup & Verification"
echo "=============================================="
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check Java version
echo "1. Checking Java version..."
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -ge "17" ]; then
        echo -e "${GREEN}✓ Java $JAVA_VERSION detected${NC}"
    else
        echo -e "${RED}✗ Java 17+ required, found Java $JAVA_VERSION${NC}"
        exit 1
    fi
else
    echo -e "${RED}✗ Java not found${NC}"
    exit 1
fi
echo ""

# Check Maven
echo "2. Checking Maven..."
if command -v mvn &> /dev/null; then
    MVN_VERSION=$(mvn -version | head -1 | cut -d' ' -f3)
    echo -e "${GREEN}✓ Maven $MVN_VERSION detected${NC}"
else
    echo -e "${YELLOW}⚠ Maven not found - required for building${NC}"
fi
echo ""

# Check MySQL
echo "3. Checking MySQL..."
if command -v mysql &> /dev/null; then
    echo -e "${GREEN}✓ MySQL client detected${NC}"
else
    echo -e "${YELLOW}⚠ MySQL client not found${NC}"
fi
echo ""

# Project structure verification
echo "4. Verifying project structure..."
REQUIRED_DIRS=(
    "src/main/java/com/isentric/smsserver"
    "src/main/resources"
)

for dir in "${REQUIRED_DIRS[@]}"; do
    if [ -d "$dir" ]; then
        echo -e "${GREEN}✓ $dir${NC}"
    else
        echo -e "${RED}✗ $dir missing${NC}"
    fi
done
echo ""

# Count files
echo "5. Project statistics..."
JAVA_FILES=$(find src -name "*.java" 2>/dev/null | wc -l)
CONFIG_FILES=$(find src/main/resources -name "*.properties" -o -name "*.xml" 2>/dev/null | wc -l)
DOC_FILES=$(find . -maxdepth 1 -name "*.md" 2>/dev/null | wc -l)

echo "   Java files: $JAVA_FILES"
echo "   Config files: $CONFIG_FILES"
echo "   Documentation files: $DOC_FILES"
echo ""

# Configuration check
echo "6. Configuration files..."
if [ -f "src/main/resources/application.properties" ]; then
    echo -e "${GREEN}✓ application.properties${NC}"
else
    echo -e "${RED}✗ application.properties missing${NC}"
fi

if [ -f "pom.xml" ]; then
    echo -e "${GREEN}✓ pom.xml${NC}"
else
    echo -e "${RED}✗ pom.xml missing${NC}"
fi
echo ""

# Next steps
echo "=============================================="
echo "Next Steps:"
echo "=============================================="
echo ""
echo "1. Configure Database:"
echo "   - Edit src/main/resources/application.properties"
echo "   - Update database URLs, usernames, passwords"
echo ""
echo "2. Setup JMS Broker:"
echo "   - Install Apache Artemis or ActiveMQ"
echo "   - Update broker URL in application.properties"
echo ""
echo "3. Build Project:"
echo "   mvn clean package"
echo ""
echo "4. Run Application:"
echo "   mvn spring-boot:run"
echo "   OR"
echo "   java -jar target/extmtpush-springboot-1.0.0.jar"
echo ""
echo "5. Test Endpoints:"
echo "   curl http://localhost:8080/ExtMTPush/actuator/health"
echo ""
echo "=============================================="
echo "For more information, see:"
echo "   - README.md"
echo "   - MIGRATION_ANALYSIS.md"
echo "   - PROJECT_SUMMARY.md"
echo "=============================================="

