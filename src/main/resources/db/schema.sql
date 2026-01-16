-- 创建数据库
CREATE DATABASE IF NOT EXISTS bigbird_blog DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bigbird_blog;

-- 创建文章表
CREATE TABLE `article` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(200) NOT NULL COMMENT '文章标题',
  `summary` varchar(500) DEFAULT NULL COMMENT '文章摘要',
  `content` text NOT NULL COMMENT '文章内容',
  `author` varchar(50) DEFAULT NULL COMMENT '作者',
  `tags` varchar(200) DEFAULT NULL COMMENT '标签（逗号分隔）',
  `views` int DEFAULT '0' COMMENT '浏览量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0-未删除，1-已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章表';

-- 插入示例数据
INSERT INTO `article` (`title`, `summary`, `content`, `author`, `tags`, `views`, `create_time`, `update_time`, `deleted`) 
VALUES 
('欢迎来到 BigBird 博客', 
 '这是博客的第一篇文章，介绍了博客的基本功能和使用方法。', 
 '<h2>欢迎</h2><p>欢迎来到 BigBird 博客！这是一个基于 Vue 3 和 Spring Boot 的个人博客系统。</p>', 
 'BigBird', 
 '博客,介绍', 
 128, 
 NOW(), 
 NOW(), 
 0),
 
('Vue 3 开发实践', 
 '分享 Vue 3 的开发经验和最佳实践，包括组合式 API、响应式系统等。', 
 '<h2>引言</h2><p>Vue 3 带来了许多令人兴奋的新特性...</p>', 
 'BigBird', 
 'Vue,前端', 
 256, 
 NOW(), 
 NOW(), 
 0),
 
('Spring Boot 微服务架构', 
 '探讨 Spring Boot 在微服务架构中的应用，包括服务注册、配置中心等。', 
 '<h2>微服务架构</h2><p>Spring Boot 是构建微服务的优秀框架...</p>', 
 'BigBird', 
 'Spring Boot,后端,微服务', 
 189, 
 NOW(), 
 NOW(), 
 0);
