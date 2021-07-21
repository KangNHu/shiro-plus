SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sp_config_extend
-- ----------------------------
DROP TABLE IF EXISTS `sp_config_extend`;
CREATE TABLE `sp_config_extend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `config_id` bigint(20) NOT NULL COMMENT '来自*_config 表主键',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '扩展属性名称',
  `value` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '扩展属性值',
  `type` tinyint(4) NOT NULL COMMENT '配置表类型 ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='配置扩展表';

-- ----------------------------
-- Table structure for sp_event
-- ----------------------------
DROP TABLE IF EXISTS `sp_event`;
CREATE TABLE `sp_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '事件内容 json',
  `time` bigint(20) NOT NULL COMMENT '有效期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='事件表 ';

-- ----------------------------
-- Table structure for sp_event_processor_record
-- ----------------------------
DROP TABLE IF EXISTS `sp_event_processor_record`;
CREATE TABLE `sp_event_processor_record` (
  `id` bigint(20) NOT NULL,
  `host` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消费者ip',
  `event_id` bigint(20) NOT NULL COMMENT '事件id',
  PRIMARY KEY (`id`),
  KEY `host` (`host`(15))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='事件处理记录';

-- ----------------------------
-- Table structure for sp_global_config
-- ----------------------------
DROP TABLE IF EXISTS `sp_global_config`;
CREATE TABLE `sp_global_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '租户id',
  `anons` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '放行白名单',
  `enable_authentication` tinyint(4) NOT NULL COMMENT '是否开启鉴权 0 关闭 1 开启',
  `enable_authorization` tinyint(4) NOT NULL COMMENT '是否开启授权 0 关闭 1开启',
  `create_tm` bigint(20) NOT NULL COMMENT '创建时间',
  `status` tinyint(4) NOT NULL COMMENT '状态 0 删除 1 正常',
  `last_update_tm` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_by` bigint(20) NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tenant_id` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='全局配置';

-- ----------------------------
-- Table structure for sp_logs
-- ----------------------------
DROP TABLE IF EXISTS `sp_logs`;
CREATE TABLE `sp_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `old_data` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '旧数据',
  `new_data` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '新数据',
  `context` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '日志内容',
  `operation_type` tinyint(4) DEFAULT NULL COMMENT '1 删除 2 新增 3 更新',
  `create_tm` bigint(20) NOT NULL COMMENT '创建时间',
  `operation_id` bigint(20) NOT NULL COMMENT '操作人',
  `business_code` tinyint(4) NOT NULL COMMENT '业务编码',
  `business_id` bigint(20) DEFAULT NULL COMMENT '业务主键',
  `extend` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '扩展信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- ----------------------------
-- Table structure for sp_role
-- ----------------------------
DROP TABLE IF EXISTS `sp_role`;
CREATE TABLE `sp_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色编码',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for sp_system
-- ----------------------------
DROP TABLE IF EXISTS `sp_system`;
CREATE TABLE `sp_system` (
  `version` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '版本',
  `event_time` bigint(20) NOT NULL COMMENT '事件有效期',
  `key_pair` varchar(2000) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录token的签名密钥',
  `login_valid_time` int(10) NOT NULL COMMENT '登录有效期 单位分',
  `client_token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户端访问token',
  `update_by` bigint(20) DEFAULT NULL,
  `last_update_tm` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `init_password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '初始密码',
  PRIMARY KEY (`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统表 ，系统相关属性和版本信息';

-- ----------------------------
-- Table structure for sp_user
-- ----------------------------
DROP TABLE IF EXISTS `sp_user`;
CREATE TABLE `sp_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名称',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称',
  `create_tm` bigint(20) NOT NULL COMMENT '创建时间',
  `status` tinyint(4) NOT NULL COMMENT '状态 0 删除 1 正常 2 禁用',
  `last_update_tm` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_by` bigint(20) NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ----------------------------
-- Table structure for sp_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sp_user_role`;
CREATE TABLE `sp_user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色表';

SET FOREIGN_KEY_CHECKS = 1;
