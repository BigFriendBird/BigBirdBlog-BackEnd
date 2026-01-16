# BigBird 博客后端

基于 Spring Boot + MyBatis的个人博客后端服务。

## 技术栈

- Spring Boot 3.2.1
- MyBatis Plus 3.5.5
- MySQL 8.0
- Lombok

## 功能特性

- ✅ 文章管理（CRUD）
- ✅ 分页查询
- ✅ 文章搜索
- ✅ 浏览量统计
- ✅ RESTful API

## 快速开始

### 1. 配置数据库

首先创建数据库并执行初始化脚本：

```bash
mysql -u root -p < src/main/resources/db/schema.sql
```

### 2. 修改配置

编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bigbird_blog
    username: your_username
    password: your_password
```

### 3. 启动项目

```bash
# Maven
mvn spring-boot:run

# 或直接运行主类
# com.bigbird.blog.BlogApplication
```

服务将在 http://localhost:8080 启动

## API 接口

### 文章相关

- `GET /articles/latest?limit=3` - 获取最新文章
- `GET /articles?page=1&size=10&keyword=` - 分页查询文章
- `GET /articles/{id}` - 获取文章详情
- `POST /articles` - 创建文章
- `PUT /articles/{id}` - 更新文章
- `DELETE /articles/{id}` - 删除文章

## 项目结构

```
src/main/java/com/bigbird/blog/
├── BlogApplication.java      # 启动类
├── common/                   # 通用类
│   └── Result.java          # 统一响应结果
├── config/                   # 配置类
│   └── MyMetaObjectHandler.java  # 字段自动填充
├── controller/               # 控制器
│   └── ArticleController.java
├── entity/                   # 实体类
│   └── Article.java
├── mapper/                   # Mapper接口
│   └── ArticleMapper.java
└── service/                  # 服务层
    ├── ArticleService.java
    └── impl/
        └── ArticleServiceImpl.java
```

## 开发计划

- [ ] 用户认证（JWT）
- [ ] 评论系统
- [ ] 文件上传
- [ ] 后台管理
- [ ] 文章分类
- [ ] 标签管理

## License

MIT
