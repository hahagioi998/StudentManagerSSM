/*
Navicat MySQL Data Transfer

Source Server         : dmbjz
Source Server Version : 50561
Source Host           : localhost:3306
Source Database       : studentssm

Target Server Type    : MYSQL
Target Server Version : 50561
File Encoding         : 65001

Date: 2020-04-08 11:50:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `c3p0testtable`
-- ----------------------------
DROP TABLE IF EXISTS `c3p0testtable`;
CREATE TABLE `c3p0testtable` (
  `a` char(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of c3p0testtable
-- ----------------------------

-- ----------------------------
-- Table structure for `clazz`
-- ----------------------------
DROP TABLE IF EXISTS `clazz`;
CREATE TABLE `clazz` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gradeId` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  `remark` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `gradeId` (`gradeId`),
  CONSTRAINT `clazz_ibfk_1` FOREIGN KEY (`gradeId`) REFERENCES `grade` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of clazz
-- ----------------------------
INSERT INTO `clazz` VALUES ('12', '13', '网络工程1801', '网络工程专业2018届1班');
INSERT INTO `clazz` VALUES ('13', '1', '软件工程1101', '软件工程1101班');
INSERT INTO `clazz` VALUES ('14', '22', '播音1班', '播音系专业2018届');
INSERT INTO `clazz` VALUES ('21', '1', '软件开发1班', '2020年软件开发学生所属班级');
INSERT INTO `clazz` VALUES ('22', '18', '软件开发3班', '软件开发2017届3班学生');
INSERT INTO `clazz` VALUES ('25', '20', '表演系1班', '表演系2019届1班');
INSERT INTO `clazz` VALUES ('26', '20', '表演3班', '表演专业2018届3班');
INSERT INTO `clazz` VALUES ('28', '25', '考古3班', '考古专业2020届3班');
INSERT INTO `clazz` VALUES ('29', '24', '摄影1班', '摄影专业2018届');
INSERT INTO `clazz` VALUES ('30', '24', '摄影3班', '摄影专业2019届');
INSERT INTO `clazz` VALUES ('31', '19', '外语3班', '外语专业2020届');
INSERT INTO `clazz` VALUES ('36', '24', '摄影4班', '摄影专业2020届');

-- ----------------------------
-- Table structure for `grade`
-- ----------------------------
DROP TABLE IF EXISTS `grade`;
CREATE TABLE `grade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `remark` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of grade
-- ----------------------------
INSERT INTO `grade` VALUES ('1', '软件工程2020届', '计算机学院软件工程专业2020届');
INSERT INTO `grade` VALUES ('12', '计算机2019届', '计算机学院计算机专业2019届');
INSERT INTO `grade` VALUES ('13', '网络工程2018届', '计算机学院网络工程专业2018届');
INSERT INTO `grade` VALUES ('14', '网络工程2010届', '计算机学院网络工程专业2010届');
INSERT INTO `grade` VALUES ('18', '软件开发2017届', '计算机学院软件开发专业2017届');
INSERT INTO `grade` VALUES ('19', '外语专业', '外语专业2011届');
INSERT INTO `grade` VALUES ('20', '表演专业', '表演专业2009届学生');
INSERT INTO `grade` VALUES ('21', '挖掘机专业', '挖掘机专业2000届学生');
INSERT INTO `grade` VALUES ('22', '播音系专业', '播音系专业2012届学生');
INSERT INTO `grade` VALUES ('24', '摄影专业', '摄影专业2016届学生');
INSERT INTO `grade` VALUES ('25', '考古专业', '考古专业2019届学生');

-- ----------------------------
-- Table structure for `student`
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `clazzId` int(11) NOT NULL,
  `sn` varchar(32) NOT NULL,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `sex` varchar(8) NOT NULL,
  `photo` varchar(128) NOT NULL,
  `remark` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `gradeId` (`clazzId`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`clazzId`) REFERENCES `clazz` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('29', '13', 'S1586271568896', 'dmbjz', '112233', '男', '/photo/76cc323f-59f5-4602-be36-8ec217d798ca.jpeg', '辅导员助理');
INSERT INTO `student` VALUES ('30', '26', 'S1586271836918', '小丽', '11233', '女', '/photo/7266049d-5899-4ac5-b005-b714fd62ab6d.jpg', '喜欢唱歌、舞蹈');
INSERT INTO `student` VALUES ('31', '14', 'S1586271913387', '小圆', '23341', '女', '/photo/df5ef1b3-8ff8-4333-ad2e-bae6ed911f90.jpg', '学霸一枚');
INSERT INTO `student` VALUES ('32', '30', 'S1586271973438', '小爱', 'aksdj133', '女', '/photo/171b9058-d76c-40f4-b0a9-08dc0f53e68c.jpg', '班委');
INSERT INTO `student` VALUES ('33', '22', 'S1586271998093', '小军', 'asdl214', '男', '/photo/94fa584e-8984-4c5c-8f7e-38c16f391ad0.jpg', '喜欢看电影');
INSERT INTO `student` VALUES ('34', '28', 'S1586272087255', '小赵', 'akjsdh123', '男', '/photo/016cc148-0cbb-433b-a8b5-a8a1504954e6.jpg', '乐于助人，体育委员');
INSERT INTO `student` VALUES ('35', '13', 'S1586272290919', '小李', 'kalsdlfh', '男', '/photo/efb5cc4c-c590-416b-81f9-9f3e1dcc1636.jpeg', '');
INSERT INTO `student` VALUES ('36', '25', 'S1586272309599', '小天', '123ada1', '男', '/photo/57522c4b-d7e5-453c-bf22-2aa5923aa6bf.jpeg', '');
INSERT INTO `student` VALUES ('37', '31', 'S1586272324280', '古田', 'asd123asd', '男', '/photo/df7a6e38-02af-4cc9-91ac-e8ae1796daed.jpeg', '');
INSERT INTO `student` VALUES ('38', '21', 'S1586272350138', '李杨军', 'adk1j2sd', '男', '/photo/ef4a5e66-3ac6-41bb-98c6-5a34455232bd.jpeg', '');
INSERT INTO `student` VALUES ('39', '21', 'S1586272379777', '小卢', 'asd1123', '男', '/photo/5f669554-d361-4ca0-b334-89075aff635d.jpeg', '');
INSERT INTO `student` VALUES ('49', '14', 'S1586317369236', '土豆', '112233', '男', '/photo/98635918-9012-4033-9c40-ce0131d03f7a.jpg', '');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', 'admin');
INSERT INTO `user` VALUES ('17', 'honor', 'honor');
INSERT INTO `user` VALUES ('20', '小刘', '123123123');
INSERT INTO `user` VALUES ('21', '小东', '123123123');
INSERT INTO `user` VALUES ('22', '小灶', '11223344');
INSERT INTO `user` VALUES ('23', '腾讯', '11211212');
