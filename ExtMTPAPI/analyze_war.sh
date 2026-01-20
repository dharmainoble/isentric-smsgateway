#!/bin/bash

# Script to Extract and Compare WAR with Spring Boot Project
WAR_FILE="/home/arun/Documents/rec/ExtMTPush.war"
EXTRACT_DIR="/tmp/war_analysis"
SPRINGBOOT_DIR="/home/arun/Documents/rec/ExtMTPush-SpringBoot"

echo "=========================================="
echo "WAR to Spring Boot Comparison Analysis"
echo "=========================================="
echo ""

# Check if WAR file exists
if [ ! -f "$WAR_FILE" ]; then
    echo "❌ WAR file not found: $WAR_FILE"
    exit 1
fi

# Clean and create extraction directory
rm -rf "$EXTRACT_DIR"
mkdir -p "$EXTRACT_DIR"

echo "1. Extracting WAR file..."
cd "$EXTRACT_DIR"
jar -xf "$WAR_FILE" 2>/dev/null || unzip -q "$WAR_FILE" 2>/dev/null

if [ $? -ne 0 ]; then
    echo "❌ Failed to extract WAR file"
    exit 1
fi

echo "✓ WAR extracted successfully"
echo ""

# Analyze structure
echo "2. Analyzing WAR structure..."
echo ""

echo "Configuration Files:"
echo "-------------------"
find "$EXTRACT_DIR" -name "*.xml" -o -name "*.properties" | while read file; do
    rel_path="${file#$EXTRACT_DIR/}"
    echo "  - $rel_path"
done
echo ""

echo "Java Source Files (if any):"
echo "-------------------------"
find "$EXTRACT_DIR" -name "*.java" | head -10 | while read file; do
    rel_path="${file#$EXTRACT_DIR/}"
    echo "  - $rel_path"
done
echo ""

echo "Compiled Classes:"
echo "----------------"
find "$EXTRACT_DIR/WEB-INF/classes" -name "*.class" 2>/dev/null | wc -l | xargs echo "  Total class files:"
echo ""

echo "JAR Libraries:"
echo "-------------"
find "$EXTRACT_DIR/WEB-INF/lib" -name "*.jar" 2>/dev/null | while read file; do
    basename "$file"
done | head -20
echo ""

echo "3. Comparing with Spring Boot project..."
echo ""

# Check for web.xml
if [ -f "$EXTRACT_DIR/WEB-INF/web.xml" ]; then
    echo "✓ Found web.xml (legacy servlet config)"
    echo "  Spring Boot: Uses @SpringBootApplication instead"
fi

# Check for applicationContext.xml or Spring XML configs
SPRING_XML=$(find "$EXTRACT_DIR/WEB-INF" -name "*applicationContext*.xml" -o -name "*spring*.xml" 2>/dev/null)
if [ -n "$SPRING_XML" ]; then
    echo "✓ Found Spring XML configurations:"
    echo "$SPRING_XML" | while read file; do
        echo "  - $(basename $file)"
    done
    echo "  Spring Boot: Uses Java @Configuration classes instead"
fi

# Check for persistence.xml
if [ -f "$EXTRACT_DIR/WEB-INF/classes/META-INF/persistence.xml" ]; then
    echo "✓ Found persistence.xml (JPA config)"
    echo "  Spring Boot: Configured in application.properties"
fi

# Check for data source configurations
if [ -f "$EXTRACT_DIR/META-INF/context.xml" ]; then
    echo "✓ Found context.xml (Tomcat data source config)"
    echo "  Spring Boot: Configured in DataSourceConfig.java"
fi

echo ""
echo "4. Missing Files Analysis..."
echo ""

# List unique packages in WAR
echo "Packages in WAR:"
find "$EXTRACT_DIR/WEB-INF/classes" -type d 2>/dev/null | grep -E "com/isentric" | sed 's|.*/WEB-INF/classes/||' | sort -u | head -10

echo ""
echo "Packages in Spring Boot:"
find "$SPRINGBOOT_DIR/src/main/java" -type d 2>/dev/null | grep -E "com/isentric" | sed 's|.*/com/isentric/|com.isentric.|g' | sed 's|/|.|g' | sort -u | head -10

echo ""
echo "=========================================="
echo "Analysis complete!"
echo "=========================================="
echo ""
echo "Key findings saved to: war_analysis_report.txt"

# Generate detailed report
cat > "$SPRINGBOOT_DIR/war_analysis_report.txt" << 'REPORT'
# WAR to Spring Boot Migration - Missing Files Check

## Configuration Files Mapping

### Original WAR Structure:
- WEB-INF/web.xml → Spring Boot uses embedded Tomcat (no web.xml needed)
- WEB-INF/applicationContext.xml → Converted to @Configuration classes
- META-INF/context.xml → Converted to DataSourceConfig.java
- META-INF/persistence.xml → Converted to application.properties

### Database Configuration:
- Original: context.xml with JNDI data sources
- Spring Boot: DataSourceConfig.java with HikariCP

### JMS Configuration:
- Original: XML-based JMS config
- Spring Boot: JmsConfig.java

## Common Missing Items to Check:

1. **Property Files**:
   - Check if there were any .properties files in WEB-INF/classes
   - These should be moved to src/main/resources

2. **Static Resources**:
   - CSS, JS, images in WAR root → src/main/resources/static
   - JSPs in WEB-INF/jsp → src/main/resources/templates (if using Thymeleaf)

3. **Configuration Files**:
   - log4j.xml or logback.xml → src/main/resources/logback-spring.xml
   - Any custom XML configs → Convert to Java @Configuration

4. **Libraries**:
   - JARs in WEB-INF/lib → Dependencies in pom.xml

5. **Servlets and Filters**:
   - Servlet definitions in web.xml → @WebServlet or @Bean definitions
   - Filter definitions → @Component or FilterRegistrationBean

6. **Web Services**:
   - WSDL files → src/main/resources/wsdl
   - XSD schemas → src/main/resources/xsd

REPORT

echo "Report saved to: $SPRINGBOOT_DIR/war_analysis_report.txt"
echo ""
echo "To extract specific files from WAR:"
echo "  jar -xf $WAR_FILE WEB-INF/classes/config.properties"
echo ""

