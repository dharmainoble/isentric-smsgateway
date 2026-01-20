-- ExtMTPush MySQL Database Schema
-- Run this script to create the required tables in MySQL
-- Usage: mysql -u root -p < schema-mysql.sql

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS extmtpush;
USE extmtpush;

-- Table: extmtpush_receive_bulk - Stores incoming MT push requests
CREATE TABLE IF NOT EXISTS extmtpush_receive_bulk (
    row_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bill_flag VARCHAR(5),
    process_flag VARCHAR(5),
    shortcode VARCHAR(20),
    custid VARCHAR(50),
    rmsisdn VARCHAR(20),
    smsisdn VARCHAR(50),
    mtid VARCHAR(50),
    mtprice VARCHAR(10),
    product_type INT,
    product_code VARCHAR(50),
    keyword VARCHAR(100),
    data_encoding INT,
    data_str TEXT,
    data_url VARCHAR(500),
    url_title VARCHAR(200),
    dnrep INT,
    group_tag VARCHAR(50),
    ewig_flag VARCHAR(5),
    received_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_date TIMESTAMP NULL,
    remarks VARCHAR(500),
    INDEX idx_custid (custid),
    INDEX idx_mtid (mtid),
    INDEX idx_rmsisdn (rmsisdn),
    INDEX idx_process_flag (process_flag),
    INDEX idx_received_date (received_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: extmtpush_send_bulk - Stores outgoing MT messages
CREATE TABLE IF NOT EXISTS extmtpush_send_bulk (
    row_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    shortcode VARCHAR(20),
    custid VARCHAR(50),
    rmsisdn VARCHAR(20),
    smsisdn VARCHAR(50),
    mtid VARCHAR(50),
    mtprice VARCHAR(10),
    product_type INT,
    product_code VARCHAR(50),
    keyword VARCHAR(100),
    data_encoding INT,
    data_str TEXT,
    data_url VARCHAR(500),
    url_title VARCHAR(200),
    dnrep INT,
    group_tag VARCHAR(50),
    telco VARCHAR(20),
    send_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20),
    hex_data TEXT,
    INDEX idx_custid (custid),
    INDEX idx_mtid (mtid),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: extmt_mtid - Tracks message IDs to prevent duplicates
CREATE TABLE IF NOT EXISTS extmt_mtid (
    row_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mtid VARCHAR(50),
    custid VARCHAR(50),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_mtid_custid (mtid, custid),
    INDEX idx_mtid (mtid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: tbl_smpp_dn - Stores delivery notifications
CREATE TABLE IF NOT EXISTS tbl_smpp_dn (
    row_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    smpServiceID VARCHAR(50),
    smppName VARCHAR(50),
    smppId VARCHAR(50),
    smppType VARCHAR(20),
    sender VARCHAR(50),
    recipient VARCHAR(20),
    timestamp TIMESTAMP NULL,
    smppStatus VARCHAR(50),
    DNStatus VARCHAR(50),
    errorCode VARCHAR(20),
    dcs VARCHAR(10),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    bytes VARCHAR(500),
    INDEX idx_smppId (smppId),
    INDEX idx_recipient (recipient),
    INDEX idx_date (date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: bulk_credit - Stores customer credit information
CREATE TABLE IF NOT EXISTS bulk_credit (
    row_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    custid VARCHAR(50) UNIQUE,
    credit DECIMAL(15,2) DEFAULT 0,
    credit_type INT DEFAULT 1 COMMENT '-1=unlimited, 1=prepaid',
    password VARCHAR(100),
    status INT DEFAULT 1 COMMENT '1=active, 0=inactive',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_custid (custid),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: bulk_credit_log - Logs credit transactions
CREATE TABLE IF NOT EXISTS bulk_credit_log (
    row_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    custid VARCHAR(50),
    amount DECIMAL(15,2),
    transaction_type VARCHAR(20),
    remarks VARCHAR(200),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_custid (custid),
    INDEX idx_transaction_date (transaction_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: bulk_masking_id - Stores approved sender IDs
CREATE TABLE IF NOT EXISTS bulk_masking_id (
    row_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    custid VARCHAR(50),
    masking_id VARCHAR(50),
    status INT DEFAULT 1,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_custid (custid),
    INDEX idx_masking_id (masking_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: bulk_destination_sms - Stores destination settings
CREATE TABLE IF NOT EXISTS bulk_destination_sms (
    row_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    custid VARCHAR(50),
    local_flag VARCHAR(5) DEFAULT '0' COMMENT '0=allowed, 1=blocked',
    int_flag VARCHAR(5) DEFAULT '0' COMMENT '0=allowed, 1=blocked',
    status INT DEFAULT 1,
    INDEX idx_custid (custid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: bulk_ip_whitelist - IP whitelist for customers
CREATE TABLE IF NOT EXISTS bulk_ip_whitelist (
    row_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    custid VARCHAR(50),
    ip_address VARCHAR(50),
    status INT DEFAULT 1,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_custid (custid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: sms_generic_forwardmo_blacklist - MSISDN blacklist
CREATE TABLE IF NOT EXISTS sms_generic_forwardmo_blacklist (
    row_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    message_id VARCHAR(50),
    msisdn VARCHAR(20),
    blacklist_keyword VARCHAR(100),
    blacklist_flag INT DEFAULT 0,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_msisdn (msisdn)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: sms_generic_forwardmo_details - Whitelist details
CREATE TABLE IF NOT EXISTS sms_generic_forwardmo_details (
    row_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    message_id VARCHAR(50),
    msisdn VARCHAR(20),
    shortcode VARCHAR(20),
    keyword VARCHAR(100),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_msisdn (msisdn)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================
-- Insert sample test data
-- ========================================

-- Insert test customers
INSERT IGNORE INTO bulk_credit (custid, credit, credit_type, password, status) VALUES
('TEST001', 1000.00, 1, 'test123', 1),
('TEST002', 500.00, 1, 'test456', 1),
('UNLIMITED', 0.00, -1, 'unlimited', 1);

-- Insert masking IDs
INSERT IGNORE INTO bulk_masking_id (custid, masking_id, status) VALUES
('TEST001', 'SENDER', 1),
('TEST001', 'COMPANY', 1),
('TEST002', 'BRAND', 1);

-- Insert destination settings (allow both local and international)
INSERT IGNORE INTO bulk_destination_sms (custid, local_flag, int_flag, status) VALUES
('TEST001', '0', '0', 1),
('TEST002', '0', '1', 1),
('UNLIMITED', '0', '0', 1);

-- Insert IP whitelist (0.0.0.0 means allow all IPs)
INSERT IGNORE INTO bulk_ip_whitelist (custid, ip_address, status) VALUES
('TEST001', '0.0.0.0', 1),
('TEST002', '127.0.0.1', 1);

-- Insert sample delivery notifications for testing
INSERT INTO tbl_smpp_dn (smpServiceID, smppName, smppId, smppType, sender, recipient, timestamp, smppStatus, DNStatus, errorCode, dcs, date, bytes) VALUES
('SVC001', 'HTTP_CELCOM', 'MSG001', 'MT_STATUS', '12345', '60123456789', NOW(), 'STATE_DELIVERED', 'DELIVERED', '0', '', NOW(), ''),
('SVC001', 'HTTP_DIGI', 'MSG002', 'MT_STATUS', '12345', '60163456789', NOW(), 'STATE_ENROUTE', 'PENDING', '', '', NOW(), '');

SELECT 'Database setup completed successfully!' AS Status;

