CREATE DATABASE  IF NOT EXISTS `document_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `document_db`;
-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: document_db
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `level_id` int NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_categories_level_id` (`level_id`),
  KEY `idx_categories_is_deleted` (`is_deleted`),
  CONSTRAINT `categories_ibfk_1` FOREIGN KEY (`level_id`) REFERENCES `levels` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (11,'Общее',0,1,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(12,'Здравоохранение',0,1,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(13,'Образование',0,1,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(14,'Строительство',0,1,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(15,'Транспорт',0,1,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(16,'Финансы',0,1,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(17,'Промышленность',0,1,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(18,'Экономика',0,1,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(19,'Торговля',0,1,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(20,'Информационные технологии',0,1,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(21,'Общее',0,2,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(22,'Здравоохранение',0,2,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(23,'Образование',0,2,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(24,'Строительство',0,2,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(25,'Транспорт',0,2,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(26,'Финансы',0,2,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(27,'Промышленность',0,2,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(28,'Экономика',0,2,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(29,'Торговля',0,2,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(30,'Информационные технологии',0,2,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(31,'Общее',0,3,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(32,'Здравоохранение',0,3,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(33,'Образование',0,3,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(34,'Строительство',0,3,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(35,'Транспорт',0,3,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(36,'Финансы',0,3,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(37,'Промышленность',0,3,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(38,'Экономика',0,3,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(39,'Торговля',0,3,'2025-11-07 09:41:11','2025-11-07 09:41:11'),(40,'Информационные технологии',0,3,'2025-11-07 09:41:11','2025-11-07 09:41:11');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `document_categories`
--

DROP TABLE IF EXISTS `document_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `document_categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `document_id` int NOT NULL,
  `category_id` int NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_document_category` (`document_id`,`category_id`),
  KEY `idx_document_categories_document_id` (`document_id`),
  KEY `idx_document_categories_category_id` (`category_id`),
  CONSTRAINT `document_categories_ibfk_1` FOREIGN KEY (`document_id`) REFERENCES `documents` (`id`) ON DELETE CASCADE,
  CONSTRAINT `document_categories_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `document_categories`
--

LOCK TABLES `document_categories` WRITE;
/*!40000 ALTER TABLE `document_categories` DISABLE KEYS */;
INSERT INTO `document_categories` VALUES (2,1,11,'2025-11-07 12:00:08');
/*!40000 ALTER TABLE `document_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `documents`
--

DROP TABLE IF EXISTS `documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documents` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `publication_date` datetime DEFAULT NULL,
  `status` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `source_url` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_documents_publication_date` (`publication_date`),
  KEY `idx_documents_status` (`status`),
  KEY `idx_documents_is_deleted` (`is_deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documents`
--

LOCK TABLES `documents` WRITE;
/*!40000 ALTER TABLE `documents` DISABLE KEYS */;
INSERT INTO `documents` VALUES (1,'ГОСТ Р 59162-2020',NULL,'действующий','https://rst.gov.ru:8443/file-service/file/load/1699366818935','2025-11-07 11:58:58','2025-11-07 11:59:09',0);
/*!40000 ALTER TABLE `documents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `levels`
--

DROP TABLE IF EXISTS `levels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `levels` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `levels`
--

LOCK TABLES `levels` WRITE;
/*!40000 ALTER TABLE `levels` DISABLE KEYS */;
INSERT INTO `levels` VALUES (1,'Федеральный уровень',0,'2025-11-07 09:36:07','2025-11-07 09:36:07'),(2,'Технический уровень',0,'2025-11-07 09:36:07','2025-11-07 09:36:07'),(3,'Образовательный уровень',0,'2025-11-07 09:36:07','2025-11-07 09:36:07');
/*!40000 ALTER TABLE `levels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sections`
--

DROP TABLE IF EXISTS `sections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sections` (
  `id` int NOT NULL AUTO_INCREMENT,
  `document_id` int NOT NULL,
  `title` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `order` int DEFAULT '0',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sections_document_id` (`document_id`),
  KEY `idx_sections_order` (`order`),
  KEY `idx_sections_is_deleted` (`is_deleted`),
  CONSTRAINT `sections_ibfk_1` FOREIGN KEY (`document_id`) REFERENCES `documents` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sections`
--

LOCK TABLES `sections` WRITE;
/*!40000 ALTER TABLE `sections` DISABLE KEYS */;
/*!40000 ALTER TABLE `sections` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-07 17:00:38
