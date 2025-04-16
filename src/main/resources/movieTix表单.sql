-- DBName： movie_tix
use movie_tix;

-- 用户基础信息表
CREATE TABLE users (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password_hash` varchar(255) NOT NULL COMMENT '加密后的密码',
  `is_admin` tinyint(1) DEFAULT 0 COMMENT '管理员标识：0-普通用户, 1-管理员',
  `is_blocked` tinyint(1) DEFAULT 0 COMMENT '拉黑标识：0-正常, 1-已拉黑',
  `balance` decimal(10,2) DEFAULT 0.00 COMMENT '用户余额',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`)
) COMMENT='用户基础信息表';

-- 用户额外信息表
CREATE TABLE user_profiles (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL COMMENT '关联users表的id',
  `nickname` varchar(50) DEFAULT '神秘来客' COMMENT '用户昵称',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `id_card` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `gender` tinyint(1) DEFAULT NULL COMMENT '性别:0-未知 1-男 2-女',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `signature` varchar(255) DEFAULT '话语不能透过纯白之门' COMMENT '个人签名',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_id` (`user_id`),
  UNIQUE KEY `idx_phone` (`phone`),
  UNIQUE KEY `idx_id_card` (`id_card`),
  CONSTRAINT `fk_profile_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) COMMENT='用户额外信息表';

-- 电影基础信息表
CREATE TABLE movies (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL COMMENT '电影名称',
  `poster_url` varchar(255) COMMENT '海报URL',
  `release_date` date COMMENT '上映日期',
  `duration` int unsigned COMMENT '时长(分钟)',
  `genre` varchar(50) COMMENT '类型',
  `rating` decimal(3,1) COMMENT '评分',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态:0-下架 1-上映',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_title` (`title`),
  KEY `idx_genre` (`genre`),
  KEY `idx_status` (`status`)
) COMMENT='电影基础信息表';

-- 电影详细信息表
CREATE TABLE movies_detail (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `movie_id` int unsigned NOT NULL COMMENT '电影ID',
  `description` text COMMENT '详细描述',
  `director` varchar(50) COMMENT '导演',
  `actors` varchar(255) COMMENT '主演列表',
  `language` varchar(50) COMMENT '语言',
  `country` varchar(50) COMMENT '国家/地区',
  `trailer_url` varchar(255) COMMENT '预告片URL',
  `awards` text COMMENT '获奖情况',
  `producer` varchar(100) COMMENT '制片人',
  `screenwriter` varchar(100) COMMENT '编剧',
  `film_location` varchar(255) COMMENT '拍摄地',
  `budget` decimal(15,2) COMMENT '预算',
  `box_office` decimal(15,2) COMMENT '票房',
  `tags` varchar(255) COMMENT '标签',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_movie_id` (`movie_id`),
  CONSTRAINT `fk_detail_movie` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`) ON DELETE CASCADE
) COMMENT='电影详细信息表';

-- 放映厅表
CREATE TABLE halls (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '厅名',
  `capacity` int unsigned NOT NULL COMMENT '座位容量',
  `rows` int unsigned NOT NULL COMMENT '行数',
  `cols` int unsigned NOT NULL COMMENT '列数',
  `facilities` varchar(255) COMMENT '设施(JSON)',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态:0-关停 1-启用',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) COMMENT='放映厅表';

-- 放映场次表
CREATE TABLE screenings (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `movie_id` int unsigned NOT NULL COMMENT '电影ID',
  `hall_id` int unsigned NOT NULL COMMENT '放映厅ID',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `price` decimal(10,2) NOT NULL COMMENT '票价',
  `remaining_seats` int unsigned NOT NULL COMMENT '剩余座位数',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态:0-已取消 1-正常',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_movie_id` (`movie_id`),
  KEY `idx_hall_id` (`hall_id`),
  KEY `idx_time` (`start_time`),
  CONSTRAINT `fk_screening_movie` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_screening_hall` FOREIGN KEY (`hall_id`) REFERENCES `halls` (`id`) ON DELETE CASCADE
) COMMENT='放映场次表';

-- 订单信息表
CREATE TABLE orders (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `order_no` varchar(32) COMMENT '订单编号',
  `user_id` int unsigned NOT NULL COMMENT '用户ID',
  `screening_id` int unsigned NOT NULL COMMENT '场次ID',
  `total_amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `seat_count` int unsigned NOT NULL COMMENT '座位数',
  `status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '状态:0-待支付 1-已支付 2-已取消 3-已完成 4-已退款',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_screening_id` (`screening_id`),
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_order_screening` FOREIGN KEY (`screening_id`) REFERENCES `screenings` (`id`) ON DELETE CASCADE
) COMMENT='订单表';

-- 订单座位表
CREATE TABLE order_seats (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` int unsigned NOT NULL COMMENT '订单ID',
  `seat_row` int unsigned NOT NULL COMMENT '座位行',
  `seat_col` int unsigned NOT NULL COMMENT '座位列',
  `seat_no` varchar(10) NOT NULL COMMENT '座位号(如A01)',
  `price` decimal(10,2) NOT NULL COMMENT '座位价格',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_screening_seat` (`order_id`, `seat_row`, `seat_col`),
  CONSTRAINT `fk_order_seat` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE
) COMMENT='订单座位表';

-- 支付记录表
CREATE TABLE payments (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int unsigned NOT NULL COMMENT '订单ID',
  `amount` decimal(10,2) NOT NULL COMMENT '支付金额',
  `payment_method` varchar(20) COMMENT '支付方式',
  `transaction_id` varchar(64) COMMENT '交易号',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态:0-未支付 1-支付成功 2-支付失败',
  `pay_time` datetime COMMENT '支付时间',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  CONSTRAINT `fk_payment_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE
) COMMENT='支付记录表';

-- 退票申请表
CREATE TABLE refunds (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int unsigned NOT NULL COMMENT '订单ID',
  `user_id` int unsigned NOT NULL COMMENT '用户ID',
  `reason` varchar(255) COMMENT '退票原因',
  `status` tinyint(1) DEFAULT 0 COMMENT '状态:0-待处理 1-已同意 2-已拒绝',
  `admin_id` int unsigned COMMENT '处理管理员ID',
  `processed_at` datetime COMMENT '处理时间',
  `refund_amount` decimal(10,2) COMMENT '退款金额',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_refund_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_refund_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) COMMENT='退票申请表';

-- 观影提醒表
CREATE TABLE reminders (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '提醒ID',
  `user_id` int unsigned NOT NULL COMMENT '用户ID',
  `screening_id` int unsigned NOT NULL COMMENT '场次ID',
  `notify_time` datetime NOT NULL COMMENT '应提醒时间',
  `is_notified` tinyint(1) DEFAULT 0 COMMENT '是否已提醒:0-否 1-是',
  `notified_at` datetime COMMENT '实际提醒时间',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_screening` (`user_id`, `screening_id`),
  CONSTRAINT `fk_reminder_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_reminder_screening` FOREIGN KEY (`screening_id`) REFERENCES `screenings` (`id`) ON DELETE CASCADE
) COMMENT='观影提醒表';

-- 电影评论表(待用)
CREATE TABLE comments (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `movie_id` int unsigned NOT NULL COMMENT '电影ID',
  `user_id` int unsigned NOT NULL COMMENT '用户ID',
  `content` text NOT NULL COMMENT '评论内容',
  `rating` tinyint(1) COMMENT '评分(1-5)',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态:0-隐藏 1-显示',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_movie_id` (`movie_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_comment_movie` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) COMMENT='电影评论表';

-- 系统日志表
CREATE TABLE logs (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int unsigned COMMENT '用户ID',
  `action` varchar(50) NOT NULL COMMENT '操作类型',
  `ip_address` varchar(50) COMMENT 'IP地址',
  `user_agent` varchar(255) COMMENT '用户代理',
  `details` text COMMENT '详细信息',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_action` (`action`),
  CONSTRAINT `fk_log_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) COMMENT='系统日志表';
