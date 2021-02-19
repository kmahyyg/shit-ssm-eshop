-- MariaDB dump 10.18  Distrib 10.5.8-MariaDB, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: projacc_shopdb
-- ------------------------------------------------------
-- Server version	10.5.8-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sys_item_cates`
--

DROP TABLE IF EXISTS `sys_item_cates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_item_cates` (
  `name` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id` int(8) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_item_cates`
--

/*!40000 ALTER TABLE `sys_item_cates` DISABLE KEYS */;
INSERT INTO `sys_item_cates` VALUES ('测试商品类别1',1),('测试商品类别2',2),('测试商品类别3',3);
/*!40000 ALTER TABLE `sys_item_cates` ENABLE KEYS */;

--
-- Table structure for table `sys_items`
--

DROP TABLE IF EXISTS `sys_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_items` (
  `cid` int(8) unsigned NOT NULL,
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` decimal(14,2) NOT NULL,
  `status` tinyint(1) unsigned NOT NULL,
  `image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `descr` varchar(5000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shopown` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `shopown` (`shopown`),
  KEY `itemidx` (`name`),
  KEY `sys_items_ibfk_1` (`cid`),
  CONSTRAINT `sys_items_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `sys_item_cates` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_items_ibfk_2` FOREIGN KEY (`shopown`) REFERENCES `sys_users` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_items`
--

/*!40000 ALTER TABLE `sys_items` DISABLE KEYS */;
INSERT INTO `sys_items` VALUES (1,1,'测试商品1',22.12,1,'/static/imgs/avatar.png','测试商品描述1','8ad3980a-18fe-4951-849b-a6a687aaabd8'),(2,2,'测试商品2',33.55,1,'/static/imgs/avatar.png','测试商品描述2','8ad3980a-18fe-4951-849b-a6a687aaabd8'),(2,3,'测试商品3',89.64,1,'/static/imgs/avatar.png','测试描述3','fa21befd-2cdc-4ad0-af2f-2d58afb50fe4');
/*!40000 ALTER TABLE `sys_items` ENABLE KEYS */;

--
-- Table structure for table `sys_orders`
--

DROP TABLE IF EXISTS `sys_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_orders` (
  `oid` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT uuid(),
  `id` int(8) unsigned NOT NULL AUTO_INCREMENT,
  `status` tinyint(1) unsigned NOT NULL,
  `items` varchar(5000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `uid` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `finalPrice` decimal(14,2) NOT NULL,
  `genTime` bigint(12) NOT NULL DEFAULT unix_timestamp(),
  `paidTime` bigint(12) DEFAULT NULL,
  `doneTime` bigint(12) DEFAULT NULL,
  `refundTime` bigint(12) DEFAULT NULL,
  `paymentId` tinyint(1) unsigned NOT NULL,
  `deliveryId` tinyint(1) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `oid` (`oid`),
  UNIQUE KEY `orders_uid` (`oid`,`uid`),
  KEY `uid` (`uid`),
  CONSTRAINT `sys_orders_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `sys_users` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_orders`
--

/*!40000 ALTER TABLE `sys_orders` DISABLE KEYS */;
INSERT INTO `sys_orders` VALUES ('917decb2-6b04-11eb-8b32-b89a2afc3a6e',1,2,'{\"items\": [{\"itemId\": 1, \"itemNo\": 2}]}','a09ccf60-daba-4507-bfa0-d1688ee5f2ea',44.24,1612895292,NULL,NULL,NULL,1,1);
/*!40000 ALTER TABLE `sys_orders` ENABLE KEYS */;

--
-- Table structure for table `sys_user_cart`
--

DROP TABLE IF EXISTS `sys_user_cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_cart` (
  `id` int(8) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `items` varchar(5000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uid` (`uid`),
  KEY `uididx` (`uid`),
  CONSTRAINT `sys_user_cart_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `sys_users` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_cart`
--

/*!40000 ALTER TABLE `sys_user_cart` DISABLE KEYS */;
INSERT INTO `sys_user_cart` VALUES (3,'8ad3980a-18fe-4951-849b-a6a687aaabd8','{\"cart\":[]}'),(4,'a09ccf60-daba-4507-bfa0-d1688ee5f2ea','{\"cart\":[{\"itemId\":1,\"itemNum\":2}, {\"itemId\":3,\"itemNum\":5}]}'),(5,'fa21befd-2cdc-4ad0-af2f-2d58afb50fe4','{\"cart\":[]}');
/*!40000 ALTER TABLE `sys_user_cart` ENABLE KEYS */;

--
-- Table structure for table `sys_users`
--

DROP TABLE IF EXISTS `sys_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_users` (
  `username` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `uid` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT uuid(),
  `id` int(8) unsigned NOT NULL AUTO_INCREMENT,
  `phone` bigint(11) NOT NULL,
  `password` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(24) COLLATE utf8mb4_unicode_ci NOT NULL,
  `avatar` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `preferPayment` tinyint(1) unsigned NOT NULL,
  `preferDelivery` tinyint(1) unsigned NOT NULL,
  `addr` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` tinyint(2) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `uid` (`uid`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_users`
--

/*!40000 ALTER TABLE `sys_users` DISABLE KEYS */;
INSERT INTO `sys_users` VALUES ('admin','0c33d2fd-015b-4c60-bc58-df139dc9d7b9',1,12345678902,'$2a$10$veUS/ypYi0uDSkHefZLg8u1P50DI7JxVH6N3SSQ3EEX5TSsOvIOuG','ROLE_ADMIN','/static/imgs/avatar.png',1,1,'addr123',0),('testuser2','8ad3980a-18fe-4951-849b-a6a687aaabd8',4,12345678002,'$2a$10$.079yr9VLx.Jo0TVpNaVcuWNMdZZw5ZKu09kafPTUXyqw1riPt2VK','ROLE_USER,ROLE_SHOPOWNER','/static/imgs/avatar.png',1,1,'addrtest2',0),('testuser5','a09ccf60-daba-4507-bfa0-d1688ee5f2ea',5,12345678005,'$2a$10$OYnQjXYZaCs0XHpJ7i0GeeqCmqUsL20.izGh0sp1Yj9znrbkr22FG','ROLE_USER','/static/imgs/avatar.png',1,1,'addrtest5',0),('testuser9','fa21befd-2cdc-4ad0-af2f-2d58afb50fe4',6,12345678909,'$2a$10$1s79rdYKIErDIHhMf8QIfebC5xv.EIB3vq2kxCE66azLgH/3DIeoS','ROLE_USER,ROLE_SHOPOWNER','/static/imgs/avatar.png',1,1,'testaddr09',0);
/*!40000 ALTER TABLE `sys_users` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-02-15 20:39:46
