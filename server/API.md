# 学生管理系统后端 API 文档

> 统一返回结构：`{ code: number, message: string, data: any }`
> 成功 code=0；常见业务错误 code=-1。无数据时 data 为空对象/数组。
> 所有需要鉴权的请求需在 Header 携带：`Authorization: Bearer <token>`。

## 认证
- `POST /api/auth/login`
  - body: `{ "username": "string", "password": "string" }`
  - resp.data: `{ token, role }`
- `POST /api/auth/reset-password` （ADMIN）
  - body: `{ "username": "string", "newPassword": "string" }`

## 学生 Student
- `GET /api/students?page=1&size=20` （ADMIN/TEACHER）列表
- `GET /api/students/{id}` （ADMIN/TEACHER）详情
- `GET /api/students/me` （STUDENT）本人详情
- `POST /api/students` （ADMIN）创建；body: `{ studentNo, name, age?, gender?, phone?, department?, major?, classId }`
- `PUT /api/students/{id}` （ADMIN）更新；body 同上
- `DELETE /api/students/{id}` （ADMIN）删除
> 创建学生会自动创建登录账号：username=studentNo，初始密码`123456`，role=STUDENT。

## 教师 Teacher
- `GET /api/teachers?page=1&size=20` （ADMIN）列表
- `GET /api/teachers/{id}` （ADMIN/TEACHER）详情
- `GET /api/teachers/me` （TEACHER）本人详情
- `POST /api/teachers` （ADMIN）创建；body: `{ teacherNo, name, phone?, department? }`
- `PUT /api/teachers/{id}` （ADMIN）更新；body 同上
- `DELETE /api/teachers/{id}` （ADMIN）删除
> 创建教师会自动创建登录账号：username=teacherNo，初始密码`123456`，role=TEACHER。

## 班级 ClassInfo
- `GET /api/classes?page=1&size=20` （ADMIN）列表
- `GET /api/classes/{id}` （ADMIN）详情
- `POST /api/classes` （ADMIN）创建；body: `{ classCode, major?, headTeacher?, entryYear }`
- `PUT /api/classes/{id}` （ADMIN）更新；body 同上
- `DELETE /api/classes/{id}` （ADMIN）删除

## 课程 Course
- `GET /api/courses?page=1&size=20` （ADMIN/TEACHER/STUDENT）列表
- `GET /api/courses/{id}` （ADMIN/TEACHER/STUDENT）详情
- `GET /api/courses/my-teaching` （TEACHER）我授课的课程
- `POST /api/courses` （ADMIN）创建；body: `{ courseNo, courseName, credit, teacherId, classTime? }`
- `PUT /api/courses/{id}` （ADMIN）更新；body 同上
- `DELETE /api/courses/{id}` （ADMIN）删除

## 选课 Enrollment
- `POST /api/enrollments` （STUDENT）选课；body: `{ courseId }`
- `GET /api/enrollments/my` （STUDENT）我的选课
- `DELETE /api/enrollments/{id}` （STUDENT）退选（仅本人记录）
- `GET /api/enrollments/by-course/{courseId}` （ADMIN/TEACHER）按课程查看选课；教师仅可查看自己授课的课程

## 成绩 Score
- `POST /api/scores` （TEACHER/ADMIN）录入或修改成绩；body: `{ studentId, courseId, score }`
  - 需存在选课记录；教师仅可操作自己授课课程。
- `GET /api/scores/my` （STUDENT）我的成绩
- `GET /api/scores/by-course/{courseId}` （TEACHER/ADMIN）按课程查看成绩；教师仅可查看自己授课课程

## 角色与权限速览
- STUDENT：登录、查看/编辑自身选课与成绩；查询课程列表。
- TEACHER：查询学生列表与学生详情、查询/维护自己授课课程的选课与成绩。
- ADMIN：全量 CRUD；重置密码。

## 数据模型简述
- 用户：`sys_user`（username/password/role/related_id/status）
- 学生：`student`；教师：`teacher`；班级：`class_info`；课程：`course`
- 选课：`student_course`（student_id, course_id, uniq）
- 成绩：`score`（student_course_id, score）

## 配置
- `application.yml` 使用 MySQL 连接 `jdbc:mysql://localhost:3306/student_manager`（请按需修改账号密码和 jwt.secret）。
- MyBatis 已启用驼峰映射 `map-underscore-to-camel-case: true`。
