-- ==========================================================
-- AI 能力平台核心数据库建表脚本 (MySQL 5.7 兼容)
-- 注意：所有表均取消了 AUTO_INCREMENT，主键统一声明为 BIGINT
-- 实体类请务必配置为 @TableId(type = IdType.ASSIGN_ID)
-- 以完美兼容达梦数据库和其他异构数据库
-- ==========================================================

-- 1. 业务系统接入表：鉴权验证
CREATE TABLE `ai_app_info` (
  `id` bigint(20) NOT NULL COMMENT '主键ID(雪花算法)',
  `app_code` varchar(64) NOT NULL COMMENT '业务系统唯一编码(如: dccs-oms, erp)',
  `app_name` varchar(128) NOT NULL COMMENT '业务系统名称',
  `app_secret` varchar(128) NOT NULL COMMENT '调用密钥',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态: 1-正常 0-停用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_app_code` (`app_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='外部接入业务系统信息表';

-- 2. 大模型配置池：解耦大模型账号，可多配千问、百川、OpenAI
CREATE TABLE `ai_model_config` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `provider` varchar(64) NOT NULL COMMENT '模型服务商(如: Qwen, OpenAI, Zhipu)',
  `model_name` varchar(64) NOT NULL COMMENT '具体模型版本(如: qwen-max, gpt-4o)',
  `base_url` varchar(255) NOT NULL COMMENT '代理地址/内网部署地址',
  `api_key` varchar(255) NOT NULL COMMENT '调用秘钥(建议脱敏)',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态: 1-启用 0-停用',
  `priority` int(11) NOT NULL DEFAULT '0' COMMENT '负载优先级(数字越大越优先)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='大模型底层通道配置表';

-- 3. MCP Server 注册中心：集中管理散落在各微服务中的 MCP 节点
CREATE TABLE `ai_mcp_server` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `server_code` varchar(64) NOT NULL COMMENT '服务唯一标识(如: weather_mcp, db_mcp)',
  `server_name` varchar(128) NOT NULL COMMENT '服务人类可读名称(如: 天气工具服务)',
  `transport_type` varchar(32) NOT NULL DEFAULT 'SSE' COMMENT '传输协议(SSE 或 STDIO)',
  `server_url` varchar(512) NOT NULL COMMENT 'SSE端点连接地址',
  `health_check_url` varchar(512) DEFAULT NULL COMMENT '心跳检查地址(可选)',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '挂载状态: 1-上线 0-下线',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_server_code` (`server_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='MCP 工具提供方注册汇总表';

-- 4. 应用权限分发关联表：(控制哪个业务系统能用哪几个大模型和MCP工具)
CREATE TABLE `ai_app_resource_rel` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `app_id` bigint(20) NOT NULL COMMENT '业务系统ID (关联 ai_app_info)',
  `resource_type` varchar(32) NOT NULL COMMENT '资源类型(MODEL 或 MCP_SERVER)',
  `resource_id` bigint(20) NOT NULL COMMENT '关联的 ai_model_config.id 或 ai_mcp_server.id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_app_res` (`app_id`, `resource_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='外部应用系统关联模型与MCP工具授权表';

-- 5. 全局共享会话表：统一掌管所有业务子系统的用户上下文
CREATE TABLE `ai_chat_session` (
  `id` bigint(20) NOT NULL COMMENT '主键也是真实的 Session ID',
  `app_id` bigint(20) NOT NULL COMMENT '这是哪个业务系统建立的会话',
  `user_id` varchar(64) NOT NULL COMMENT '业务系统传过来的原始用户工号/ID',
  `model_config_id` bigint(20) NOT NULL COMMENT '本次会话指定绑定的大模型资源ID',
  `session_title` varchar(255) DEFAULT NULL COMMENT '由大模型自动抽取的聊天下拉框标题',
  `is_deleted` tinyint(2) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_app_user` (`app_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI 问答统一会话上下文记录表';

-- 6. 会话流水表 (打点用，日后用于 Finetune 训练或审计)
CREATE TABLE `ai_chat_message` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `session_id` bigint(20) NOT NULL COMMENT '所属会话ID',
  `role` varchar(32) NOT NULL COMMENT '角色(user/assistant/tool)',
  `content` longtext NOT NULL COMMENT '完整聊天流水内容(对于大模型回答可能极长)',
  `tool_calls` varchar(2000) DEFAULT NULL COMMENT '调用的MCP工具名及入参记录(JSON)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  PRIMARY KEY (`id`),
  KEY `idx_session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI 问答统一对话流水记录表';
