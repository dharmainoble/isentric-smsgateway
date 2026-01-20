#!/bin/bash

# Simple WAR File Extractor and Analyzer
WAR="/home/arun/Documents/rec/ExtMTPush.war"
OUTPUT="/home/arun/Documents/rec/war_extracted"

echo "============================================"
echo "Extracting WAR File for Analysis"
echo "============================================"
echo "WAR: $WAR"
echo "Output: $OUTPUT"
echo ""

# Create clean output directory
rm -rf "$OUTPUT"
mkdir -p "$OUTPUT"
cd "$OUTPUT"

# Extract WAR
echo "1. Extracting WAR file..."
jar -xf "$WAR" 2>&1
if [ $? -eq 0 ]; then
    echo "   ✓ Extracted successfully"
else
    echo "   ✗ Extraction failed"
    exit 1
fi

echo ""
echo "2. Analyzing extracted files..."
echo ""

# Configuration files
echo "=== Configuration Files ==="
find . -name "*.xml" -not -path "*/WEB-INF/lib/*" | sort
echo ""
find . -name "*.properties" -not -path "*/WEB-INF/lib/*" | sort
echo ""

# Web config
if [ -f "WEB-INF/web.xml" ]; then
    echo "=== web.xml found ==="
    echo "Location: WEB-INF/web.xml"
    echo "Size: $(wc -c < WEB-INF/web.xml) bytes"
    echo ""
fi

# Spring configs
echo "=== Spring Configuration Files ==="
find WEB-INF/classes -name "*spring*.xml" -o -name "*applicationContext*.xml" 2>/dev/null
echo ""

# Database configs
echo "=== Database Configuration ==="
if [ -f "META-INF/context.xml" ]; then
    echo "Found: META-INF/context.xml"
fi
find WEB-INF/classes -name "*datasource*.xml" -o -name "*db*.properties" 2>/dev/null
echo ""

# JMS configs
echo "=== JMS Configuration ==="
find WEB-INF/classes -name "*jms*.xml" -o -name "*activemq*.xml" -o -name "*artemis*.xml" 2>/dev/null
echo ""

# Web services
echo "=== Web Services ==="
find . -name "*.wsdl" -o -name "*.xsd" | grep -v "WEB-INF/lib"
echo ""

# Static resources
echo "=== Static Resources ==="
find . -maxdepth 2 -name "*.html" -o -name "*.css" -o -name "*.js" | head -10
echo ""

# JSP pages
echo "=== JSP Pages ==="
find . -name "*.jsp" | head -10
echo ""

# Libraries
echo "=== JAR Libraries (first 20) ==="
find WEB-INF/lib -name "*.jar" 2>/dev/null | head -20 | xargs -I {} basename {}
echo ""

# Java source (usually not in WAR, but check)
JAVA_FILES=$(find . -name "*.java" | wc -l)
if [ $JAVA_FILES -gt 0 ]; then
    echo "=== Java Source Files Found: $JAVA_FILES ==="
    find . -name "*.java" | head -10
    echo ""
fi

# Summary
echo "============================================"
echo "Summary"
echo "============================================"
echo "Total files: $(find . -type f | wc -l)"
echo "XML files: $(find . -name "*.xml" -not -path "*/WEB-INF/lib/*" | wc -l)"
echo "Properties files: $(find . -name "*.properties" -not -path "*/WEB-INF/lib/*" | wc -l)"
echo "JAR libraries: $(find WEB-INF/lib -name "*.jar" 2>/dev/null | wc -l)"
echo "JSP pages: $(find . -name "*.jsp" | wc -l)"
echo ""
echo "Extracted to: $OUTPUT"
echo ""
echo "Key files to review:"
echo "  - WEB-INF/web.xml (servlet config)"
echo "  - WEB-INF/classes/*.xml (Spring configs)"
echo "  - WEB-INF/classes/*.properties (app configs)"
echo "  - META-INF/context.xml (datasource)"
echo ""

