/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : loveeat

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2020-07-15 19:38:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for le_business
-- ----------------------------
DROP TABLE IF EXISTS `le_business`;
CREATE TABLE `le_business` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `businessname` varchar(255) DEFAULT NULL COMMENT '商家全称',
  `businessnickname` varchar(255) DEFAULT NULL COMMENT '商家简称',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of le_business
-- ----------------------------
INSERT INTO `le_business` VALUES ('1', '中国肯德基有限公司', '肯德基', '2020-05-16 20:20:38', '2020-05-16 20:20:43');
INSERT INTO `le_business` VALUES ('2', '北京吉野家食品有限公司', '吉野家', '2020-05-17 06:28:18', '2020-05-17 06:28:18');
INSERT INTO `le_business` VALUES ('3', '北京吉野家食品有限公司', '吉野家', '2020-06-11 05:46:32', '2020-06-11 05:46:32');
INSERT INTO `le_business` VALUES ('4', '北京吉野家食品有限公司', '吉野家', '2020-06-16 03:37:24', '2020-06-16 03:37:24');
INSERT INTO `le_business` VALUES ('5', '北京吉野家食品有限公司', '吉野家', '2020-06-17 05:09:01', '2020-06-17 05:09:01');
INSERT INTO `le_business` VALUES ('6', '北京吉野家食品有限公司', '吉野家', '2020-06-17 05:24:06', '2020-06-17 05:24:06');
INSERT INTO `le_business` VALUES ('7', '北京吉野家食品有限公司', '吉野家', '2020-06-17 05:33:41', '2020-06-17 05:33:41');
INSERT INTO `le_business` VALUES ('8', '北京吉野家食品有限公司', '吉野家', '2020-06-23 05:18:38', '2020-06-23 05:18:38');

-- ----------------------------
-- Table structure for le_business_detail
-- ----------------------------
DROP TABLE IF EXISTS `le_business_detail`;
CREATE TABLE `le_business_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bid` int(11) NOT NULL COMMENT '商家id',
  `isbranch` int(11) DEFAULT NULL COMMENT '是否分店 1是总店，2是分店',
  `branchname` varchar(255) NOT NULL COMMENT '分店名称',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `modifitime` datetime DEFAULT NULL COMMENT '修改时间',
  `location` varchar(255) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `describetion` varchar(255) DEFAULT NULL COMMENT '商家描述',
  `status` varchar(255) DEFAULT NULL COMMENT '审核状态 1审核中 2审核通过 3审核不通过',
  `state` int(2) DEFAULT NULL COMMENT '状态 1:启用 2:禁用',
  `url` varchar(255) DEFAULT '' COMMENT '商家分店url',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of le_business_detail
-- ----------------------------
INSERT INTO `le_business_detail` VALUES ('1', '1', '1', '肯德基（云岗店）', '2020-05-16 20:23:16', '2020-05-16 20:23:20', '北京市丰台区云岗街道1号院', '12345678911', '我是肯德基公司', '审核中', '2', '');
INSERT INTO `le_business_detail` VALUES ('2', '1', '1', '肯德基（陶然亭店）', '2020-05-16 20:23:16', '2020-05-16 20:23:20', '北京市西城区陶然亭街道', '12345678911', '我是肯德基公司', '审核通过', '1', null);

-- ----------------------------
-- Table structure for le_foodtype
-- ----------------------------
DROP TABLE IF EXISTS `le_foodtype`;
CREATE TABLE `le_foodtype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `foodtype` varchar(255) DEFAULT NULL COMMENT '美食类型',
  `describ` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of le_foodtype
-- ----------------------------
INSERT INTO `le_foodtype` VALUES ('1', '1', '单人餐');
INSERT INTO `le_foodtype` VALUES ('2', '2', '双人餐');
INSERT INTO `le_foodtype` VALUES ('3', '3', '3-4人餐');
INSERT INTO `le_foodtype` VALUES ('4', '4', '5-6人餐');
INSERT INTO `le_foodtype` VALUES ('5', '5', '超值套餐');
INSERT INTO `le_foodtype` VALUES ('6', '6', '火锅');
INSERT INTO `le_foodtype` VALUES ('7', '7', '串串香');
INSERT INTO `le_foodtype` VALUES ('8', '8', '烧烤');
INSERT INTO `le_foodtype` VALUES ('9', '9', '饮料');
INSERT INTO `le_foodtype` VALUES ('10', '10', '甜品');

-- ----------------------------
-- Table structure for le_permission
-- ----------------------------
DROP TABLE IF EXISTS `le_permission`;
CREATE TABLE `le_permission` (
  `id` int(11) NOT NULL COMMENT '权限id',
  `method` varchar(10) DEFAULT NULL COMMENT '方法类型',
  `zuul_prefix` varchar(30) DEFAULT NULL COMMENT '网关前缀',
  `service_prefix` varchar(30) DEFAULT NULL COMMENT '服务前缀',
  `uri` varchar(100) DEFAULT NULL COMMENT '请求路径',
  `createTime` datetime DEFAULT NULL COMMENT '创建日期',
  `updateTime` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of le_permission
-- ----------------------------
INSERT INTO `le_permission` VALUES ('1', 'GET', '/api', '/auth', 'exit', '2018-12-14 09:45:35', '2018-12-14 09:45:37');
INSERT INTO `le_permission` VALUES ('2', 'GET', '/api', '/auth', 'member', '2018-12-17 13:23:25', '2018-12-17 13:23:27');
INSERT INTO `le_permission` VALUES ('3', 'GET', '/api', '/member', 'hello', '2018-12-17 13:23:25', '2018-12-17 13:23:25');
INSERT INTO `le_permission` VALUES ('4', 'GET', '/api', '/member', 'current', '2018-12-17 13:23:25', '2018-12-17 13:23:25');
INSERT INTO `le_permission` VALUES ('5', 'GET', '/api', '/member', 'query', '2018-12-17 13:23:25', '2018-12-17 13:23:25');

-- ----------------------------
-- Table structure for le_product
-- ----------------------------
DROP TABLE IF EXISTS `le_product`;
CREATE TABLE `le_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `businessname` varchar(255) DEFAULT NULL COMMENT '商家分店名称',
  `productname` varchar(255) DEFAULT NULL,
  `bid` int(11) DEFAULT NULL COMMENT '商家分店id',
  `createtime` datetime DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `state` int(255) DEFAULT NULL COMMENT '状态 ',
  `status` varchar(255) DEFAULT NULL COMMENT '状态  1审核中 2审核通过 3审核不通过',
  `foodtype` int(2) DEFAULT NULL,
  `describ` varchar(255) DEFAULT NULL COMMENT '商品描述',
  `originalprice` decimal(10,2) DEFAULT '0.00' COMMENT '原价',
  `bargainprice` decimal(10,2) DEFAULT '0.00' COMMENT '砍价之后的价格',
  `bargainpersonsum` int(11) DEFAULT NULL,
  `issale` int(2) DEFAULT NULL COMMENT '是否已售 1已抢购 2未抢购',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of le_product
-- ----------------------------
INSERT INTO `le_product` VALUES ('1', '肯德基（陶然亭店）', null, '2', '2020-05-16 20:58:06', '2020-05-16 20:58:09', '1', '审核通过', '1', '麦辣鸡腿堡套餐', '31.00', '30.00', '1', '2', '0');
INSERT INTO `le_product` VALUES ('2', null, '肯德基超值套餐', '2', '2020-05-16 20:58:06', '2020-06-23 07:15:41', '2', '审核中', null, null, null, '30.00', '1', '2', '0');
INSERT INTO `le_product` VALUES ('3', null, '肯德基超值套餐', '2', null, null, null, null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for le_product_menudetail
-- ----------------------------
DROP TABLE IF EXISTS `le_product_menudetail`;
CREATE TABLE `le_product_menudetail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) DEFAULT NULL,
  `parentid` int(11) DEFAULT NULL,
  `item` varchar(255) DEFAULT NULL COMMENT '菜品',
  `price` decimal(10,2) DEFAULT '0.00' COMMENT '菜品价格',
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of le_product_menudetail
-- ----------------------------
INSERT INTO `le_product_menudetail` VALUES ('1', '1', '0', null, null, '套餐内容1');
INSERT INTO `le_product_menudetail` VALUES ('2', '1', '1', '麦拉堡', '18.00', null);
INSERT INTO `le_product_menudetail` VALUES ('3', '1', '1', '薯条', '8.00', null);
INSERT INTO `le_product_menudetail` VALUES ('4', '1', '1', '可乐', '6.00', null);
INSERT INTO `le_product_menudetail` VALUES ('5', '1', '0', null, null, '套餐内容2三选0');
INSERT INTO `le_product_menudetail` VALUES ('6', '1', '5', '麦拉堡', '18.00', null);
INSERT INTO `le_product_menudetail` VALUES ('7', '1', '5', '薯条', '8.00', null);
INSERT INTO `le_product_menudetail` VALUES ('8', '1', '5', '可乐', '6.00', null);
INSERT INTO `le_product_menudetail` VALUES ('9', '1', '0', null, null, '我改名字了-菜单标题');
INSERT INTO `le_product_menudetail` VALUES ('10', '1', '9', '我是可乐可乐', '10.00', null);

-- ----------------------------
-- Table structure for le_product_picurl
-- ----------------------------
DROP TABLE IF EXISTS `le_product_picurl`;
CREATE TABLE `le_product_picurl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL,
  `picurl` varchar(255) DEFAULT NULL COMMENT '图片地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of le_product_picurl
-- ----------------------------
INSERT INTO `le_product_picurl` VALUES ('11', '1', 'http://149.248.8.106:9000/loveeat-dev-pro/pid_1_1594013170481_3.jpg');
INSERT INTO `le_product_picurl` VALUES ('12', '1', 'http://149.248.8.106:9000/loveeat-dev-pro/pid_1_1594013999211_1.jpg');
INSERT INTO `le_product_picurl` VALUES ('13', '1', 'http://149.248.8.106:9000/loveeat-dev-pro/pid_1_1594014039495_1.jpg');

-- ----------------------------
-- Table structure for le_role
-- ----------------------------
DROP TABLE IF EXISTS `le_role`;
CREATE TABLE `le_role` (
  `id` int(11) NOT NULL COMMENT '角色id',
  `role_name` varchar(50) DEFAULT NULL COMMENT '角色名称',
  `valid` tinyint(1) DEFAULT NULL COMMENT '是否有效 1是 0否',
  `createTime` datetime DEFAULT NULL COMMENT '创建日期',
  `updateTime` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of le_role
-- ----------------------------
INSERT INTO `le_role` VALUES ('1', 'ROLE_ADMIN', '1', '2018-12-14 09:46:01', '2018-12-14 09:46:03');
INSERT INTO `le_role` VALUES ('2', 'ROLE_USER', '1', '2018-12-14 09:46:16', '2018-12-14 09:46:18');

-- ----------------------------
-- Table structure for le_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `le_role_permission`;
CREATE TABLE `le_role_permission` (
  `id` int(11) NOT NULL COMMENT '主键',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `permission_id` int(11) DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of le_role_permission
-- ----------------------------
INSERT INTO `le_role_permission` VALUES ('1', '1', '1');
INSERT INTO `le_role_permission` VALUES ('2', '1', '2');
INSERT INTO `le_role_permission` VALUES ('5', '1', '5');
INSERT INTO `le_role_permission` VALUES ('6', '2', '1');
INSERT INTO `le_role_permission` VALUES ('7', '2', '3');

-- ----------------------------
-- Table structure for le_user_bak
-- ----------------------------
DROP TABLE IF EXISTS `le_user_bak`;
CREATE TABLE `le_user_bak` (
  `id` int(32) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(96) NOT NULL,
  `userpic` varchar(255) DEFAULT NULL COMMENT '头像',
  `status` varchar(32) NOT NULL COMMENT '用户状态',
  `create_time` datetime NOT NULL,
  `last_logintime` datetime DEFAULT NULL,
  `login_type` int(2) DEFAULT NULL COMMENT '登录类型，0：网页，1微信',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of le_user_bak
-- ----------------------------
INSERT INTO `le_user_bak` VALUES ('46', 'super', '$2a$10$TJ4TmCdK.X4wv/tCqHW14.w70U3CC33CeVncD3SLmyMXMknstqKRe', null, '1', '2018-03-07 16:27:47', null, null);
INSERT INTO `le_user_bak` VALUES ('48', 'admin', '$2a$10$TJ4TmCdK.X4wv/tCqHW14.w70U3CC33CeVncD3SLmyMXMknstqKRe', null, '1', '2018-03-07 16:27:47', null, null);
INSERT INTO `le_user_bak` VALUES ('49', 'itcast', '$2a$10$TJ4TmCdK.X4wv/tCqHW14.w70U3CC33CeVncD3SLmyMXMknstqKRe', null, '1', '2018-03-07 16:27:47', null, null);
INSERT INTO `le_user_bak` VALUES ('50', 'stu1', '$2a$10$pLtt2KDAFpwTWLjNsmTEi.oU1yOZyIn9XkziK/y/spH5rftCpUMZa', null, '1', '2018-03-07 16:27:47', null, null);
INSERT INTO `le_user_bak` VALUES ('51', 'stu2', '$2a$10$nxPKkYSez7uz2YQYUnwhR.z57km3yqKn3Hr/p1FR6ZKgc18u.Tvqm', null, '1', '2018-03-07 16:27:47', null, null);
INSERT INTO `le_user_bak` VALUES ('52', 't1', '$2a$10$TJ4TmCdK.X4wv/tCqHW14.w70U3CC33CeVncD3SLmyMXMknstqKRe', null, '', '2018-03-07 16:27:47', null, null);

-- ----------------------------
-- Table structure for le_user_basic
-- ----------------------------
DROP TABLE IF EXISTS `le_user_basic`;
CREATE TABLE `le_user_basic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `userpic` varchar(255) DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of le_user_basic
-- ----------------------------
INSERT INTO `le_user_basic` VALUES ('1', 'aaa', null, null, '2020-07-10 17:53:55', 'zhangsan', '123', '13691386065', '2020-07-10 17:54:03', null);

-- ----------------------------
-- Table structure for le_user_role
-- ----------------------------
DROP TABLE IF EXISTS `le_user_role`;
CREATE TABLE `le_user_role` (
  `id` int(11) NOT NULL COMMENT '主键',
  `user_id` int(11) DEFAULT NULL COMMENT '会员id',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of le_user_role
-- ----------------------------
INSERT INTO `le_user_role` VALUES ('1', '1', '1');
INSERT INTO `le_user_role` VALUES ('2', '1', '2');

-- ----------------------------
-- Table structure for oauth_access_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(128) NOT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  `authentication` blob,
  `refresh_token` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_access_token
-- ----------------------------

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(128) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('LeWebapp', null, '$2a$10$zGDI.9wXmwz/2Z/t0A.mpuXMHC143pbPsfCy9KxZhSWQbtAmyixNW', 'app', 'password,authorization_code', 'http://localhost', null, '60', '60', null, null);

-- ----------------------------
-- Table structure for oauth_client_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(128) NOT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_client_token
-- ----------------------------

-- ----------------------------
-- Table structure for oauth_code
-- ----------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code` (
  `code` varchar(256) DEFAULT NULL,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_code
-- ----------------------------

-- ----------------------------
-- Table structure for oauth_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_refresh_token
-- ----------------------------
