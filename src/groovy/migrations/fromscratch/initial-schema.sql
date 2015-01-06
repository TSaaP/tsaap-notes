-- MySQL dump 10.13  Distrib 5.1.69, for redhat-linux-gnu (x86_64)
--
-- Host: localhost    Database: tsaap-notes
-- ------------------------------------------------------
-- Server version	5.1.69

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activation_key`
--

DROP TABLE IF EXISTS `activation_key`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activation_key` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `version` bigint(20) NOT NULL,
    `activation_email_sent` tinyint(1) NOT NULL,
    `activation_key` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `date_created` datetime NOT NULL,
    `user_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKAFE4D3B62E7CCBC2` (`user_id`),
    CONSTRAINT `FKAFE4D3B62E7CCBC2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bookmark`
--

DROP TABLE IF EXISTS `bookmark`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bookmark` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `date_created` datetime NOT NULL,
    `note_id` bigint(20) NOT NULL,
    `user_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK7787A536B958760E` (`note_id`),
    KEY `FK7787A5362E7CCBC2` (`user_id`),
    CONSTRAINT `FK7787A5362E7CCBC2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK7787A536B958760E` FOREIGN KEY (`note_id`) REFERENCES `note` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `context`
--

DROP TABLE IF EXISTS `context`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `context` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `context_name` varchar(1024) COLLATE utf8_unicode_ci NOT NULL,
    `date_created` datetime NOT NULL,
    `description_as_note` varchar(280) COLLATE utf8_unicode_ci DEFAULT NULL,
    `last_updated` datetime NOT NULL,
    `owner_id` bigint(20) NOT NULL,
    `owner_is_teacher` tinyint(1) NOT NULL,
    `url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK38B735AF9A637BDA` (`owner_id`),
    CONSTRAINT `FK38B735AF9A637BDA` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `context_follower`
--

DROP TABLE IF EXISTS `context_follower`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `context_follower` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `context_id` bigint(20) NOT NULL,
    `date_created` datetime NOT NULL,
    `follower_id` bigint(20) NOT NULL,
    `follower_is_teacher` tinyint(1) NOT NULL,
    `is_no_more_subscribed` tinyint(1) NOT NULL,
    `unsusbscription_date` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKEF2B10AECD492546` (`context_id`),
    KEY `FKEF2B10AE96BFD28F` (`follower_id`),
    CONSTRAINT `FKEF2B10AE96BFD28F` FOREIGN KEY (`follower_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FKEF2B10AECD492546` FOREIGN KEY (`context_id`) REFERENCES `context` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `live_session`
--

DROP TABLE IF EXISTS `live_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `live_session` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `version` bigint(20) NOT NULL,
    `date_created` datetime NOT NULL,
    `end_date` datetime DEFAULT NULL,
    `note_id` bigint(20) NOT NULL,
    `start_date` datetime DEFAULT NULL,
    `status` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
    `result_matrix_as_json` text COLLATE utf8_unicode_ci,
    PRIMARY KEY (`id`),
    KEY `FK6C792E43B958760E` (`note_id`),
    CONSTRAINT `FK6C792E43B958760E` FOREIGN KEY (`note_id`) REFERENCES `note` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `live_session_response`
--

DROP TABLE IF EXISTS `live_session_response`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `live_session_response` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `version` bigint(20) NOT NULL,
    `answer_list_as_string` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
    `live_session_id` bigint(20) NOT NULL,
    `percent_credit` float DEFAULT NULL,
    `user_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK9E4F72BD7BA22C99` (`live_session_id`),
    KEY `FK9E4F72BD2E7CCBC2` (`user_id`),
    CONSTRAINT `FK9E4F72BD2E7CCBC2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK9E4F72BD7BA22C99` FOREIGN KEY (`live_session_id`) REFERENCES `live_session` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2580 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `note`
--

DROP TABLE IF EXISTS `note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `note` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `author_id` bigint(20) NOT NULL,
    `content` varchar(280) COLLATE utf8_unicode_ci NOT NULL,
    `context_id` bigint(20) DEFAULT NULL,
    `date_created` datetime NOT NULL,
    `parent_note_id` bigint(20) DEFAULT NULL,
    `fragment_tag_id` bigint(20) DEFAULT NULL,
    `score` int(11) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK33AFF28F35BE02` (`author_id`),
    KEY `FK33AFF2CD492546` (`context_id`),
    KEY `FK33AFF2C87CDB79` (`parent_note_id`),
    KEY `FK33AFF273CC1035` (`fragment_tag_id`),
    CONSTRAINT `FK33AFF273CC1035` FOREIGN KEY (`fragment_tag_id`) REFERENCES `tag` (`id`),
    CONSTRAINT `FK33AFF28F35BE02` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK33AFF2C87CDB79` FOREIGN KEY (`parent_note_id`) REFERENCES `note` (`id`),
    CONSTRAINT `FK33AFF2CD492546` FOREIGN KEY (`context_id`) REFERENCES `context` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=413 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `note_mention`
--

DROP TABLE IF EXISTS `note_mention`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `note_mention` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `mention_id` bigint(20) NOT NULL,
    `note_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKD2FA629DE177143` (`mention_id`),
    KEY `FKD2FA629DB958760E` (`note_id`),
    CONSTRAINT `FKD2FA629DB958760E` FOREIGN KEY (`note_id`) REFERENCES `note` (`id`),
    CONSTRAINT `FKD2FA629DE177143` FOREIGN KEY (`mention_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `note_tag`
--

DROP TABLE IF EXISTS `note_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `note_tag` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `note_id` bigint(20) NOT NULL,
    `tag_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK5E4355CDB958760E` (`note_id`),
    KEY `FK5E4355CD6A432F66` (`tag_id`),
    CONSTRAINT `FK5E4355CD6A432F66` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`),
    CONSTRAINT `FK5E4355CDB958760E` FOREIGN KEY (`note_id`) REFERENCES `note` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1929 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `resource`
--

DROP TABLE IF EXISTS `resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resource` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `version` bigint(20) NOT NULL,
    `date_created` datetime NOT NULL,
    `description_as_note` varchar(280) COLLATE utf8_unicode_ci DEFAULT NULL,
    `metadata` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
    `url` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `resource_follower`
--

DROP TABLE IF EXISTS `resource_follower`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resource_follower` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `version` bigint(20) NOT NULL,
    `date_created` datetime NOT NULL,
    `follower_id` bigint(20) NOT NULL,
    `resource_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FKB2B73D2F96BFD28F` (`follower_id`),
    KEY `FKB2B73D2F68909C2A` (`resource_id`),
    CONSTRAINT `FKB2B73D2F68909C2A` FOREIGN KEY (`resource_id`) REFERENCES `resource` (`id`),
    CONSTRAINT `FKB2B73D2F96BFD28F` FOREIGN KEY (`follower_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `authority` varchar(12) COLLATE utf8_unicode_ci NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `score`
--

DROP TABLE IF EXISTS `score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `score` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `date_created` datetime NOT NULL,
    `note_id` bigint(20) NOT NULL,
    `user_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK6833E92B958760E` (`note_id`),
    KEY `FK6833E922E7CCBC2` (`user_id`),
    CONSTRAINT `FK6833E922E7CCBC2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK6833E92B958760E` FOREIGN KEY (`note_id`) REFERENCES `note` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=369 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `account_expired` tinyint(1) NOT NULL,
    `account_locked` tinyint(1) NOT NULL,
    `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `enabled` tinyint(1) NOT NULL,
    `first_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `last_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `normalized_username` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `password_expired` tinyint(1) NOT NULL,
    `username` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `version` bigint(20) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=211 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
    `role_id` bigint(20) NOT NULL,
    `user_id` bigint(20) NOT NULL,
    PRIMARY KEY (`role_id`,`user_id`),
    KEY `FK143BF46A895207E2` (`role_id`),
    KEY `FK143BF46A2E7CCBC2` (`user_id`),
    CONSTRAINT `FK143BF46A2E7CCBC2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK143BF46A895207E2` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-01-05 16:09:18
