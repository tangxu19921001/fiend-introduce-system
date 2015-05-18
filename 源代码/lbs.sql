/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50522
Source Host           : localhost:3306
Source Database       : lbs

Target Server Type    : MYSQL
Target Server Version : 50522
File Encoding         : 65001

Date: 2012-05-30 16:09:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `location`
-- ----------------------------
DROP TABLE IF EXISTS `location`;
CREATE TABLE `location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `turn` int(11) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `time` datetime NOT NULL,
  `user` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ui` (`user`),
  CONSTRAINT `ui` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=447 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of location
-- ----------------------------
INSERT INTO `location` VALUES ('14', '1', '30.2635', '100.115', '2012-05-09 13:25:53', '3');
INSERT INTO `location` VALUES ('15', '1', '22.0651', '30.0037', '2012-05-09 15:15:03', '3');
INSERT INTO `location` VALUES ('16', '1', '30.7735', '129.885', '2012-05-09 16:29:22', '19');
INSERT INTO `location` VALUES ('17', '1', '35.0067', '130.129', '2012-05-09 16:29:40', '3');
INSERT INTO `location` VALUES ('18', '1', '36.0871', '135.333', '2012-05-09 18:43:28', '3');
INSERT INTO `location` VALUES ('19', '1', '22.2123', '144.316', '2012-05-09 22:59:15', '3');
INSERT INTO `location` VALUES ('20', '1', '25.2561', '90.4561', '2012-05-09 19:00:53', '19');
INSERT INTO `location` VALUES ('21', '1', '22.3136', '92.0971', '2012-05-09 20:01:39', '19');
INSERT INTO `location` VALUES ('22', '1', '28.1131', '122.201', '2012-05-09 23:02:19', '19');
INSERT INTO `location` VALUES ('23', '1', '34.7654', '110.293', '2012-05-09 23:02:54', '19');
INSERT INTO `location` VALUES ('24', '1', '56.7351', '84.6653', '2012-05-09 23:22:43', '19');
INSERT INTO `location` VALUES ('25', '1', '25.0961', '78.7263', '2012-05-11 00:01:55', '20');
INSERT INTO `location` VALUES ('26', '1', '9.08541', '55.2634', '2012-05-11 00:02:42', '20');
INSERT INTO `location` VALUES ('27', '1', '77.9653', '49.9753', '2012-05-11 01:18:20', '20');
INSERT INTO `location` VALUES ('28', '1', '30.8964', '120.876', '2012-05-14 15:27:26', '3');
INSERT INTO `location` VALUES ('33', '1', '30.5264', '120.123', '2012-05-15 17:30:14', '6');
INSERT INTO `location` VALUES ('34', '1', '30.0765', '120.887', '2012-05-15 17:31:05', '6');
INSERT INTO `location` VALUES ('35', '1', '30.3018', '120.769', '2012-05-15 17:32:16', '6');

-- ----------------------------
-- Table structure for `relationship`
-- ----------------------------
DROP TABLE IF EXISTS `relationship`;
CREATE TABLE `relationship` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user1` int(11) NOT NULL,
  `user2` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user1` (`user1`),
  CONSTRAINT `relationship_ibfk_1` FOREIGN KEY (`user1`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of relationship
-- ----------------------------
INSERT INTO `relationship` VALUES ('1', '4', '3');
INSERT INTO `relationship` VALUES ('2', '5', '3');
INSERT INTO `relationship` VALUES ('3', '15', '3');
INSERT INTO `relationship` VALUES ('4', '3', '4');
INSERT INTO `relationship` VALUES ('5', '4', '5');
INSERT INTO `relationship` VALUES ('6', '3', '5');
INSERT INTO `relationship` VALUES ('7', '16', '3');
INSERT INTO `relationship` VALUES ('8', '8', '4');
INSERT INTO `relationship` VALUES ('9', '17', '3');
INSERT INTO `relationship` VALUES ('10', '5', '4');
INSERT INTO `relationship` VALUES ('11', '4', '8');
INSERT INTO `relationship` VALUES ('15', '3', '15');
INSERT INTO `relationship` VALUES ('16', '3', '16');
INSERT INTO `relationship` VALUES ('18', '3', '17');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `info` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('3', 'abc', 'abc', 'hellow');
INSERT INTO `user` VALUES ('4', 'ddd', 'ab', 'yunnanbaiyaoaa');
INSERT INTO `user` VALUES ('5', 'qcc', 'qqq', 'qq');
INSERT INTO `user` VALUES ('6', 'qq', 'qq', 'jiajieshi');
INSERT INTO `user` VALUES ('8', 'kai', 'abc', 'dd');
INSERT INTO `user` VALUES ('15', 'bc', 'bc', null);
INSERT INTO `user` VALUES ('16', 'wbc', 'www', 'aerbeisi');
INSERT INTO `user` VALUES ('17', 'baaa', 'af', 'aaa');
INSERT INTO `user` VALUES ('19', 'a', 'a', 'shufujia');
INSERT INTO `user` VALUES ('20', 'b', 'b', 'bbb');
