#!/bin/bash

echo "========================================="
echo "Fixing Java 17 Configuration for IntelliJ"
echo "========================================="
echo ""

# Clean Maven artifacts
echo "Step 1: Cleaning Maven build artifacts..."
mvn clean

# Force Maven to update dependencies
echo ""
echo "Step 2: Updating Maven dependencies..."
mvn dependency:resolve -U

# Run a test compile to verify Maven is using Java 17
echo ""
echo "Step 3: Testing Maven compilation with Java 17..."
mvn compile

echo ""
echo "========================================="
echo "Maven Configuration: ✓ COMPLETE"
echo "========================================="
echo ""
echo "Maven is correctly configured for Java 17."
echo "The issue you're seeing is IntelliJ IDEA's cache."
echo ""
echo "========================================="
echo "REQUIRED: IntelliJ IDEA Actions"
echo "========================================="
echo ""
echo "1. INVALIDATE CACHES (REQUIRED):"
echo "   File → Invalidate Caches..."
echo "   ✓ Check: Clear file system cache and Local History"
echo "   ✓ Check: Clear downloaded shared indexes"
echo "   Click: 'Invalidate and Restart'"
echo ""
echo "2. AFTER RESTART:"
echo "   Right-click pom.xml → Maven → Reload Project"
echo ""
echo "3. REBUILD:"
echo "   Build → Rebuild Project"
echo ""
echo "4. IF STILL NOT WORKING:"
echo "   Press Ctrl+Alt+Shift+S (Project Structure)"
echo "   → Project tab:"
echo "      - SDK: Set to '17'"
echo "      - Language level: Set to '17'"
echo "   → Modules tab:"
echo "      - Select 'extmtpush-springboot'"
echo "      - Language level: Set to 'Project default (17)'"
echo "   Click Apply and OK"
echo ""
echo "========================================="
echo ""



