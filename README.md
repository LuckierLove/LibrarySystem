# 图书借阅管理系统

## 项目简介

这是一个基于Java Swing开发的图书借阅管理系统，是九江学院《高级Java》课程的期末大作业项目。系统采用分层架构设计，实现了图书管理、用户管理、借阅管理等核心功能。

## 技术栈

- **开发语言**：Java (JDK 8+)
- **GUI框架**：Swing
- **构建工具**：Maven
- **数据库**：MySQL 8.0
- **开发工具**：Eclipse
- **辅助工具**：Lombok（简化实体类开发）

## 功能特性

### 用户功能
- ✅ 用户登录（支持管理员和读者两种角色）
- ✅ 图书浏览与搜索
- ✅ 图书借阅（带库存检查和重复借阅验证）
- ✅ 图书归还
- ✅ 查看个人借阅记录

### 管理员功能
- ✅ 查看所有用户的借阅记录
- ✅ 图书管理（增删改查）
- ✅ 用户管理

## 项目结构

```
LibrarySystem/
├── src/main/java/cn/jju/library/
│   ├── entity/          # 实体层（Book, User, BorrowRecord）
│   ├── dao/             # 数据访问层（BookDao, UserDao, BorrowRecordDao）
│   ├── service/         # 业务逻辑层（BookService, UserService, BorrowService）
│   ├── ui/              # 表现层（LoginFrame, MainFrame, BorrowRecordDialog）
│   └── util/            # 工具类（DBUtil）
├── src/main/resources/
│   └── db.properties    # 数据库配置文件
├── database/
│   └── schema.sql       # 数据库建表脚本
├── docs/
│   └── 设计文档.md      # 详细设计文档
└── pom.xml              # Maven配置文件
```

## 快速开始

### 1. 环境准备

- 安装 JDK 8 或更高版本
- 安装 MySQL 8.0
- 安装 Maven
- 安装 Eclipse（推荐）或其他Java IDE

### 2. 数据库配置

1. 启动MySQL服务
2. 执行数据库脚本：
```bash
mysql -u root -p < database/schema.sql
```

3. 修改配置文件 `src/main/resources/db.properties`：
```properties
db.url=jdbc:mysql://localhost:3306/library_system?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
db.username=root
db.password=你的数据库密码
```

### 3. 编译运行

#### 方法一：使用Maven命令行
```bash
# 编译项目
mvn clean compile

# 运行主程序
mvn exec:java -Dexec.mainClass="cn.jju.library.ui.LoginFrame"
```

#### 方法二：在Eclipse中运行
1. 导入Maven项目
2. 右键点击 `LoginFrame.java`
3. 选择 `Run As` → `Java Application`

### 4. 默认账号

系统预置了以下测试账号：

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| admin | admin123 | 管理员 | 拥有所有权限 |
| reader1 | reader123 | 读者 | 张三 |
| reader2 | reader123 | 读者 | 李四 |

## 核心功能说明

### 借书流程
1. 用户登录系统
2. 在主界面浏览或搜索图书
3. 选择要借阅的图书
4. 点击"借书"按钮
5. 输入借阅天数（建议30天）
6. 系统验证库存并创建借阅记录
7. 自动减少图书库存

### 还书流程
1. 在主界面选择已借阅的图书
2. 点击"还书"按钮
3. 确认归还操作
4. 系统更新借阅记录状态
5. 自动增加图书库存

## 系统架构

本系统采用经典的**四层架构**设计：

```
展现层 (UI Layer)
    ↓ 调用
业务逻辑层 (Service Layer)
    ↓ 调用
数据访问层 (DAO Layer)
    ↓ 使用
实体层 (Entity Layer)
```

每层职责明确，相互独立，便于维护和扩展。

## 数据库设计

### 核心表结构

1. **books**（图书信息表）
   - 存储图书基本信息
   - 包含total_quantity（总数量）和available_quantity（可借数量）

2. **users**（用户信息表）
   - 存储用户账号信息
   - 区分管理员和普通读者

3. **borrow_records**（借阅记录表）
   - 记录借阅和归还信息
   - 通过外键关联users和books表

详细的数据库设计请参考 `database/schema.sql`。

## 设计文档

详细的设计文档请查看：[docs/设计文档.md](docs/设计文档.md)

文档包含：
- 功能设计说明（借书/还书流程）
- 类图说明（类之间的调用关系）
- 核心代码解析（带详细注释）

## 开发规范

- 代码注释：所有类和方法都有JavaDoc注释
- 命名规范：变量名、函数名见名知意
- 异常处理：完善的异常捕获和错误提示
- 代码风格：遵循Java编码规范

## 测试数据

系统预置了10本测试图书，包括：
- Java编程思想
- 深入理解Java虚拟机
- 设计模式
- 算法导论
- 数据库系统概念
- 活着
- 三体
- 红楼梦
- 明朝那些事儿
- 人类简史

## 常见问题

### Q1: 数据库连接失败？
A: 请检查：
- MySQL服务是否启动
- 数据库配置文件中的用户名和密码是否正确
- 数据库library_system是否已创建

### Q2: 编译时找不到MySQL驱动？
A: 执行 `mvn clean install` 下载依赖

### Q3: Lombok不生效？
A: 在Eclipse中安装Lombok插件，参考：https://projectlombok.org/setup/eclipse

## 项目亮点

1. **严格的分层架构**：UI、Service、DAO、Entity职责分明
2. **完善的业务逻辑**：借还书功能包含多重验证和错误处理
3. **友好的用户界面**：简洁美观的Swing界面
4. **详细的代码注释**：每个类和方法都有完整的JavaDoc
5. **规范的数据库设计**：包含外键约束、索引、CHECK约束等
6. **使用现代工具**：Maven依赖管理、Lombok简化开发

## 作者信息

- **项目名称**：图书借阅管理系统
- **课程**：九江学院《高级Java》课程
- **开发团队**：图书管理系统开发组
- **版本**：1.0.0
- **日期**：2025-12-28

## 许可证

本项目仅供学习交流使用。
