# 学生管理系统

一个基于 Spring Boot + Vue 3 + MySQL 的现代化学生管理系统，支持多角色权限管理、课程管理、选课管理和成绩管理。

## 📋 目录

- [功能特性](#功能特性)
- [技术栈](#技术栈)
- [系统架构](#系统架构)
- [快速开始](#快速开始)
- [数据库设计](#数据库设计)
- [API 文档](#api-文档)
- [用户角色说明](#用户角色说明)
- [项目结构](#项目结构)

## ✨ 功能特性

### 核心功能
- **用户认证与授权**：基于 JWT 的无状态认证，支持三种角色（管理员、教师、学生）
- **多角色权限管理**：细粒度的权限控制，不同角色具有不同的操作权限
- **学生信息管理**：学生基本信息的增删改查，支持院系、专业、班级关联
- **教师信息管理**：教师信息维护，支持院系归属
- **院系专业管理**：院系和专业的层级管理
- **班级管理**：班级信息维护，包含班主任、入学年份等信息

### 课程管理
- **公共课程管理**：面向全体学生的公共课程，所有学生默认可见
- **开课课程管理**：由教师开设的选修课程，支持选课人数限制
- **双课程体系**：区分公共课和选修课，灵活的课程分类

### 选课系统
- **学生选课**：学生可浏览并选择开放的选修课程
- **选课管理**：查看选课记录，支持退课操作
- **人数限制**：自动控制选课人数上限
- **选课状态**：支持开放、关闭、已结课等多种状态

### 成绩管理
- **双成绩体系**
  - **选课成绩（offering_score）**：选修课程成绩，基于 student_course 选课记录
  - **公共课成绩（enrollment_score）**：公共课程成绩，直接关联学生和课程
- **成绩录入**：教师可录入和修改所授课程的学生成绩
- **智能筛选**：录入成绩时根据课程类型动态筛选学生列表
  - 选课成绩：只显示已选该课程的学生
  - 公共课成绩：显示所有学生
- **成绩查询**
  - 教师：查看所授课程的成绩
  - 学生：查看个人成绩
  - 管理员：查看所有成绩

## 🛠 技术栈

### 后端
- **框架**：Spring Boot 3.2.5
- **Java 版本**：17
- **持久层**：MyBatis 3.0.3
- **数据库**：MySQL 8.0.44
- **安全框架**：Spring Security 6.x
- **身份认证**：JWT (JSON Web Token)
- **密码加密**：BCrypt
- **构建工具**：Maven
- **开发工具**：Lombok

### 前端
- **框架**：Vue 3.5.12
- **构建工具**：Vite 5.4.10
- **UI 组件库**：Element Plus 2.8.4
- **状态管理**：Pinia 2.2.6
- **路由管理**：Vue Router 4.4.5
- **HTTP 客户端**：Axios 1.7.8
- **语言**：TypeScript 5.6.3

## 🏗 系统架构

```
学生管理系统
├── 前端 (fronted/)
│   ├── Vue 3 + TypeScript
│   ├── Element Plus UI
│   └── Pinia 状态管理
├── 后端 (server/)
│   ├── Spring Boot 3.x
│   ├── Spring Security + JWT
│   ├── MyBatis
│   └── RESTful API
└── 数据库 (database/)
    └── MySQL 8.0
```

### 权限架构
- **管理员 (ADMIN)**：拥有系统所有权限
- **教师 (TEACHER)**：管理学生信息、课程和成绩
- **学生 (STUDENT)**：查看课程、选课、查看成绩

## 🚀 快速开始

### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Node.js 16+
- npm 或 yarn

### 1. 克隆项目
```bash
git clone <repository-url>
cd Student_manager
```

### 2. 数据库配置

#### 创建数据库
```sql
CREATE DATABASE student_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
```

#### 导入数据库脚本
```bash
# 导入主数据库脚本
mysql -u root -p student_manager < database/student_manager.sql

# 如果需要修改 enrollment_score 表结构（适配新版本）
mysql -u root -p student_manager < database/alter_enrollment_score.sql
```

#### 配置数据库连接
编辑 `server/src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/student_manager?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

### 3. 启动后端

```bash
cd server
mvn clean install
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

### 4. 启动前端

```bash
cd fronted
npm install
npm run dev
```

前端应用将在 `http://localhost:5173` 启动

### 5. 默认账号

系统预置了以下测试账号：

| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 管理员 | admin | admin123 | 拥有所有权限 |
| 教师 | T001 | teacher123 | 教师账号示例 |
| 学生 | S2025001 | student123 | 学生账号示例 |

> ⚠️ **安全提示**：首次登录后请修改默认密码

## 📊 数据库设计

### 核心数据表

#### 用户与权限
- **sys_user**：系统用户表（username, password, role, related_id, status）
  - role: ADMIN / TEACHER / STUDENT
  - related_id: 关联到 teacher 或 student 表的 ID

#### 组织结构
- **department**：院系表（dept_code, dept_name）
- **major**：专业表（major_code, major_name, department_id）
- **class_info**：班级表（class_code, major, head_teacher, entry_year）

#### 人员信息
- **teacher**：教师表（teacher_no, name, gender, phone, email, department）
- **student**：学生表（student_no, name, age, gender, phone, department, major, class_id）

#### 课程体系
- **course**：公共课程表（course_no, course_name, credit, teacher_id, class_time）
- **course_offering**：开课课程表（course_no, course_name, credit, teacher_id, semester, class_time, classroom, max_students, current_students, status）

#### 选课与成绩
- **student_course**：选课记录表（student_id, course_offering_id, enrollment_date, status）
- **offering_score**：选课成绩表（student_course_id, score）
- **enrollment_score**：公共课成绩表（student_id, course_id, score）

### ER 关系说明

```
department (1) -----> (*) major
major (1) -----> (*) student
class_info (1) -----> (*) student
teacher (1) -----> (*) course
teacher (1) -----> (*) course_offering
student (*) -----> (*) course_offering [通过 student_course]
student_course (1) -----> (1) offering_score
student (*) -----> (*) course [通过 enrollment_score]
```

## 📡 API 文档

### 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/reset-password` - 重置密码（仅管理员）

### 学生管理
- `GET /api/students` - 学生列表（管理员/教师）
- `GET /api/students/{id}` - 学生详情
- `GET /api/students/me` - 当前学生信息
- `POST /api/students` - 创建学生（管理员）
- `PUT /api/students/{id}` - 更新学生（管理员）
- `DELETE /api/students/{id}` - 删除学生（管理员）

### 教师管理
- `GET /api/teachers` - 教师列表（管理员）
- `POST /api/teachers` - 创建教师（管理员）
- `PUT /api/teachers/{id}` - 更新教师（管理员）
- `DELETE /api/teachers/{id}` - 删除教师（管理员）

### 院系专业管理
- `GET /api/departments` - 院系列表
- `POST /api/departments` - 创建院系（管理员）
- `GET /api/majors` - 专业列表
- `POST /api/majors` - 创建专业（管理员）

### 课程管理
- `GET /api/courses` - 公共课程列表
- `POST /api/courses` - 创建公共课程（管理员）
- `GET /api/course-offerings` - 开课课程列表
- `POST /api/course-offerings` - 创建开课课程（管理员/教师）

### 选课管理
- `GET /api/enrollments/my` - 我的选课（学生）
- `POST /api/enrollments/enroll` - 选课（学生）
- `DELETE /api/enrollments/{id}` - 退课（学生）
- `GET /api/enrollments/by-course/{courseId}` - 课程选课记录（管理员/教师）

### 成绩管理
- `POST /api/scores/offering` - 录入选课成绩（管理员/教师）
- `POST /api/scores/enrollment` - 录入公共课成绩（管理员/教师）
- `GET /api/scores/my` - 我的成绩（学生）
- `GET /api/scores/by-course/{courseId}` - 课程成绩列表（管理员/教师）
- `GET /api/scores/students-by-course/{courseId}` - 获取课程学生列表（管理员/教师）
- `GET /api/scores/teacher-courses` - 获取教师课程列表（管理员/教师）

> 详细 API 文档请参考：`server/API.md`

## 👥 用户角色说明

### 管理员 (ADMIN)
**权限**：
- ✅ 管理所有用户（学生、教师）
- ✅ 管理院系、专业、班级
- ✅ 管理所有课程（公共课、开课课程）
- ✅ 查看所有选课记录和成绩
- ✅ 录入所有课程成绩
- ✅ 重置用户密码

**主要功能**：
- 用户管理：新增、编辑、删除学生和教师
- 组织管理：院系、专业、班级的维护
- 课程管理：创建和管理所有课程
- 成绩管理：录入和修改所有成绩
- 数据统计：查看系统概览和统计信息

### 教师 (TEACHER)
**权限**：
- ✅ 查看学生信息
- ✅ 查看所有课程
- ✅ 查看自己教授的课程
- ✅ 录入自己课程的成绩
- ✅ 查看自己课程的选课记录
- ❌ 不能管理用户和组织结构
- ❌ 不能查看选课界面

**主要功能**：
- 学生管理：查看和管理学生信息
- 成绩管理：为自己教授的课程录入和修改成绩
- 课程查询：查看课程信息和选课情况

### 学生 (STUDENT)
**权限**：
- ✅ 查看个人信息
- ✅ 查看所有公共课程
- ✅ 查看自己已选的开课课程
- ✅ 选择开放的开课课程
- ✅ 退选已选课程
- ✅ 查看个人成绩
- ❌ 不能管理其他用户
- ❌ 不能录入成绩

**主要功能**：
- 课程浏览：查看公共课和自己的选课
- 选课操作：选课和退课
- 成绩查询：查看个人各科成绩

## 📁 项目结构

```
Student_manager/
├── database/                          # 数据库相关
│   ├── student_manager.sql           # 主数据库脚本
│   └── alter_enrollment_score.sql    # 表结构更新脚本
├── fronted/                          # 前端项目
│   ├── src/
│   │   ├── api/                      # API 接口封装
│   │   │   ├── http.ts              # Axios 实例配置
│   │   │   └── index.ts             # API 方法定义
│   │   ├── router/                   # 路由配置
│   │   │   └── index.ts             # 路由定义和权限守卫
│   │   ├── stores/                   # Pinia 状态管理
│   │   │   └── auth.ts              # 认证状态管理
│   │   ├── views/                    # 页面组件
│   │   │   ├── Layout.vue           # 布局组件（侧边栏+顶栏）
│   │   │   ├── Login.vue            # 登录页
│   │   │   ├── Dashboard.vue        # 仪表盘
│   │   │   ├── Students.vue         # 学生管理
│   │   │   ├── Teachers.vue         # 教师管理
│   │   │   ├── Classes.vue          # 班级管理
│   │   │   ├── Majors.vue           # 专业管理
│   │   │   ├── Courses.vue          # 课程管理
│   │   │   ├── Enrollments.vue      # 选课管理
│   │   │   └── Scores.vue           # 成绩管理
│   │   ├── App.vue                   # 根组件
│   │   └── main.ts                   # 入口文件
│   ├── package.json                  # 依赖配置
│   └── vite.config.ts               # Vite 配置
└── server/                           # 后端项目
    ├── src/main/
    │   ├── java/com/studentmanager/
    │   │   ├── common/              # 通用类
    │   │   │   ├── ApiResponse.java # 统一响应格式
    │   │   │   └── PageData.java    # 分页数据封装
    │   │   ├── config/              # 配置类
    │   │   │   ├── CorsConfig.java  # 跨域配置
    │   │   │   └── SecurityConfig.java # Spring Security 配置
    │   │   ├── controller/          # 控制器层
    │   │   │   ├── AuthController.java           # 认证接口
    │   │   │   ├── StudentController.java        # 学生接口
    │   │   │   ├── TeacherController.java        # 教师接口
    │   │   │   ├── DepartmentController.java     # 院系接口
    │   │   │   ├── MajorController.java          # 专业接口
    │   │   │   ├── ClassController.java          # 班级接口
    │   │   │   ├── CourseController.java         # 公共课程接口
    │   │   │   ├── CourseOfferingController.java # 开课课程接口
    │   │   │   ├── EnrollmentController.java     # 选课接口
    │   │   │   └── ScoreController.java          # 成绩接口
    │   │   ├── dto/                 # 数据传输对象
    │   │   │   ├── LoginRequest.java
    │   │   │   ├── LoginResponse.java
    │   │   │   ├── ScoreRequest.java
    │   │   │   └── ScoreResponse.java
    │   │   ├── entity/              # 实体类
    │   │   │   ├── SysUser.java     # 系统用户
    │   │   │   ├── Student.java     # 学生
    │   │   │   ├── Teacher.java     # 教师
    │   │   │   ├── Department.java  # 院系
    │   │   │   ├── Major.java       # 专业
    │   │   │   ├── ClassInfo.java   # 班级
    │   │   │   ├── Course.java      # 公共课程
    │   │   │   ├── CourseOffering.java    # 开课课程
    │   │   │   ├── StudentCourse.java     # 选课记录
    │   │   │   ├── OfferingScore.java     # 选课成绩
    │   │   │   └── EnrollmentScore.java   # 公共课成绩
    │   │   ├── exception/           # 异常处理
    │   │   │   ├── BusinessException.java # 业务异常
    │   │   │   └── GlobalExceptionHandler.java # 全局异常处理器
    │   │   ├── mapper/              # MyBatis Mapper 接口
    │   │   │   ├── SysUserMapper.java
    │   │   │   ├── StudentMapper.java
    │   │   │   ├── TeacherMapper.java
    │   │   │   ├── DepartmentMapper.java
    │   │   │   ├── MajorMapper.java
    │   │   │   ├── ClassInfoMapper.java
    │   │   │   ├── CourseMapper.java
    │   │   │   ├── CourseOfferingMapper.java
    │   │   │   ├── StudentCourseMapper.java
    │   │   │   ├── OfferingScoreMapper.java
    │   │   │   └── EnrollmentScoreMapper.java
    │   │   ├── security/            # 安全相关
    │   │   │   ├── JwtUtil.java     # JWT 工具类
    │   │   │   ├── JwtAuthenticationFilter.java # JWT 过滤器
    │   │   │   ├── CustomUserDetailsService.java # 用户详情服务
    │   │   │   └── SecurityUtil.java # 安全工具类
    │   │   ├── service/             # 服务接口
    │   │   │   ├── AuthService.java
    │   │   │   ├── StudentService.java
    │   │   │   ├── TeacherService.java
    │   │   │   ├── DepartmentService.java
    │   │   │   ├── MajorService.java
    │   │   │   ├── ClassService.java
    │   │   │   ├── CourseService.java
    │   │   │   ├── CourseOfferingService.java
    │   │   │   ├── EnrollmentService.java
    │   │   │   └── ScoreService.java
    │   │   └── service/impl/        # 服务实现
    │   │       ├── AuthServiceImpl.java
    │   │       ├── StudentServiceImpl.java
    │   │       ├── TeacherServiceImpl.java
    │   │       ├── DepartmentServiceImpl.java
    │   │       ├── MajorServiceImpl.java
    │   │       ├── ClassServiceImpl.java
    │   │       ├── CourseServiceImpl.java
    │   │       ├── CourseOfferingServiceImpl.java
    │   │       ├── EnrollmentServiceImpl.java
    │   │       └── ScoreServiceImpl.java
    │   └── resources/
    │       └── application.yml      # 应用配置
    ├── pom.xml                      # Maven 配置
    └── API.md                       # API 文档
```

## 🔐 安全性

### 身份认证
- 使用 JWT Token 进行身份验证
- Token 有效期：24小时
- 密码使用 BCrypt 加密存储

### 权限控制
- 基于角色的访问控制（RBAC）
- 方法级权限注解：`@PreAuthorize`
- 前端路由守卫：动态权限验证

### 安全配置
- CORS 跨域配置
- CSRF 保护（API 模式下禁用）
- 无状态会话管理

## 📈 特色功能

### 1. 双成绩体系
系统创新性地实现了双成绩管理体系：

**选课成绩（Offering Score）**
- 基于选课记录的成绩管理
- 学生必须先选课才能有成绩
- 适用于选修课、专业课等

**公共课成绩（Enrollment Score）**
- 直接关联学生和课程
- 无需选课，所有学生默认可见
- 适用于必修课、公共基础课等

### 2. 智能学生筛选
在录入成绩时，系统会根据课程类型智能筛选学生列表：
- **选课成绩**：仅显示已选该课程的学生
- **公共课成绩**：显示所有学生

### 3. 教师课程隔离
- 教师只能查看和管理自己教授的课程
- 成绩录入时自动过滤课程列表
- 保证数据访问的安全性

### 4. 灵活的课程分类
- **公共课程**：所有学生可见，无需选课
- **开课课程**：学生主动选择，有人数限制

## 🐛 常见问题

### 1. 数据库连接失败
- 检查 MySQL 服务是否启动
- 确认数据库连接配置正确
- 验证数据库用户权限

### 2. 前端跨域问题
- 后端已配置 CORS，允许 `http://localhost:5173`
- 如需修改前端端口，请同步更新后端 CorsConfig

### 3. JWT Token 过期
- Token 默认有效期 24 小时
- 过期后需重新登录
- 可在 `application.yml` 中调整有效期

### 4. 成绩录入失败
- 确保已选择正确的成绩类型
- 检查是否有对应的选课记录（选课成绩）
- 验证学生和课程是否存在

## 📝 更新日志

### v1.0.0 (2025-12-14)
- ✨ 初始版本发布
- ✅ 完整的用户权限管理
- ✅ 学生、教师、课程管理
- ✅ 选课系统
- ✅ 双成绩体系
- ✅ 院系专业管理
- ✅ 响应式 UI 设计

## 👨‍💻 开发团队

本项目作为学生管理系统的完整解决方案，适用于学校、培训机构等教育场景。

### 作者
程序员Eighteen

## 📄 许可证

本项目仅供学习和研究使用。

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

## 📮 联系方式

如有问题或建议，请通过以下方式联系：
- 提交 GitHub Issue
- 发送邮件至项目维护者
- QQ邮箱：3273495516@qq.com
- Gmail：eighteenthstuai@gmail.com

---

**Happy Coding! 🎉**
