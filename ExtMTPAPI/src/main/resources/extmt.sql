-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.42-0ubuntu0.20.04.1 - (Ubuntu)
-- Server OS:                    Linux
-- HeidiSQL Version:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for extmt
DROP DATABASE IF EXISTS `extmt`;
CREATE DATABASE IF NOT EXISTS `extmt` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `extmt`;

-- Dumping structure for table extmt.bulk_credit
DROP TABLE IF EXISTS `bulk_credit`;
CREATE TABLE IF NOT EXISTS `bulk_credit` (
  `row_id` bigint NOT NULL AUTO_INCREMENT,
  `custid` varchar(50) DEFAULT NULL,
  `credit` decimal(15,2) DEFAULT '0.00',
  `credit_type` int DEFAULT '1',
  `password` varchar(100) DEFAULT NULL,
  `status` int DEFAULT '1',
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`row_id`),
  UNIQUE KEY `custid` (`custid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table extmt.bulk_credit: ~0 rows (approximately)

-- Dumping structure for table extmt.bulk_credit_log
DROP TABLE IF EXISTS `bulk_credit_log`;
CREATE TABLE IF NOT EXISTS `bulk_credit_log` (
  `row_id` bigint NOT NULL AUTO_INCREMENT,
  `custid` varchar(50) DEFAULT NULL,
  `amount` decimal(15,2) DEFAULT NULL,
  `transaction_type` varchar(20) DEFAULT NULL,
  `remarks` varchar(200) DEFAULT NULL,
  `transaction_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`row_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table extmt.bulk_credit_log: ~0 rows (approximately)

-- Dumping structure for table extmt.bulk_destination_sms
DROP TABLE IF EXISTS `bulk_destination_sms`;
CREATE TABLE IF NOT EXISTS `bulk_destination_sms` (
  `row_id` bigint NOT NULL AUTO_INCREMENT,
  `custid` varchar(50) DEFAULT NULL,
  `local_flag` varchar(5) DEFAULT '0',
  `int_flag` varchar(5) DEFAULT '0',
  `status` int DEFAULT '1',
  PRIMARY KEY (`row_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table extmt.bulk_destination_sms: ~0 rows (approximately)

-- Dumping structure for table extmt.bulk_ip_whitelist
DROP TABLE IF EXISTS `bulk_ip_whitelist`;
CREATE TABLE IF NOT EXISTS `bulk_ip_whitelist` (
  `row_id` bigint NOT NULL AUTO_INCREMENT,
  `custid` varchar(50) DEFAULT NULL,
  `ip_address` varchar(50) DEFAULT NULL,
  `status` int DEFAULT '1',
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`row_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table extmt.bulk_ip_whitelist: ~0 rows (approximately)

-- Dumping structure for table extmt.bulk_masking_id
DROP TABLE IF EXISTS `bulk_masking_id`;
CREATE TABLE IF NOT EXISTS `bulk_masking_id` (
  `row_id` bigint NOT NULL AUTO_INCREMENT,
  `custid` varchar(50) DEFAULT NULL,
  `masking_id` varchar(50) DEFAULT NULL,
  `status` int DEFAULT '1',
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`row_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table extmt.bulk_masking_id: ~0 rows (approximately)

-- Dumping structure for table extmt.extmtpush_receive_bulk
DROP TABLE IF EXISTS `extmtpush_receive_bulk`;
CREATE TABLE IF NOT EXISTS `extmtpush_receive_bulk` (
  `row_id` bigint NOT NULL AUTO_INCREMENT,
  `bill_flag` char(1) DEFAULT '1',
  `process_flag` char(1) DEFAULT 'P',
  `shortcode` varchar(10) DEFAULT NULL,
  `custid` varchar(50) DEFAULT NULL,
  `rmsisdn` varchar(20) DEFAULT NULL,
  `smsisdn` varchar(20) DEFAULT NULL,
  `mtid` varchar(100) DEFAULT NULL,
  `mtprice` varchar(10) DEFAULT NULL,
  `product_type` int DEFAULT NULL,
  `product_code` varchar(50) DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `data_encoding` int DEFAULT NULL,
  `data_str` text,
  `data_url` varchar(500) DEFAULT NULL,
  `url_title` varchar(255) DEFAULT NULL,
  `dnrep` int DEFAULT NULL,
  `group_tag` varchar(50) DEFAULT NULL,
  `ewig_flag` char(1) DEFAULT NULL,
  `received_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `sent_date` timestamp NULL DEFAULT NULL,
  `delivery_status` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`row_id`),
  KEY `idx_custid` (`custid`),
  KEY `idx_mtid` (`mtid`),
  KEY `idx_process_flag` (`process_flag`),
  KEY `idx_received_date` (`received_date`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table extmt.extmtpush_receive_bulk: ~6 rows (approximately)
INSERT INTO `extmtpush_receive_bulk` (`row_id`, `bill_flag`, `process_flag`, `shortcode`, `custid`, `rmsisdn`, `smsisdn`, `mtid`, `mtprice`, `product_type`, `product_code`, `keyword`, `data_encoding`, `data_str`, `data_url`, `url_title`, `dnrep`, `group_tag`, `ewig_flag`, `received_date`, `sent_date`, `delivery_status`) VALUES
	(1, '1', 'P', '66399', 'CUST001', '60123456789', NULL, 'MSG_FINAL_1765962888', NULL, 4, NULL, '', 0, 'SUCCESS! Data inserted via Postman', NULL, '', 1, NULL, NULL, '2025-12-17 03:44:48', NULL, NULL),
	(2, '1', 'P', '66399', 'CUST001', '60123456789', NULL, 'MSG_WORKING_1765962903', NULL, 4, NULL, '', 0, 'Test SMS', NULL, '', 1, NULL, NULL, '2025-12-17 03:45:03', NULL, NULL),
	(3, '1', 'P', '66399', 'CUST001', '60192782366', '62003', 'MSG_1765968746', '000', 4, '', '', 0, 'Test SMS from Postman POST', '', '', 1, '1', NULL, '2025-12-17 05:22:26', NULL, NULL),
	(4, '1', 'P', '66399', 'CUST001', '60192782366', NULL, 'MSG_1765968805_0', '000', 4, '', '', 0, 'Bulk SMS from Postman', '', '', 0, '10', NULL, '2025-12-17 05:23:25', NULL, NULL),
	(5, '1', 'P', '66399', 'CUST001', '60122786404', NULL, 'MSG_1765968805_1', '000', 4, '', '', 0, 'Bulk SMS from Postman', '', '', 0, '10', NULL, '2025-12-17 05:23:25', NULL, NULL),
	(6, '1', 'P', '66399', 'CUST001', '60122879404', NULL, 'MSG_1765968805_2', '000', 4, '', '', 0, 'Bulk SMS from Postman', '', '', 0, '10', NULL, '2025-12-17 05:23:25', NULL, NULL),
	(7, '1', 'P', '66399', 'CUST001', '60192782366', '62003', 'MSG_1765968847', '000', 10, '', '', 8, '002854094e509a6c00297b2c0032573aff1a0038002900207a7a4e2d4f208bf4002000200053006b00790020004c006500670065006e0064002000209ad85ea663a88350002a002a002a002000370029002091d182729ece660e00200047006f006c00640065006e0020004c0061006b00730068006d00690020', '', '', 0, '10', NULL, '2025-12-17 05:24:07', NULL, NULL);

-- Dumping structure for table extmt.extmtpush_send_bulk
DROP TABLE IF EXISTS `extmtpush_send_bulk`;
CREATE TABLE IF NOT EXISTS `extmtpush_send_bulk` (
  `row_id` bigint NOT NULL AUTO_INCREMENT,
  `shortcode` varchar(20) DEFAULT NULL,
  `custid` varchar(50) DEFAULT NULL,
  `rmsisdn` varchar(20) DEFAULT NULL,
  `smsisdn` varchar(50) DEFAULT NULL,
  `mtid` varchar(50) DEFAULT NULL,
  `mtprice` varchar(10) DEFAULT NULL,
  `product_type` int DEFAULT NULL,
  `product_code` varchar(50) DEFAULT NULL,
  `keyword` varchar(100) DEFAULT NULL,
  `data_encoding` int DEFAULT NULL,
  `data_str` text,
  `data_url` varchar(500) DEFAULT NULL,
  `url_title` varchar(200) DEFAULT NULL,
  `dnrep` int DEFAULT NULL,
  `group_tag` varchar(50) DEFAULT NULL,
  `telco` varchar(20) DEFAULT NULL,
  `send_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(20) DEFAULT NULL,
  `hex_data` text,
  PRIMARY KEY (`row_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table extmt.extmtpush_send_bulk: ~0 rows (approximately)

-- Dumping structure for table extmt.extmt_mtid
DROP TABLE IF EXISTS `extmt_mtid`;
CREATE TABLE IF NOT EXISTS `extmt_mtid` (
  `row_id` bigint NOT NULL AUTO_INCREMENT,
  `mtid` varchar(100) NOT NULL,
  `custid` varchar(50) NOT NULL,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`row_id`),
  UNIQUE KEY `mtid` (`mtid`),
  KEY `idx_mtid_custid` (`mtid`,`custid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table extmt.extmt_mtid: ~7 rows (approximately)
INSERT INTO `extmt_mtid` (`row_id`, `mtid`, `custid`, `date`) VALUES
	(1, 'MSG_FINAL_1765962888', 'CUST001', '2025-12-17 03:44:48'),
	(2, 'MSG_WORKING_1765962903', 'CUST001', '2025-12-17 03:45:03'),
	(3, 'MSG_1765968746', 'CUST001', '2025-12-17 05:22:26'),
	(4, 'MSG_1765968805_0', 'CUST001', '2025-12-17 05:23:25'),
	(5, 'MSG_1765968805_1', 'CUST001', '2025-12-17 05:23:25'),
	(6, 'MSG_1765968805_2', 'CUST001', '2025-12-17 05:23:25'),
	(7, 'MSG_1765968847', 'CUST001', '2025-12-17 05:24:07');

-- Dumping structure for table extmt.sms_generic_forwardmo_blacklist
DROP TABLE IF EXISTS `sms_generic_forwardmo_blacklist`;
CREATE TABLE IF NOT EXISTS `sms_generic_forwardmo_blacklist` (
  `row_id` bigint NOT NULL AUTO_INCREMENT,
  `message_id` varchar(50) DEFAULT NULL,
  `msisdn` varchar(20) DEFAULT NULL,
  `blacklist_keyword` varchar(100) DEFAULT NULL,
  `blacklist_flag` int DEFAULT '0',
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`row_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table extmt.sms_generic_forwardmo_blacklist: ~0 rows (approximately)

-- Dumping structure for table extmt.sms_generic_forwardmo_details
DROP TABLE IF EXISTS `sms_generic_forwardmo_details`;
CREATE TABLE IF NOT EXISTS `sms_generic_forwardmo_details` (
  `row_id` bigint NOT NULL AUTO_INCREMENT,
  `message_id` varchar(50) DEFAULT NULL,
  `msisdn` varchar(20) DEFAULT NULL,
  `shortcode` varchar(20) DEFAULT NULL,
  `keyword` varchar(100) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`row_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table extmt.sms_generic_forwardmo_details: ~0 rows (approximately)

-- Dumping structure for table extmt.tbl_smpp_dn
DROP TABLE IF EXISTS `tbl_smpp_dn`;
CREATE TABLE IF NOT EXISTS `tbl_smpp_dn` (
  `row_id` bigint NOT NULL AUTO_INCREMENT,
  `smpServiceID` varchar(50) DEFAULT NULL,
  `smppName` varchar(50) DEFAULT NULL,
  `smppId` varchar(50) DEFAULT NULL,
  `smppType` varchar(20) DEFAULT NULL,
  `sender` varchar(50) DEFAULT NULL,
  `recipient` varchar(20) DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT NULL,
  `smppStatus` varchar(50) DEFAULT NULL,
  `DNStatus` varchar(50) DEFAULT NULL,
  `errorCode` varchar(20) DEFAULT NULL,
  `dcs` varchar(10) DEFAULT NULL,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `bytes` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`row_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table extmt.tbl_smpp_dn: ~0 rows (approximately)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
