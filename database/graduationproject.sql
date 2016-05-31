/*
Navicat MySQL Data Transfer

Source Server         : 本地Mysql
Source Server Version : 50173
Source Host           : localhost:3306
Source Database       : graduationproject

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2015-07-08 11:27:28
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `news`
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(1024) DEFAULT NULL,
  `time` timestamp NULL DEFAULT NULL,
  `title` varchar(1024) DEFAULT NULL,
  `keywords` text,
  `description` text,
  `mainbody` text,
  `htmlPosition` varchar(1024) DEFAULT NULL,
  `srcPosition` varchar(1024) DEFAULT NULL,
  `classic` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of news
-- ----------------------------
