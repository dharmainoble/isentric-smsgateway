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
CREATE DATABASE IF NOT EXISTS `extmt` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `extmt`;

-- Dumping structure for table extmt.bulk_credit
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
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table extmt.extmtpush_receive_bulk: ~24 rows (approximately)
INSERT INTO `extmtpush_receive_bulk` (`row_id`, `bill_flag`, `process_flag`, `shortcode`, `custid`, `rmsisdn`, `smsisdn`, `mtid`, `mtprice`, `product_type`, `product_code`, `keyword`, `data_encoding`, `data_str`, `data_url`, `url_title`, `dnrep`, `group_tag`, `ewig_flag`, `received_date`, `sent_date`, `delivery_status`) VALUES
	(1, '1', 'P', '66399', 'CUST001', '60123456789', NULL, 'MSG_FINAL_1765962888', NULL, 4, NULL, '', 0, 'SUCCESS! Data inserted via Postman', NULL, '', 1, NULL, NULL, '2025-12-17 03:44:48', NULL, NULL),
	(2, '1', 'P', '66399', 'CUST001', '60123456789', NULL, 'MSG_WORKING_1765962903', NULL, 4, NULL, '', 0, 'Test SMS', NULL, '', 1, NULL, NULL, '2025-12-17 03:45:03', NULL, NULL),
	(3, '1', 'P', '66399', 'CUST001', '60192782366', '62003', 'MSG_1765968746', '000', 4, '', '', 0, 'Test SMS from Postman POST', '', '', 1, '1', NULL, '2025-12-17 05:22:26', NULL, NULL),
	(4, '1', 'P', '66399', 'CUST001', '60192782366', NULL, 'MSG_1765968805_0', '000', 4, '', '', 0, 'Bulk SMS from Postman', '', '', 0, '10', NULL, '2025-12-17 05:23:25', NULL, NULL),
	(5, '1', 'P', '66399', 'CUST001', '60122786404', NULL, 'MSG_1765968805_1', '000', 4, '', '', 0, 'Bulk SMS from Postman', '', '', 0, '10', NULL, '2025-12-17 05:23:25', NULL, NULL),
	(6, '1', 'P', '66399', 'CUST001', '60122879404', NULL, 'MSG_1765968805_2', '000', 4, '', '', 0, 'Bulk SMS from Postman', '', '', 0, '10', NULL, '2025-12-17 05:23:25', NULL, NULL),
	(7, '1', 'P', '66399', 'CUST001', '60192782366', '62003', 'MSG_1765968847', '000', 10, '', '', 8, '002854094e509a6c00297b2c0032573aff1a0038002900207a7a4e2d4f208bf4002000200053006b00790020004c006500670065006e0064002000209ad85ea663a88350002a002a002a002000370029002091d182729ece660e00200047006f006c00640065006e0020004c0061006b00730068006d00690020', '', '', 0, '10', NULL, '2025-12-17 05:24:07', NULL, NULL),
	(8, '1', 'P', '39398', 'isentric_demo', '60192782366', '62003', 'MSG_1770131092', '000', 4, '', '', 0, 'Test SMS from Postman POST', '', '', 1, '1', NULL, '2026-02-03 09:34:52', NULL, NULL),
	(9, '1', 'P', '39398', 'isentric_demo', '60192782366', '62003', 'MSG_1770135707', '000', 4, '', '', 0, 'Test SMS from Postman POST', '', '', 1, '1', NULL, '2026-02-03 10:51:47', NULL, NULL),
	(10, '1', 'P', '39398', 'isentric_demo', '60192782366', '62003', 'MSG_1770135980', '000', 4, '', '', 0, 'Test SMS from Postman POST', '', '', 1, '1', NULL, '2026-02-03 10:56:21', NULL, NULL),
	(11, '1', 'P', '39398', 'isentric_demo', '60192782366', '62003', 'MSG_1770136369', '000', 4, '', '', 0, 'Test SMS from Postman POST', '', '', 1, '1', NULL, '2026-02-03 11:02:49', NULL, NULL),
	(12, '1', 'P', '39398', 'isentric_demo', '60163380820', '62003', 'MSG_1770136543', '000', 4, '', '', 0, 'Test SMS from Postman POST', '', '', 1, '1', NULL, '2026-02-03 11:05:43', NULL, NULL),
	(13, '1', 'P', '10086', 'isentric_demo', '60163380820', '62003', 'MSG_1770136775', '000', 4, '', '', 0, 'Test SMS from Postman POST', '', '', 1, '1', NULL, '2026-02-03 11:09:35', NULL, NULL),
	(14, '1', 'P', '10086', 'isentric_demo', '60163380820', '62003', 'MSG_1770137214', '000', 4, '', '', 0, 'Test SMS from Postman POST', '', '', 1, '1', NULL, '2026-02-03 11:16:54', NULL, NULL),
	(15, '1', 'P', '10086', 'isentric_demo', '60163380820', '62003', 'MSG_1770137304', '000', 4, '', '', 0, 'Test SMS from Postman POST', '', '', 1, '1', NULL, '2026-02-03 11:18:24', NULL, NULL),
	(16, '1', 'P', '10086', 'isentric_demo', '60125762417', '62003', 'MSG_1770137471', '000', 4, '', '', 0, 'Test SMS from Postman POST', '', '', 1, '1', NULL, '2026-02-03 11:21:11', NULL, NULL),
	(17, '1', 'P', '10086', 'isentric_demo', '60125762417', '62003', 'MSG_1770137511', '000', 4, '', '', 0, 'Test SMS from Postman POST', '', '', 1, '1', NULL, '2026-02-03 11:21:51', NULL, NULL),
	(18, '1', 'P', '10086', 'isentric_demo', '60125762417', '62003', 'MSG_1770138112', '000', 10, '', '', 8, '002854094e509a6c00297b2c0032573aff1a0038002900207a7a4e2d4f208bf4002000200053006b00790020004c006500670065006e0064002000209ad85ea663a88350002a002a002a002000370029002091d182729ece660e00200047006f006c00640065006e0020004c0061006b00730068006d00690020', '', '', 0, '10', NULL, '2026-02-03 11:31:52', NULL, NULL),
	(19, '1', 'P', '10086', 'isentric_demo', '60125762417', '62003', 'MSG_1770653517', '000', 10, '', '', 8, 'Test', '', '', 0, '10', NULL, '2026-02-09 10:41:58', NULL, NULL),
	(20, '1', 'P', '10086', 'isentric_demo', '60121111257807', '62003', 'MSG_1770653727', '000', 10, '', '', 8, 'Test', '', '', 0, '10', NULL, '2026-02-09 10:45:27', NULL, NULL),
	(21, '1', 'P', '10086', 'isentric_demo', '60121111257807', '62003', 'MSG_1770659306', '000', 10, '', '', 8, 'Test', '', '', 0, '10', NULL, '2026-02-09 12:18:26', NULL, NULL),
	(22, '1', 'P', '10086', 'isentric_demo', '601159149006', '62003', 'MSG_1770659414', '000', 10, '', '', 8, 'Test', '', '', 0, '10', NULL, '2026-02-09 12:20:14', NULL, NULL),
	(23, '1', 'P', '10086', 'isentric_demo', '601159149006', '62003', 'MSG_1770659474', '000', 10, '', '', 8, 'Test', '', '', 0, '10', NULL, '2026-02-09 12:21:14', NULL, NULL),
	(24, '1', 'P', '10086', 'isentric_demo', '601159149006', '62003', 'MSG_1770660681', '000', 10, '', '', 8, 'Test', '', '', 0, '10', NULL, '2026-02-09 12:41:21', NULL, NULL);

-- Dumping structure for table extmt.extmtpush_send_bulk
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
CREATE TABLE IF NOT EXISTS `extmt_mtid` (
  `row_id` bigint NOT NULL AUTO_INCREMENT,
  `mtid` varchar(100) NOT NULL,
  `custid` varchar(50) NOT NULL,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`row_id`),
  UNIQUE KEY `mtid` (`mtid`),
  KEY `idx_mtid_custid` (`mtid`,`custid`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table extmt.extmt_mtid: ~24 rows (approximately)
INSERT INTO `extmt_mtid` (`row_id`, `mtid`, `custid`, `date`) VALUES
	(1, 'MSG_FINAL_1765962888', 'CUST001', '2025-12-17 03:44:48'),
	(2, 'MSG_WORKING_1765962903', 'CUST001', '2025-12-17 03:45:03'),
	(3, 'MSG_1765968746', 'CUST001', '2025-12-17 05:22:26'),
	(4, 'MSG_1765968805_0', 'CUST001', '2025-12-17 05:23:25'),
	(5, 'MSG_1765968805_1', 'CUST001', '2025-12-17 05:23:25'),
	(6, 'MSG_1765968805_2', 'CUST001', '2025-12-17 05:23:25'),
	(7, 'MSG_1765968847', 'CUST001', '2025-12-17 05:24:07'),
	(8, 'MSG_1770131092', 'isentric_demo', '2026-02-03 09:34:52'),
	(9, 'MSG_1770135707', 'isentric_demo', '2026-02-03 10:51:47'),
	(10, 'MSG_1770135980', 'isentric_demo', '2026-02-03 10:56:21'),
	(11, 'MSG_1770136369', 'isentric_demo', '2026-02-03 11:02:49'),
	(12, 'MSG_1770136543', 'isentric_demo', '2026-02-03 11:05:43'),
	(13, 'MSG_1770136775', 'isentric_demo', '2026-02-03 11:09:35'),
	(14, 'MSG_1770137214', 'isentric_demo', '2026-02-03 11:16:55'),
	(15, 'MSG_1770137304', 'isentric_demo', '2026-02-03 11:18:24'),
	(16, 'MSG_1770137471', 'isentric_demo', '2026-02-03 11:21:11'),
	(17, 'MSG_1770137511', 'isentric_demo', '2026-02-03 11:21:51'),
	(18, 'MSG_1770138112', 'isentric_demo', '2026-02-03 11:31:52'),
	(19, 'MSG_1770653517', 'isentric_demo', '2026-02-09 10:41:58'),
	(20, 'MSG_1770653727', 'isentric_demo', '2026-02-09 10:45:27'),
	(21, 'MSG_1770659306', 'isentric_demo', '2026-02-09 12:18:26'),
	(22, 'MSG_1770659414', 'isentric_demo', '2026-02-09 12:20:14'),
	(23, 'MSG_1770659474', 'isentric_demo', '2026-02-09 12:21:14'),
	(24, 'MSG_1770660681', 'isentric_demo', '2026-02-09 12:41:21');

-- Dumping structure for table extmt.route_config
CREATE TABLE IF NOT EXISTS `route_config` (
  `routeName` varchar(50) NOT NULL,
  `configType` varchar(50) NOT NULL,
  `configFile` varchar(100) NOT NULL,
  `status` int NOT NULL DEFAULT '0',
  `moFlag` tinyint unsigned NOT NULL DEFAULT '0',
  `IPAddress` varchar(20) NOT NULL,
  `apiKey` varchar(150) DEFAULT '',
  `startup_115` int NOT NULL,
  `startup_118` int NOT NULL,
  `startup_161` int NOT NULL,
  PRIMARY KEY (`routeName`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3;

-- Dumping data for table extmt.route_config: 47 rows
/*!40000 ALTER TABLE `route_config` DISABLE KEYS */;
INSERT INTO `route_config` (`routeName`, `configType`, `configFile`, `status`, `moFlag`, `IPAddress`, `apiKey`, `startup_115`, `startup_118`, `startup_161`) VALUES
	('HTTP_CELCOM', 'http', '/WEB-INF/HttpRoute.properties', 0, 0, '192.168.26.163', '', 0, 0, 0),
	('HTTP_DIGI', 'http', '/WEB-INF/HttpRoute.properties', 0, 0, '192.168.26.100', '', 0, 0, 0),
	('HTTP_MAXIS_ESMS', 'http', '/WEB-INF/HttpRoute.properties', 0, 0, '192.168.26.100', '', 0, 0, 0),
	('HTTP_MAXIS_ESMS_PREFERRED', 'http', '/home/arun/IdeaProjects/isentric-smsgateway/BulkGateway/src/main/resources/HttpRoute.properties', 0, 0, '192.168.26.100', '', 0, 0, 0),
	('HTTP_MAXIS_ESMS_TAC', 'http', '/WEB-INF/HttpRoute.properties', 0, 0, '192.168.26.100', '', 0, 0, 0),
	('SMPP_RADIUS_NEW', 'smpp', '/WEB-INF/radius_isentric_new.conf', 0, 0, '', '', 1, 1, 1),
	('SMPP_RADIUS_NEW2', 'smpp', '/WEB-INF/jsms_smpp_radius_new2.conf', 0, 0, '', '', 1, 1, 1),
	('WSDL_DIGI', 'wsdl', '/WEB-INF/WsdlRoute.properties', 0, 0, '192.168.26.100', '', 0, 0, 0),
	('CHARGE_DIGI', 'charge', '/WEB-INF/ChargeDigi.properties', 0, 0, '', '', 0, 0, 1),
	('WSDL_DIGI_ISEN', 'wsdl', '/home/arun/IdeaProjects/isentric-smsgateway/BulkGateway/src/main/resources/WsdlRoute.properties', 0, 1, '192.168.26.100', '', 0, 0, 0),
	('HTTP_SILVERSTREET', 'http', '/WEB-INF/HttpRoute.properties', 0, 0, '192.168.26.100', '', 0, 0, 0),
	('HTTP_SILVERSTREET1', 'http', '/WEB-INF/HttpRoute.properties', 0, 0, '', '', 0, 0, 1),
	('SMPP_TUNETALK_115', 'smpp', '/WEB-INF/jsms_smpp_tunetalk_115.conf', 0, 0, '', '', 1, 1, 1),
	('HTTP_SILVERSTREET2', 'http', '/WEB-INF/HttpRoute.properties', 1, 0, '', '', 0, 0, 1),
	('SMPP_UV_MSIA', 'smpp', '/WEB-INF/jsms_smpp_uv_malaysia.conf', 1, 0, '', '', 1, 1, 1),
	('WSDL_DIGI_66990', 'wsdl', '/WEB-INF/WsdlRoute.properties', 0, 0, '192.168.26.100', '', 0, 0, 0),
	('HTTP_SILVERSTREET3', 'http', '/WEB-INF/HttpRoute.properties', 0, 0, '192.168.26.100', '', 0, 0, 0),
	('SMPP_UV_ASIA', 'smpp', '/WEB-INF/jsms_smpp_uv_asia.conf', 1, 0, '192.168.26.163', '', 1, 1, 1),
	('SMPP_TUNETALK_118', 'smpp', '/WEB-INF/jsms_smpp_tunetalk_118.conf', 0, 0, '192.168.26.100', '', 1, 1, 1),
	('SMPP_RADIUS_NEW_118', 'smpp', '/WEB-INF/jsms_smpp_radius_new_118.conf', 0, 0, '192.168.26.100', '', 1, 1, 1),
	('SMPP_CITI_115', 'smpp', '/WEB-INF/jsms_smpp_citi_115.conf', 1, 1, '', '', 0, 0, 0),
	('SMPP_CITI_118', 'smpp', '/WEB-INF/jsms_smpp_citi_118.conf', 1, 1, '192.168.26.100', '', 1, 0, 0),
	('SMPP_TUNETALK_NOWSMS', 'smpp', '/WEB-INF/jsms_smpp_tunetalk_nowsms.conf', 0, 0, '192.168.26.163', '', 1, 1, 1),
	('SMPP_BANKISLAM_115', 'smpp', '/WEB-INF/jsms_smpp_bankislam_115.conf', 1, 0, '', '', 0, 0, 1),
	('HTTP_INFOBIP', 'http', '/home/arun/IdeaProjects/isentric-smsgateway/BulkGateway/src/main/resources/HttpRouteNew.properties', 0, 0, '192.168.26.163', '', 0, 0, 0),
	('SMPP_ALEX_115', 'smpp', '/WEB-INF/jsms_smpp_alex.conf', 1, 1, '', '', 0, 0, 1),
	('HTTP_UMOBILE', 'http', '/home/arun/IdeaProjects/isentric-smsgateway/BulkGateway/src/main/resources/HttpRouteNew.properties', 0, 0, '192.168.26.163', '', 0, 0, 0),
	('HTTP_XOX', 'http', '/home/arun/IdeaProjects/isentric-smsgateway/BulkGateway/src/main/resources/HttpRouteNew.properties', 0, 0, '192.168.26.163', '', 0, 0, 0),
	('WSDL_DIGI_62226', 'wsdl', '/WEB-INF/WsdlRoute.properties', 0, 0, '', '', 0, 0, 0),
	('HTTP_GINSMS', 'http', '/WEB-INF/HttpRoute.properties', 0, 0, '', '', 0, 0, 0),
	('SMPP_RADIUS_SILVERSTREET', 'smpp', '/WEB-INF/jsms_smpp_silverstreet.conf', 0, 0, '192.168.26.100', '', 1, 1, 1),
	('SMPP_YES', 'smpp', '/WEB-INF/jsms_smpp_firemobile.conf', 0, 0, '', '', 0, 0, 0),
	('SMPP_DST', 'smpp', '/WEB-INF/jsms_smpp_dst.conf', 0, 0, '', '', 1, 0, 1),
	('HTTP_TRIOMOBILE_MODEM', 'http', '/WEB-INF/HttpRoute.properties', 0, 0, '192.168.26.163', '95a896dd58853860510fb3ad45930063a4cb3d4044a39446a272782e76e86f78', 1, 1, 1),
	('HTTP_TRIOMOBILE', 'http', '/WEB-INF/HttpRoute.properties', 0, 0, '192.168.26.163', '95a896dd58853860510fb3ad45930063a4cb3d4044a39446a272782e76e86f78', 1, 1, 1),
	('SMPP_P1', 'smpp', '/WEB-INF/jsms_smpp_p1.conf', 0, 1, '', '', 0, 1, 0),
	('HTTP_INFOBIP_MULTROUTE', 'http', '/home/arun/IdeaProjects/isentric-smsgateway/BulkGateway/src/main/resources/HttpRoute.properties', 0, 0, '192.168.26.100', '', 0, 0, 0),
	('HTTP_INFOBIP_INTERNATIONAL', 'http', '/home/arun/IdeaProjects/isentric-smsgateway/BulkGateway/src/main/resources/HttpRoute.properties', 0, 0, '192.168.26.100', '', 0, 0, 0),
	('HTTP_FIREMOBILE', 'http', '/WEB-INF/HttpRoute.properties', 0, 0, '192.168.26.163', '', 0, 0, 0),
	('HTTP_WEBE', 'http', '/home/arun/IdeaProjects/isentric-smsgateway/BulkGateway/src/main/resources/HttpRouteNew.properties', 0, 0, '192.168.26.163', '', 0, 0, 0),
	('HTTP_TATA', 'http', '/WEB-INF/HttpRoute.properties', 0, 0, '192.168.26.163', '', 0, 0, 0),
	('HTTP_ICE', 'http', '/WEB-INF/HttpRoute.properties', 0, 0, '192.168.26.163', '', 0, 0, 0),
	('HTTP_ICE_TAC', 'http', '/WEB-INF/HttpRoute.properties', 0, 0, '', '', 0, 0, 0),
	('HTTP_TUNETALK', 'http', '/home/arun/IdeaProjects/isentric-smsgateway/BulkGateway/src/main/resources/HttpRoute.properties', 0, 0, '192.168.26.163', '', 0, 0, 0),
	('HTTP_SOPRANO', 'http', '/WEB-INF/HttpRoute.properties', 0, 0, '192.168.26.163', '', 0, 0, 0),
	('HTTP_SOPRANO_TAC', 'http', '/WEB-INF/HttpRoute.properties', 0, 0, '192.168.26.163', '', 0, 0, 0),
	('HTTP_MAXIS_ESMS_PREFERRED_62226', 'http', '/home/arun/IdeaProjects/isentric-smsgateway/BulkGateway/src/main/resources/HttpRoute.properties', 0, 0, '192.168.26.100', '', 0, 0, 0);
/*!40000 ALTER TABLE `route_config` ENABLE KEYS */;

-- Dumping structure for table extmt.sms_generic_forwardmo_blacklist
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
