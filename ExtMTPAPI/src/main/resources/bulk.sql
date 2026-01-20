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


-- Dumping database structure for bulk_config
DROP DATABASE IF EXISTS `bulk_config`;
CREATE DATABASE IF NOT EXISTS `bulk_config` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bulk_config`;

-- Dumping structure for table bulk_config.blacklist
DROP TABLE IF EXISTS `blacklist`;
CREATE TABLE IF NOT EXISTS `blacklist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `msisdn` varchar(20) NOT NULL,
  `shortcode` varchar(10) NOT NULL,
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_msisdn_shortcode` (`msisdn`,`shortcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bulk_config.blacklist: ~0 rows (approximately)

-- Dumping structure for table bulk_config.bulk_destination_sms
DROP TABLE IF EXISTS `bulk_destination_sms`;
CREATE TABLE IF NOT EXISTS `bulk_destination_sms` (
  `id` int NOT NULL AUTO_INCREMENT,
  `custid` varchar(50) NOT NULL,
  `local_flag` char(1) DEFAULT '0',
  `int_flag` char(1) DEFAULT '0',
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_custid` (`custid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bulk_config.bulk_destination_sms: ~0 rows (approximately)
INSERT INTO `bulk_destination_sms` (`id`, `custid`, `local_flag`, `int_flag`, `created_date`) VALUES
	(1, 'CUST001', '0', '0', '2025-12-16 11:16:49');

-- Dumping structure for table bulk_config.cpip
DROP TABLE IF EXISTS `cpip`;
CREATE TABLE IF NOT EXISTS `cpip` (
  `row_id` int NOT NULL AUTO_INCREMENT,
  `shortcode` varchar(10) DEFAULT NULL,
  `cpidentity` varchar(50) DEFAULT NULL,
  `cp_ip` varchar(50) DEFAULT NULL,
  `hlr_flag` char(1) DEFAULT '1',
  `active` char(1) DEFAULT '1',
  PRIMARY KEY (`row_id`),
  KEY `idx_shortcode_cpidentity_ip` (`shortcode`,`cpidentity`,`cp_ip`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bulk_config.cpip: ~4 rows (approximately)
INSERT INTO `cpip` (`row_id`, `shortcode`, `cpidentity`, `cp_ip`, `hlr_flag`, `active`) VALUES
	(5, '66399', 'CUST001', '127.0.0.1', '1', '1'),
	(6, '66399', 'CUST001', 'localhost', '1', '1'),
	(7, '66399', 'CUST001', '0:0:0:0:0:0:0:1', '1', '1'),
	(8, '66399', 'CUST001', '::1', '1', '1');

-- Dumping structure for table bulk_config.cp_package
DROP TABLE IF EXISTS `cp_package`;
CREATE TABLE IF NOT EXISTS `cp_package` (
  `id` int NOT NULL AUTO_INCREMENT,
  `custid` varchar(50) NOT NULL,
  `shortcode` varchar(10) NOT NULL,
  `active` char(1) DEFAULT '1',
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_custid_shortcode` (`custid`,`shortcode`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bulk_config.cp_package: ~1 rows (approximately)
INSERT INTO `cp_package` (`id`, `custid`, `shortcode`, `active`, `created_date`) VALUES
	(1, 'CUST001', '66399', '1', '2025-12-16 11:09:27');

-- Dumping structure for table bulk_config.customer_credit
DROP TABLE IF EXISTS `customer_credit`;
CREATE TABLE IF NOT EXISTS `customer_credit` (
  `row_id` int NOT NULL AUTO_INCREMENT,
  `custid` varchar(50) NOT NULL,
  `credit_balance` decimal(10,2) DEFAULT '0.00',
  `credit_limit` decimal(10,2) DEFAULT NULL,
  `last_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `active` char(1) DEFAULT '1',
  PRIMARY KEY (`row_id`),
  UNIQUE KEY `custid` (`custid`),
  KEY `idx_custid` (`custid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bulk_config.customer_credit: ~1 rows (approximately)
INSERT INTO `customer_credit` (`row_id`, `custid`, `credit_balance`, `credit_limit`, `last_updated`, `active`) VALUES
	(1, 'CUST001', 1000.00, 10000.00, '2025-12-16 11:22:20', '1');

-- Dumping structure for table bulk_config.masking_id
DROP TABLE IF EXISTS `masking_id`;
CREATE TABLE IF NOT EXISTS `masking_id` (
  `id` int NOT NULL AUTO_INCREMENT,
  `custid` varchar(50) NOT NULL,
  `masking_id` varchar(20) NOT NULL,
  `active` char(1) DEFAULT '1',
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_custid_masking` (`custid`,`masking_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bulk_config.masking_id: ~4 rows (approximately)
INSERT INTO `masking_id` (`id`, `custid`, `masking_id`, `active`, `created_date`) VALUES
	(9, 'CUST001', '62003', '1', '2025-12-17 10:45:04'),
	(10, 'CUST001', '66399', '1', '2025-12-17 10:45:04'),
	(11, 'CUST001', 'TESTCO', '1', '2025-12-17 10:45:04'),
	(12, 'CUST001', 'SMS', '1', '2025-12-17 10:45:04');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
