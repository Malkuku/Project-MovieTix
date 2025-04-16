delete from comments;
delete from movies_detail;
delete from order_seats;
delete from user_profiles;
delete from reminders;
delete from refunds;
delete from payments;
delete from orders;
delete from screenings;
delete from halls;
delete from movies;
delete from users;
delete from logs;

-- 重置自增ID索引
ALTER TABLE comments AUTO_INCREMENT = 1;
ALTER TABLE movies_detail AUTO_INCREMENT = 1;
ALTER TABLE order_seats AUTO_INCREMENT = 1;
ALTER TABLE user_profiles AUTO_INCREMENT = 1;
ALTER TABLE reminders AUTO_INCREMENT = 1;
ALTER TABLE refunds AUTO_INCREMENT = 1;
ALTER TABLE payments AUTO_INCREMENT = 1;
ALTER TABLE orders AUTO_INCREMENT = 1;
ALTER TABLE screenings AUTO_INCREMENT = 1;
ALTER TABLE halls AUTO_INCREMENT = 1;
ALTER TABLE movies AUTO_INCREMENT = 1;
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE logs AUTO_INCREMENT = 1;

-- 电影基础信息表测试数据
INSERT INTO movies (title, poster_url, release_date, duration, genre, rating, status) VALUES
('肖申克的救赎', 'https://example.com/posters/shawshank.jpg', '1994-09-23', 142, '剧情', 9.7, 1),
('盗梦空间2', 'https://example.com/posters/inception.jpg', '2010-07-16', 148, '科幻', 9.3, 1),
('泰坦尼克号', 'https://example.com/posters/titanic.jpg', '1997-12-19', 194, '爱情', 9.1, 1),
('阿甘正传', 'https://example.com/posters/forrestgump.jpg', '1994-07-06', 142, '剧情', 9.5, 1),
('星际穿越', 'https://example.com/posters/interstellar.jpg', '2014-11-12', 169, '科幻', 9.3, 1),
('这个杀手不太冷', 'https://example.com/posters/leon.jpg', '1994-09-14', 110, '动作', 9.4, 1),
('千与千寻', 'https://example.com/posters/spiritedaway.jpg', '2001-07-20', 125, '动画', 9.3, 1),
('疯狂动物城', 'https://example.com/posters/zootopia.jpg', '2016-03-04', 108, '动画', 9.2, 1),
('教父', 'https://example.com/posters/godfather.jpg', '1972-03-24', 175, '犯罪', 9.2, 0),
('霸王别姬', 'https://example.com/posters/farewell.jpg', '1993-01-01', 171, '剧情', 9.6, 1),
('复仇者联盟4：终局之战', 'https://example.com/posters/endgame.jpg', '2019-04-24', 181, '动作', 9.0, 1),
('指环王：王者归来', 'https://example.com/posters/lotr3.jpg', '2003-12-17', 201, '奇幻', 9.0, 1),
('蝙蝠侠：黑暗骑士', 'https://example.com/posters/darkknight.jpg', '2008-07-18', 152, '犯罪', 9.0, 1),
('辛德勒的名单', 'https://example.com/posters/schindler.jpg', '1993-11-30', 195, '历史', 9.0, 1),
('搏击俱乐部', 'https://example.com/posters/fightclub.jpg', '1999-09-10', 139, '剧情', 8.8, 1),
('盗梦空间', 'https://example.com/posters/inception.jpg', '2010-07-16', 148, '科幻', 9.3, 1),
('黑客帝国', 'https://example.com/posters/matrix.jpg', '1999-03-31', 136, '科幻', 8.7, 1),
('飞屋环游记', 'https://example.com/posters/up.jpg', '2009-05-29', 96, '动画', 8.9, 1),
('楚门的世界', 'https://example.com/posters/truman.jpg', '1998-06-05', 103, '剧情', 8.9, 1),
('海上钢琴师', 'https://example.com/posters/1900.jpg', '1998-10-28', 165, '剧情', 9.3, 1);

-- 用户基础信息测试数据
INSERT INTO users (username, password_hash, is_admin, is_blocked, created_at, updated_at) VALUES
('john_doe', '$2a$10$xJwL5v5zP0bWQZq3VdTgE.9XUJkYlWmNnOcQrSsTtUuVvWwXxYyZz', 0, 0, '2023-01-15 09:23:45', '2023-01-15 09:23:45'),
('alice_smith', '$2a$10$yH9KjLmNoPqRsTuVwXyZzA1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6', 0, 1, '2023-02-20 14:12:33', '2023-06-01 08:45:21'),
('admin_root', '$2a$10$zI8J7H6G5F4E3D2C1B0A9Z8Y7X6W5V4U3T2S1R0Q9P8O7N6M5L4K3', 1, 0, '2022-11-05 00:00:01', '2023-05-20 18:30:15'),
('emma_watson', '$2a$10$aBcDeFgHiJkLmNoPqRsTuV1W2X3Y4Z5A6B7C8D9E0F1G2H3I4J5K6', 0, 0, '2023-03-08 11:45:10', '2023-03-08 11:45:10'),
('blocked_user', '$2a$10$bCdEfGhIjKlMnOpQrStUv1W2X3Y4Z5A6B7C8D9E0F1G2H3I4J5K6L7', 0, 1, '2023-04-01 16:30:00', '2023-05-15 12:00:00'),
('michael_brown', '$2a$10$cDeFgHiJkLmNoPqRsTuVwXyZzA1B2C3D4E5F6G7H8I9J0K1L2M3N4O5', 0, 0, '2023-01-30 10:15:22', '2023-05-25 14:20:33'),
('sys_admin', '$2a$10$dEfGhIjKlMnOpQrStUvWxYzA1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6', 1, 0, '2022-12-25 08:00:00', '2023-06-10 09:10:05'),
('sarah_connor', '$2a$10$eFgHiJkLmNoPqRsTuVwXyZzA1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7', 0, 0, '2023-05-12 13:45:18', '2023-05-12 13:45:18'),
('hacker_attempt', '$2a$10$fGhIjKlMnOpQrStUvWxYzA1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8', 0, 1, '2023-06-05 03:15:42', '2023-06-05 03:20:00'),
('guest_account', '$2a$10$gHiJkLmNoPqRsTuVwXyZzA1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9', 0, 0, '2023-04-18 19:30:15', '2023-04-18 19:30:15'),
('robert_johnson', '$2a$10$hIjKlMnOpQrStUvWxYzA1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0', 0, 0, '2023-03-15 08:20:10', '2023-03-15 08:20:10'),
('linda_williams', '$2a$10$iJkLmNoPqRsTuVwXyZzA1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0', 0, 0, '2023-04-05 12:30:45', '2023-04-05 12:30:45'),
('david_miller', '$2a$10$jKlMnOpQrStUvWxYzA1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0U1', 1, 0, '2022-12-10 09:15:30', '2023-05-18 11:25:40'),
('susan_wilson', '$2a$10$kLmNoPqRsTuVwXyZzA1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0U1V2', 0, 1, '2023-05-20 16:40:15', '2023-06-01 10:10:10'),
('james_anderson', '$2a$10$lMnOpQrStUvWxYzA1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0U1V2W3', 0, 0, '2023-02-28 14:25:50', '2023-02-28 14:25:50'),
('patricia_thomas', '$2a$10$mNoPqRsTuVwXyZzA1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0U1V2W3X4', 0, 0, '2023-01-10 10:10:10', '2023-05-15 15:15:15'),
('richard_jackson', '$2a$10$nOpQrStUvWxYzA1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0U1V2W3X4Y5', 1, 0, '2022-11-20 08:08:08', '2023-06-05 18:18:18'),
('jennifer_white', '$2a$10$oPqRsTuVwXyZzA1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0U1V2W3X4Y5Z6', 0, 0, '2023-04-22 19:19:19', '2023-04-22 19:19:19'),
('charles_harris', '$2a$10$pQrStUvWxYzA1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0U1V2W3X4Y5Z6A7', 0, 1, '2023-06-08 20:20:20', '2023-06-08 20:25:25'),
('elizabeth_martin', '$2a$10$qRsTuVwXyZzA1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0U1V2W3X4Y5Z6A7B8', 0, 0, '2023-03-30 07:07:07', '2023-03-30 07:07:07');

-- 系统日志表测试数据
INSERT INTO logs (user_id, action, ip_address, user_agent, details, created_at) VALUES
(1, 'login', '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36', '用户登录成功', '2023-06-01 09:12:34'),
(2, 'profile_update', '203.156.34.12', 'Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1', '更新了个人头像', '2023-06-01 10:23:45'),
(3, 'password_change', '172.16.34.56', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.3 Safari/605.1.15', '密码修改成功', '2023-06-02 14:35:21'),
(4, 'order_create', '118.25.67.89', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0', '创建订单 #20230602001', '2023-06-02 11:42:18'),
(5, 'logout', '210.45.67.89', 'Mozilla/5.0 (Linux; Android 11; SM-G998B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36', '用户退出登录', '2023-06-03 08:12:39'),
(6, 'file_upload', '103.45.67.89', 'Mozilla/5.0 (iPad; CPU OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.80 Mobile/15E148 Safari/604.1', '上传文件: contract.pdf', '2023-06-03 13:24:56'),
(7, 'login_failed', '192.168.1.150', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 Edg/91.0.864.59', '登录失败: 密码错误', '2023-06-04 15:37:42'),
(8, 'data_export', '223.104.63.178', 'Mozilla/5.0 (Linux; Android 10; M2006C3LVG) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36', '导出用户活动报告', '2023-06-05 18:45:13'),
(9, 'system_access', '172.16.34.78', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36', '访问管理员仪表盘', '2023-06-06 10:52:31'),
(10, 'account_lock', '127.0.0.1', 'PostmanRuntime/7.28.4', '账户因多次失败尝试被锁定', '2023-06-07 16:53:27'),
(11, 'login', '192.168.1.101', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36', '用户登录成功', '2023-06-08 09:15:22'),
(12, 'profile_update', '203.156.34.13', 'Mozilla/5.0 (iPhone; CPU iPhone OS 14_7 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1', '更新了个人资料', '2023-06-08 10:30:45'),
(13, 'admin_login', '172.16.34.57', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.3 Safari/605.1.15', '管理员登录', '2023-06-09 14:40:21'),
(14, 'order_cancel', '118.25.67.90', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:90.0) Gecko/20100101 Firefox/90.0', '取消订单 #20230609001', '2023-06-09 11:55:18'),
(15, 'logout', '210.45.67.90', 'Mozilla/5.0 (Linux; Android 11; SM-G998B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.115 Mobile Safari/537.36', '用户退出登录', '2023-06-10 08:20:39'),
(16, 'file_download', '103.45.67.90', 'Mozilla/5.0 (iPad; CPU OS 14_7 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/92.0.4515.90 Mobile/15E148 Safari/604.1', '下载文件: invoice.pdf', '2023-06-10 13:35:56'),
(17, 'login_failed', '192.168.1.151', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36 Edg/92.0.902.55', '登录失败: 账户已锁定', '2023-06-11 15:50:42'),
(18, 'data_import', '223.104.63.179', 'Mozilla/5.0 (Linux; Android 10; M2006C3LVG) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.115 Mobile Safari/537.36', '导入用户数据', '2023-06-11 18:55:13'),
(19, 'system_config', '172.16.34.79', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36', '修改系统配置', '2023-06-12 10:05:31'),
(20, 'account_unlock', '127.0.0.1', 'PostmanRuntime/7.29.0', '管理员解锁账户', '2023-06-12 16:10:27');

-- 放映厅测试数据
INSERT INTO halls (`name`, `capacity`, `rows`, `cols`, `facilities`, `status`) VALUES
('IMAX巨幕厅', 300, 15, 20, '{"screen_type":"IMAX","sound_system":"Dolby Atmos","3d_support":true}', 1),
('杜比全景声厅', 200, 10, 20, '{"screen_type":"Standard","sound_system":"Dolby Atmos","3d_support":true}', 1),
('VIP贵宾厅', 50, 5, 10, '{"screen_type":"Standard","sound_system":"5.1","recliner_seats":true,"food_service":true}', 1),
('4DX动感厅', 120, 10, 12, '{"screen_type":"Standard","sound_system":"7.1","4dx_motion":true,"environmental_effects":true}', 1),
('儿童主题厅', 80, 8, 10, '{"screen_type":"Standard","sound_system":"5.1","child_friendly":true,"decor_theme":"Cartoon"}', 1),
('普通厅1号', 180, 12, 15, '{"screen_type":"Standard","sound_system":"5.1"}', 1),
('普通厅2号', 160, 10, 16, '{"screen_type":"Standard","sound_system":"5.1"}', 1),
('激光厅', 220, 11, 20, '{"screen_type":"Laser","sound_system":"Dolby Atmos","3d_support":true}', 1),
('情侣厅', 60, 6, 10, '{"screen_type":"Standard","sound_system":"5.1","couple_seats":true}', 1),
('临时厅', 100, 10, 10, '{"screen_type":"Temporary","sound_system":"Stereo"}', 0),
('巨幕厅2号', 350, 14, 25, '{"screen_type":"IMAX","sound_system":"Dolby Atmos","3d_support":true}', 1),
('杜比厅2号', 180, 9, 20, '{"screen_type":"Standard","sound_system":"Dolby Atmos","3d_support":true}', 1),
('VIP贵宾厅2号', 40, 4, 10, '{"screen_type":"Standard","sound_system":"5.1","recliner_seats":true,"food_service":true}', 1),
('4DX动感厅2号', 100, 10, 10, '{"screen_type":"Standard","sound_system":"7.1","4dx_motion":true,"environmental_effects":true}', 1),
('儿童主题厅2号', 70, 7, 10, '{"screen_type":"Standard","sound_system":"5.1","child_friendly":true,"decor_theme":"Superhero"}', 1),
('普通厅3号', 150, 10, 15, '{"screen_type":"Standard","sound_system":"5.1"}', 1),
('普通厅4号', 140, 10, 14, '{"screen_type":"Standard","sound_system":"5.1"}', 1),
('激光厅2号', 200, 10, 20, '{"screen_type":"Laser","sound_system":"Dolby Atmos","3d_support":true}', 1),
('情侣厅2号', 50, 5, 10, '{"screen_type":"Standard","sound_system":"5.1","couple_seats":true}', 1),
('测试厅', 80, 8, 10, '{"screen_type":"Temporary","sound_system":"Stereo"}', 0);

-- 放映场次测试数据
INSERT INTO screenings
(movie_id, hall_id, start_time, end_time, price, remaining_seats, status)
VALUES
(1, 3, '2023-05-15 10:00:00', '2023-05-15 12:30:00', 89.90, 45, 1),
(5, 2, '2023-05-15 13:00:00', '2023-05-15 14:45:00', 45.00, 20, 1),
(6, 9, '2023-05-15 15:00:00', '2023-05-15 17:20:00', 65.00, 0, 0),
(4, 10, '2023-05-15 18:00:00', '2023-05-15 20:15:00', 150.00, 8, 1),
(5, 4, '2023-05-15 20:30:00', '2023-05-15 22:45:00', 120.00, 15, 1),
(6, 1, '2023-05-16 09:30:00', '2023-05-16 11:40:00', 35.00, 120, 1),
(7, 9, '2023-05-16 21:00:00', '2023-05-16 23:10:00', 88.00, 12, 1),
(8, 8, '2023-05-17 14:00:00', '2023-05-17 16:30:00', 75.00, 65, 1),
(9, 7, '2023-05-17 19:00:00', '2023-05-17 21:05:00', 50.00, 90, 1),
(5, 10, '2023-05-18 16:00:00', '2023-05-18 18:00:00', 40.00, 100, 1),
(11, 11, '2023-05-19 10:30:00', '2023-05-19 13:30:00', 99.90, 30, 1),
(12, 12, '2023-05-19 14:00:00', '2023-05-19 17:20:00', 85.00, 15, 1),
(13, 13, '2023-05-19 18:00:00', '2023-05-19 20:30:00', 75.00, 0, 0),
(14, 14, '2023-05-20 11:00:00', '2023-05-20 14:15:00', 65.00, 5, 1),
(15, 15, '2023-05-20 15:00:00', '2023-05-20 17:20:00', 55.00, 20, 1),
(16, 16, '2023-05-21 10:00:00', '2023-05-21 12:15:00', 45.00, 100, 1),
(17, 17, '2023-05-21 13:00:00', '2023-05-21 15:15:00', 50.00, 80, 1),
(18, 18, '2023-05-22 16:00:00', '2023-05-22 18:30:00', 70.00, 60, 1),
(19, 19, '2023-05-22 19:00:00', '2023-05-22 21:10:00', 65.00, 40, 1),
(20, 20, '2023-05-23 20:00:00', '2023-05-23 22:30:00', 80.00, 70, 1);


-- 订单信息表测试数据
INSERT INTO orders (order_no, user_id, screening_id, total_amount, seat_count, status, contact_phone, created_at) VALUES
-- 1. 待支付的订单（新创建）
('ORD20230001', 1, 5, 120.00, 2, 0, '13800138001', NOW()),
-- 2. 已支付的订单（正常购票）
('ORD20230002', 2, 3, 180.00, 3, 1, '13800138002', NOW()),
-- 3. 已取消的订单（超时未支付）
('ORD20230003', 3, 7, 90.00, 1, 2, '13800138003', NOW()),
-- 4. 已完成的订单（观影结束）
('ORD20230004', 4, 2, 240.00, 4, 3, '13800138004', '2023-05-01 14:30:00'),
-- 5. 已退款的订单（用户申请退款）
('ORD20230005', 5, 8, 60.00, 1, 4, '13800138005', NOW()),
-- 6. VIP用户的大额订单
('ORD20230006', 6, 1, 600.00, 10, 1, '13800138006', NOW()),
-- 7. 即将开始的场次订单
('ORD20230007', 7, 10, 150.00, 3, 1, '13800138007', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
-- 8. 历史订单（一个月前）
('ORD20230008', 8, 4, 80.00, 2, 3, '13800138008', DATE_SUB(NOW(), INTERVAL 1 MONTH)),
-- 9. 不同价格的订单组合
('ORD20230009', 9, 6, 45.00, 1, 1, '13800138009', NOW()),
-- 10. 最新创建的测试订单
('ORD20230010', 10, 9, 210.00, 3, 0, '13800138010', NOW()),
-- 11. 新创建的订单
('ORD20230011', 11, 11, 199.80, 2, 0, '13800138011', NOW()),
-- 12. 已支付的订单
('ORD20230012', 12, 12, 255.00, 3, 1, '13800138012', NOW()),
-- 13. 已取消的订单
('ORD20230013', 13, 13, 75.00, 1, 2, '13800138013', NOW()),
-- 14. 已完成的订单
('ORD20230014', 14, 14, 260.00, 4, 3, '13800138014', '2023-05-05 14:30:00'),
-- 15. 已退款的订单
('ORD20230015', 15, 15, 55.00, 1, 4, '13800138015', NOW()),
-- 16. VIP用户的大额订单
('ORD20230016', 16, 16, 450.00, 10, 1, '13800138016', NOW()),
-- 17. 即将开始的场次订单
('ORD20230017', 17, 17, 150.00, 3, 1, '13800138017', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
-- 18. 历史订单
('ORD20230018', 18, 18, 140.00, 2, 3, '13800138018', DATE_SUB(NOW(), INTERVAL 2 MONTH)),
-- 19. 不同价格的订单
('ORD20230019', 19, 19, 65.00, 1, 1, '13800138019', NOW()),
-- 20. 最新创建的测试订单
('ORD20230020', 20, 20, 240.00, 3, 0, '13800138020', NOW());

-- 支付记录表测试数据
INSERT INTO payments (order_id, amount, payment_method, transaction_id, status, pay_time, created_at, updated_at) VALUES
(1, 199.00, 'Alipay', 'ALI20230501123456', 1, '2023-05-01 10:23:45', '2023-05-01 10:20:00', '2023-05-01 10:23:45'),
(2, 599.00, 'WeChat Pay', 'WX20230502135678', 1, '2023-05-02 14:30:22', '2023-05-02 14:25:00', '2023-05-02 14:30:22'),
(3, 1299.00, 'Credit Card', 'CC202305031234567890', 2, NULL, '2023-05-03 09:15:33', '2023-05-03 09:18:00'),
(4, 89.50, 'Alipay', 'ALI20230504112233', 1, '2023-05-04 11:45:12', '2023-05-04 11:40:00', '2023-05-04 11:45:12'),
(5, 299.00, 'WeChat Pay', 'WX20230505123456', 0, NULL, '2023-05-05 16:20:18', '2023-05-05 16:20:18'),
(6, 599.00, 'Bank Transfer', 'BT2023050611223344', 1, '2023-05-06 13:15:45', '2023-05-06 13:10:00', '2023-05-06 13:15:45'),
(7, 199.00, 'Alipay', 'ALI20230507123456', 1, '2023-05-07 18:30:22', '2023-05-07 18:25:00', '2023-05-07 18:30:22'),
(8, 899.00, 'Credit Card', 'CC202305081234567891', 2, NULL, '2023-05-08 10:45:33', '2023-05-08 10:50:00'),
(9, 159.00, 'WeChat Pay', 'WX20230509112233', 1, '2023-05-09 12:15:12', '2023-05-09 12:10:00', '2023-05-09 12:15:12'),
(10, 399.00, 'Alipay', 'ALI20230510123456', 0, NULL, '2023-05-10 15:30:18', '2023-05-10 15:30:18'),
(11, 699.00, 'Bank Transfer', 'BT2023051111223345', 1, '2023-05-11 09:45:45', '2023-05-11 09:40:00', '2023-05-11 09:45:45'),
(12, 259.00, 'WeChat Pay', 'WX20230512123456', 1, '2023-05-12 14:20:22', '2023-05-12 14:15:00', '2023-05-12 14:20:22'),
(13, 999.00, 'Credit Card', 'CC202305131234567892', 1, '2023-05-13 11:35:33', '2023-05-13 11:30:00', '2023-05-13 11:35:33'),
(14, 79.00, 'Alipay', 'ALI20230514112233', 2, NULL, '2023-05-14 16:50:12', '2023-05-14 16:55:00'),
(15, 499.00, 'WeChat Pay', 'WX20230515123456', 1, '2023-05-15 10:15:18', '2023-05-15 10:10:00', '2023-05-15 10:15:18'),
(16, 1199.00, 'Bank Transfer', 'BT2023051611223346', 0, NULL, '2023-05-16 13:40:45', '2023-05-16 13:40:45'),
(17, 299.00, 'Alipay', 'ALI20230517123456', 1, '2023-05-17 17:25:22', '2023-05-17 17:20:00', '2023-05-17 17:25:22'),
(18, 899.00, 'Credit Card', 'CC202305181234567893', 1, '2023-05-18 09:50:33', '2023-05-18 09:45:00', '2023-05-18 09:50:33'),
(19, 159.00, 'WeChat Pay', 'WX20230519112233', 2, NULL, '2023-05-19 12:05:12', '2023-05-19 12:10:00'),
(20, 399.00, 'Alipay', 'ALI20230520123456', 1, '2023-05-20 15:30:18', '2023-05-20 15:25:00', '2023-05-20 15:30:18');

-- 退票申请表测试数据
INSERT INTO refunds (order_id, user_id, reason, status, admin_id, processed_at, refund_amount, created_at, updated_at) VALUES
(1, 1, '行程有变，不需要了', 1, 5, '2023-05-10 14:30:00', 150.00, '2023-05-08 09:15:22', '2023-05-10 14:30:00'),
(2, 2, '买错日期了', 2, 6, '2023-05-12 10:45:00', 200.00, '2023-05-09 11:20:33', '2023-05-12 10:45:00'),
(3, 3, '临时有事无法参加', 0, NULL, NULL, 180.50, '2023-05-10 16:45:12', '2023-05-10 16:45:12'),
(4, 4, '找到了更便宜的票', 1, 5, '2023-05-15 09:15:00', 120.00, '2023-05-11 14:30:45', '2023-05-15 09:15:00'),
(5, 5, '重复下单', 1, 7, '2023-05-16 13:20:00', 90.00, '2023-05-12 08:15:33', '2023-05-16 13:20:00'),
(6, 6, '演出取消了', 1, 5, '2023-05-18 16:00:00', 300.00, '2023-05-13 10:30:22', '2023-05-18 16:00:00'),
(7, 7, '对座位不满意', 2, 6, '2023-05-19 11:30:00', 250.00, '2023-05-14 15:45:11', '2023-05-19 11:30:00'),
(8, 8, '朋友已经买了票', 0, NULL, NULL, 175.00, '2023-05-15 19:20:33', '2023-05-15 19:20:33'),
(9, 9, '疫情原因无法出行', 1, 7, '2023-05-20 14:15:00', 210.00, '2023-05-16 12:10:44', '2023-05-20 14:15:00'),
(10, 10, '票价太贵了', 2, 5, '2023-05-21 09:45:00', 180.00, '2023-05-17 17:30:55', '2023-05-21 09:45:00'),
(11, 11, '时间冲突', 1, 6, '2023-05-22 10:30:00', 160.00, '2023-05-18 14:25:00', '2023-05-22 10:30:00'),
(12, 12, '演出内容变更', 0, NULL, NULL, 140.00, '2023-05-19 20:15:00', '2023-05-19 20:15:00'),
(13, 13, '个人原因无法参加', 1, 7, '2023-05-23 15:45:00', 190.00, '2023-05-20 11:40:00', '2023-05-23 15:45:00'),
(14, 14, '购票时信息填写错误', 2, 5, '2023-05-24 16:20:00', 220.00, '2023-05-21 13:35:00', '2023-05-24 16:20:00'),
(15, 15, '演出延期', 1, 6, '2023-05-25 11:10:00', 130.00, '2023-05-22 18:50:00', '2023-05-25 11:10:00'),
(16, 16, '付款后才发现有优惠', 0, NULL, NULL, 110.00, '2023-05-23 09:25:00', '2023-05-23 09:25:00'),
(17, 17, '身体不适无法参加', 1, 7, '2023-05-26 14:50:00', 170.00, '2023-05-24 15:30:00', '2023-05-26 14:50:00'),
(18, 18, '票买多了', 2, 5, '2023-05-27 10:15:00', 240.00, '2023-05-25 12:45:00', '2023-05-27 10:15:00'),
(19, 19, '对演出内容不满意', 1, 6, '2023-05-28 13:40:00', 155.00, '2023-05-26 19:20:00', '2023-05-28 13:40:00'),
(20, 20, '工作安排冲突', 0, NULL, NULL, 165.00, '2023-05-27 08:10:00', '2023-05-27 08:10:00');
