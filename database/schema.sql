-- ============================================
-- 图书借阅管理系统数据库设计
-- 数据库名称: library_system
-- 版本: 1.0.0
-- 日期: 2025-12-28
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS library_system 
    DEFAULT CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE library_system;

-- ============================================
-- 表1: 图书信息表 (books)
-- 功能: 存储图书基本信息和库存数量
-- ============================================
CREATE TABLE IF NOT EXISTS books (
    book_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '图书ID，主键，自增',
    book_name VARCHAR(200) NOT NULL COMMENT '图书名称',
    author VARCHAR(100) NOT NULL COMMENT '作者',
    publisher VARCHAR(100) COMMENT '出版社',
    isbn VARCHAR(20) UNIQUE COMMENT 'ISBN编号，国际标准书号',
    category VARCHAR(50) COMMENT '图书分类（如：文学、科技、历史等）',
    total_quantity INT NOT NULL DEFAULT 0 COMMENT '图书总数量',
    available_quantity INT NOT NULL DEFAULT 0 COMMENT '可借数量',
    publish_date DATE COMMENT '出版日期',
    price DECIMAL(10, 2) COMMENT '图书价格',
    description TEXT COMMENT '图书简介',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 约束：可借数量不能大于总数量
    CONSTRAINT chk_available_quantity CHECK (available_quantity >= 0 AND available_quantity <= total_quantity),
    -- 索引：提高查询效率
    INDEX idx_book_name (book_name),
    INDEX idx_author (author),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书信息表';

-- ============================================
-- 表2: 用户信息表 (users)
-- 功能: 存储读者和管理员的账号信息
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID，主键，自增',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名，唯一',
    password VARCHAR(100) NOT NULL COMMENT '密码（建议实际使用时加密存储）',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    gender ENUM('男', '女', '其他') DEFAULT '其他' COMMENT '性别',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '电子邮箱',
    user_type ENUM('管理员', '读者') NOT NULL DEFAULT '读者' COMMENT '用户类型',
    status ENUM('正常', '禁用') NOT NULL DEFAULT '正常' COMMENT '账号状态',
    max_borrow_count INT DEFAULT 5 COMMENT '最大可借数量',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 索引：提高查询效率
    INDEX idx_username (username),
    INDEX idx_user_type (user_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- ============================================
-- 表3: 借阅记录表 (borrow_records)
-- 功能: 记录图书的借阅和归还信息
-- ============================================
CREATE TABLE IF NOT EXISTS borrow_records (
    record_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '借阅记录ID，主键，自增',
    user_id INT NOT NULL COMMENT '用户ID，外键关联users表',
    book_id INT NOT NULL COMMENT '图书ID，外键关联books表',
    borrow_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借阅日期',
    due_date DATE NOT NULL COMMENT '应还日期',
    return_date TIMESTAMP NULL COMMENT '实际归还日期，NULL表示未归还',
    status ENUM('借阅中', '已归还', '已逾期') NOT NULL DEFAULT '借阅中' COMMENT '借阅状态',
    remarks TEXT COMMENT '备注信息',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    
    -- 外键约束：确保数据一致性
    CONSTRAINT fk_borrow_user 
        FOREIGN KEY (user_id) REFERENCES users(user_id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_borrow_book 
        FOREIGN KEY (book_id) REFERENCES books(book_id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    
    -- 索引：提高查询效率
    INDEX idx_user_id (user_id),
    INDEX idx_book_id (book_id),
    INDEX idx_status (status),
    INDEX idx_borrow_date (borrow_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='借阅记录表';

-- ============================================
-- 插入测试数据
-- ============================================

-- 插入管理员账号（默认密码：admin123）
INSERT INTO users (username, password, real_name, gender, phone, email, user_type, status) VALUES
('admin', 'admin123', '系统管理员', '男', '13800138000', 'admin@jju.edu.cn', '管理员', '正常'),
('reader1', 'reader123', '张三', '男', '13900139001', 'zhangsan@jju.edu.cn', '读者', '正常'),
('reader2', 'reader123', '李四', '女', '13900139002', 'lisi@jju.edu.cn', '读者', '正常');

-- 插入测试图书数据
INSERT INTO books (book_name, author, publisher, isbn, category, total_quantity, available_quantity, publish_date, price, description) VALUES
('Java编程思想（第4版）', 'Bruce Eckel', '机械工业出版社', '9787111213826', '计算机', 10, 10, '2007-06-01', 108.00, 'Java学习经典著作，深入探讨Java语言特性'),
('深入理解Java虚拟机（第3版）', '周志明', '机械工业出版社', '9787111641247', '计算机', 8, 8, '2019-12-01', 129.00, '深入剖析JVM工作原理和优化技术'),
('设计模式：可复用面向对象软件的基础', 'Erich Gamma等', '机械工业出版社', '9787111211655', '计算机', 5, 5, '2000-09-01', 45.00, '软件设计经典著作，讲解23种设计模式'),
('算法导论（第3版）', 'Thomas H.Cormen等', '机械工业出版社', '9787111407010', '计算机', 6, 6, '2012-12-01', 128.00, '算法领域的经典教材'),
('数据库系统概念（第6版）', 'Abraham Silberschatz等', '机械工业出版社', '9787111375296', '计算机', 7, 7, '2012-03-01', 89.00, '数据库理论与实践的权威教材'),
('活着', '余华', '作家出版社', '9787506365437', '文学', 15, 15, '2012-08-01', 20.00, '当代文学经典作品'),
('三体', '刘慈欣', '重庆出版社', '9787536692930', '科幻', 12, 12, '2008-01-01', 23.00, '中国科幻文学里程碑之作'),
('红楼梦', '曹雪芹', '人民文学出版社', '9787020002207', '古典文学', 10, 10, '1996-12-01', 59.70, '中国古典四大名著之一'),
('明朝那些事儿', '当年明月', '浙江人民出版社', '9787213034558', '历史', 9, 9, '2006-09-01', 24.80, '通俗历史读物，讲述明朝历史'),
('人类简史', '尤瓦尔·赫拉利', '中信出版社', '9787508647357', '历史', 11, 11, '2014-11-01', 68.00, '从认知革命到科技革命的人类发展史');

-- ============================================
-- 创建视图：当前借阅情况统计
-- ============================================
CREATE OR REPLACE VIEW v_current_borrows AS
SELECT 
    br.record_id,
    u.username,
    u.real_name,
    b.book_name,
    b.author,
    br.borrow_date,
    br.due_date,
    br.status,
    DATEDIFF(CURDATE(), br.due_date) AS overdue_days
FROM borrow_records br
JOIN users u ON br.user_id = u.user_id
JOIN books b ON br.book_id = b.book_id
WHERE br.status = '借阅中'
ORDER BY br.borrow_date DESC;

-- ============================================
-- 创建存储过程：更新逾期状态
-- ============================================
DELIMITER $$
CREATE PROCEDURE update_overdue_status()
BEGIN
    -- 更新已逾期但未归还的借阅记录状态
    UPDATE borrow_records 
    SET status = '已逾期'
    WHERE status = '借阅中' 
    AND due_date < CURDATE();
END$$
DELIMITER ;

-- ============================================
-- 数据库初始化完成
-- ============================================
