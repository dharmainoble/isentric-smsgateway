#!/bin/bash

# Quick Fix for returnCode = 7 (Invalid masking ID)

echo "=========================================="
echo "Fixing: returnCode = 7 (Invalid masking ID)"
echo "=========================================="
echo ""

DB_USER="root"
DB_PASS="arun"

echo "Creating masking_id table and adding test data..."

mysql -u$DB_USER -p$DB_PASS bulk_config << 'EOF'
-- Create masking_id table
CREATE TABLE IF NOT EXISTS masking_id (
    id INT AUTO_INCREMENT PRIMARY KEY,
    custid VARCHAR(50) NOT NULL,
    masking_id VARCHAR(20) NOT NULL,
    active CHAR(1) DEFAULT '1',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_custid_masking (custid, masking_id)
);

-- Add test masking IDs for CUST001
DELETE FROM masking_id WHERE custid = 'CUST001';

INSERT INTO masking_id (custid, masking_id, active) VALUES
('CUST001', '62003', '1'),
('CUST001', '66399', '1'),
('CUST001', 'TESTCO', '1'),
('CUST001', 'SMS', '1');

SELECT 'Masking IDs added successfully:' as '';
SELECT custid, masking_id, active FROM masking_id WHERE custid = 'CUST001';
EOF

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Success! Masking IDs configured."
    echo ""
    echo "Registered Masking IDs for CUST001:"
    echo "  - 62003"
    echo "  - 66399"
    echo "  - TESTCO"
    echo "  - SMS"
    echo ""
    echo "Now you can use these sender IDs (smsisdn parameter) in your API calls."
    echo ""
    echo "Or use the updated Postman collection which doesn't require sender ID."
else
    echo ""
    echo "❌ Error: Failed to create masking_id table"
    echo "Please check database connection and permissions"
fi

echo ""
echo "=========================================="
echo "Next Steps:"
echo "=========================================="
echo "1. Re-import the updated Postman collection (API 2 now works without sender ID)"
echo "2. Or test with curl:"
echo ""
echo "   # Without sender ID (recommended):"
echo "   curl \"http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60192782366,60122786404&mtid=MSG_\$(date +%s)&productType=4&dataEncoding=0&dataStr=Test&dnRep=0\""
echo ""
echo "   # With sender ID (now registered):"
echo "   curl \"http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&rmsisdn=60192782366&smsisdn=62003&mtid=MSG_\$(date +%s)&productType=4&dataEncoding=0&dataStr=Test&dnRep=0\""
echo ""

