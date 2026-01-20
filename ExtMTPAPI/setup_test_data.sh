#!/bin/bash

# Database Setup for ExtMTPush Testing
# This script creates necessary test data

echo "========================================"
echo "ExtMTPush - Database Setup for Testing"
echo "========================================"
echo ""

# Database credentials
DB_USER="root"
DB_PASS="arun"

echo "Creating test data in databases..."
echo ""

# 1. Add test customer IP authorization
echo "1. Adding IP authorization for localhost..."
mysql -u$DB_USER -p$DB_PASS bulk_config << EOF
-- Drop old table if exists
DROP TABLE IF EXISTS cp_ip;

-- Create cpip table with correct structure (matching CpIp entity)
CREATE TABLE IF NOT EXISTS cpip (
    row_id INT AUTO_INCREMENT PRIMARY KEY,
    shortcode VARCHAR(10),
    cpidentity VARCHAR(50),
    cp_ip VARCHAR(50),
    hlr_flag CHAR(1) DEFAULT '1',
    active CHAR(1) DEFAULT '1',
    INDEX idx_shortcode_cpidentity_ip (shortcode, cpidentity, cp_ip)
);

-- Delete existing records for CUST001
DELETE FROM cpip WHERE cpidentity = 'CUST001';

-- Add localhost and common IPs for testing with shortcode
INSERT INTO cpip (shortcode, cpidentity, cp_ip, hlr_flag, active) VALUES
('66399', 'CUST001', '127.0.0.1', '1', '1'),
('66399', 'CUST001', 'localhost', '1', '1'),
('66399', 'CUST001', '0:0:0:0:0:0:0:1', '1', '1'),
('66399', 'CUST001', '::1', '1', '1');

SELECT 'IP authorizations added:' as '';
SELECT shortcode, cpidentity, cp_ip, hlr_flag, active FROM cpip WHERE cpidentity = 'CUST001';
EOF

echo ""

# 2. Add test customer credit
echo "2. Adding customer credit..."
mysql -u$DB_USER -p$DB_PASS bulk_config << EOF
-- Create customer_credit table if not exists
CREATE TABLE IF NOT EXISTS customer_credit (
    id INT AUTO_INCREMENT PRIMARY KEY,
    custid VARCHAR(50) NOT NULL UNIQUE,
    credit_balance DECIMAL(10,2) DEFAULT 0.00,
    active CHAR(1) DEFAULT '1',
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_custid (custid)
);

-- Add test customer with credit
INSERT INTO customer_credit (custid, credit_balance, active)
VALUES ('CUST001', 1000.00, '1')
ON DUPLICATE KEY UPDATE credit_balance = 1000.00, active = '1';

SELECT 'Customer credit added:' as '';
SELECT custid, credit_balance, active FROM customer_credit WHERE custid = 'CUST001';
EOF

echo ""

# 3. Create extmtpush_receive_bulk table
echo "3. Setting up SMS receive table..."
mysql -u$DB_USER -p$DB_PASS extmt << EOF
-- Create extmtpush_receive_bulk table if not exists
CREATE TABLE IF NOT EXISTS extmtpush_receive_bulk (
    row_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bill_flag CHAR(1) DEFAULT '1',
    process_flag CHAR(1) DEFAULT 'P',
    shortcode VARCHAR(10),
    custid VARCHAR(50),
    rmsisdn VARCHAR(20),
    smsisdn VARCHAR(20),
    mtid VARCHAR(100),
    mtprice VARCHAR(10),
    product_type INT,
    product_code VARCHAR(50),
    keyword VARCHAR(255),
    data_encoding INT,
    data_str TEXT,
    data_url VARCHAR(500),
    url_title VARCHAR(255),
    dnrep INT,
    group_tag VARCHAR(50),
    ewig_flag CHAR(1),
    received_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sent_date TIMESTAMP NULL,
    delivery_status VARCHAR(20),
    INDEX idx_custid (custid),
    INDEX idx_mtid (mtid),
    INDEX idx_process_flag (process_flag),
    INDEX idx_received_date (received_date)
);

SELECT 'SMS receive table ready' as '';
SELECT COUNT(*) as 'Total SMS records' FROM extmtpush_receive_bulk;
EOF

echo ""

# 4. Create extmt_mtid table
echo "4. Setting up MTID tracking table..."
mysql -u$DB_USER -p$DB_PASS extmt << EOF
-- Create extmt_mtid table if not exists
CREATE TABLE IF NOT EXISTS extmt_mtid (
    row_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mtid VARCHAR(100) NOT NULL UNIQUE,
    custid VARCHAR(50) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_mtid_custid (mtid, custid)
);

SELECT 'MTID tracking table ready' as '';
EOF

echo ""

# 5. Create package configuration (optional)
echo "5. Setting up package configuration..."
mysql -u$DB_USER -p$DB_PASS bulk_config << EOF
-- Create package table if not exists
CREATE TABLE IF NOT EXISTS cp_package (
    id INT AUTO_INCREMENT PRIMARY KEY,
    custid VARCHAR(50) NOT NULL,
    shortcode VARCHAR(10) NOT NULL,
    active CHAR(1) DEFAULT '1',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_custid_shortcode (custid, shortcode)
);

-- Add test package
INSERT INTO cp_package (custid, shortcode, active)
VALUES ('CUST001', '66399', '1')
ON DUPLICATE KEY UPDATE active = '1';

SELECT 'Package configuration added:' as '';
SELECT custid, shortcode, active FROM cp_package WHERE custid = 'CUST001';
EOF

echo ""
echo "========================================"
echo "✓ Database setup completed successfully!"
echo "========================================"
echo ""
echo "Test credentials:"
echo "  Customer ID: CUST001"
echo "  Shortcode: 66399"
echo "  Credit Balance: 1000.00"
echo "  Authorized IPs: 127.0.0.1, localhost, ::1"
echo ""
echo "You can now test the API with:"
echo "  ./test_sms_api.sh"
echo ""

# 6. Masking ID configuration
echo "6. Setting up masking IDs..."
mysql -u$DB_USER -p$DB_PASS bulk_config << 'EOSQL'
-- Create masking_id table
CREATE TABLE IF NOT EXISTS masking_id (
    id INT AUTO_INCREMENT PRIMARY KEY,
    custid VARCHAR(50) NOT NULL,
    masking_id VARCHAR(20) NOT NULL,
    active CHAR(1) DEFAULT '1',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_custid_masking (custid, masking_id)
);
-- Add test masking IDs
DELETE FROM masking_id WHERE custid = 'CUST001';
INSERT INTO masking_id (custid, masking_id, active) VALUES
('CUST001', '62003', '1'),
('CUST001', '66399', '1'),
('CUST001', 'TESTCO', '1'),
('CUST001', 'SMS', '1');
SELECT 'Masking IDs configured:' as '';
SELECT custid, masking_id, active FROM masking_id WHERE custid = 'CUST001';
EOSQL
echo ""
echo "========================================="
echo "✓ All tables configured including masking IDs!"
echo "========================================="
