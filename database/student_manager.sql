/*
 Navicat Premium Dump SQL

 Source Server         : WSL_MySQL
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44-0ubuntu0.24.04.1)
 Source Host           : 172.31.142.67:3306
 Source Schema         : student_manager

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44-0ubuntu0.24.04.1)
 File Encoding         : 65001

 Date: 14/12/2025 17:33:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for class_info
-- ----------------------------
DROP TABLE IF EXISTS `class_info`;
CREATE TABLE `class_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `class_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班级编号',
  `major` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `head_teacher` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '班主任',
  `entry_year` int NULL DEFAULT NULL COMMENT '入学年份',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `class_code`(`class_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of class_info
-- ----------------------------
INSERT INTO `class_info` VALUES (1, 'CS2025-1', '计算机科学与技术', '张老师', 2025, '2025-12-14 14:25:15');
INSERT INTO `class_info` VALUES (2, 'SE2025-1', '软件工程', '李老师', 2025, '2025-12-14 14:25:15');
INSERT INTO `class_info` VALUES (3, 'AI2024-1', '人工智能', '王老师', 2024, '2025-12-14 14:25:15');
INSERT INTO `class_info` VALUES (4, 'cs123', '123', '123', 2026, '2025-12-14 14:47:32');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程编号',
  `course_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程名称',
  `credit` decimal(3, 1) NULL DEFAULT NULL COMMENT '学分',
  `teacher_id` bigint NULL DEFAULT NULL COMMENT '授课教师ID',
  `class_time` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上课时间',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `course_no`(`course_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 303 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公共课程表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (1, 'KC101', '数据结构', 4.0, 1, '周一 08:00-10:00', '2025-12-14 14:25:15');
INSERT INTO `course` VALUES (2, 'KC102', '操作系统', 3.5, 1, '周三 14:00-16:00', '2025-12-14 14:25:15');
INSERT INTO `course` VALUES (3, 'KC201', '软件工程导论', 3.0, 2, '周二 10:00-12:00', '2025-12-14 14:25:15');
INSERT INTO `course` VALUES (4, 'KC301', '机器学习', 4.0, 3, '周四 08:00-10:00', '2025-12-14 14:25:15');
INSERT INTO `course` VALUES (5, 'KC103', '计算机网络', 3.0, 1, '周五 14:00-16:00', '2025-12-14 14:25:15');

-- ----------------------------
-- Table structure for course_offering
-- ----------------------------
DROP TABLE IF EXISTS `course_offering`;
CREATE TABLE `course_offering`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程编号',
  `course_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程名称',
  `credit` decimal(3, 1) NULL DEFAULT NULL COMMENT '学分',
  `semester` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '开课学期（如2025-1）',
  `teacher_id` bigint NOT NULL COMMENT '授课教师ID',
  `class_time` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上课时间',
  `classroom` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上课教室',
  `max_students` int NULL DEFAULT 100 COMMENT '最大选课人数',
  `current_students` int NULL DEFAULT 0 COMMENT '当前选课人数',
  `status` enum('OPEN','CLOSED','FINISHED') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'OPEN' COMMENT '状态：开放选课、关闭选课、已结课',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_semester`(`semester` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '开课课程表（独立的课程表，与course表无关）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of course_offering
-- ----------------------------
INSERT INTO `course_offering` VALUES (1, 'XK101', '选课1', 4.0, '2025-1', 1, '周一 08:00-10:00', '教学楼A101', 50, 3, 'OPEN', '2025-12-14 14:25:15', '2025-12-14 15:58:34');
INSERT INTO `course_offering` VALUES (2, 'XK102', '选课2', 3.5, '2025-1', 1, '周三 14:00-16:00', '教学楼A102', 50, 2, 'OPEN', '2025-12-14 14:25:15', '2025-12-14 15:58:26');
INSERT INTO `course_offering` VALUES (3, 'XK201', '选课3', 3.0, '2025-1', 2, '周二 10:00-12:00', '教学楼B201', 40, 2, 'OPEN', '2025-12-14 14:25:15', '2025-12-14 15:58:40');
INSERT INTO `course_offering` VALUES (4, 'XK301', '选课4', 4.0, '2024-2', 3, '周四 08:00-10:00', '教学楼C301', 40, 2, 'FINISHED', '2025-12-14 14:25:15', '2025-12-14 15:58:44');
INSERT INTO `course_offering` VALUES (5, 'XK103', '选课5', 3.0, '2025-1', 1, '周五 14:00-16:00', '教学楼A103', 50, 3, 'OPEN', '2025-12-14 14:25:15', '2025-12-14 15:58:48');
INSERT INTO `course_offering` VALUES (6, 'XK101', '选课6', 4.0, '2024-2', 1, '周一 10:00-12:00', '教学楼A104', 50, 1, 'FINISHED', '2025-12-14 14:25:15', '2025-12-14 15:58:54');

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dept_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '院系编号',
  `dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '院系名称',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `dept_code`(`dept_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '院系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES (1, 'CS', '计算机学院', '2025-12-14 16:00:00');
INSERT INTO `department` VALUES (2, 'SE', '软件学院', '2025-12-14 16:00:00');
INSERT INTO `department` VALUES (3, 'AI', '人工智能学院', '2025-12-14 16:00:00');

-- ----------------------------
-- Table structure for enrollment_score
-- ----------------------------
DROP TABLE IF EXISTS `enrollment_score`;
CREATE TABLE `enrollment_score`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `course_id` bigint NOT NULL COMMENT '公共课程ID',
  `score` decimal(5, 2) NULL DEFAULT NULL COMMENT '成绩',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_student_course`(`student_id` ASC, `course_id` ASC) USING BTREE,
  INDEX `idx_student`(`student_id` ASC) USING BTREE,
  INDEX `idx_course`(`course_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公共课成绩表(所有学生默认可见)' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of enrollment_score
-- ----------------------------
INSERT INTO `enrollment_score` VALUES (1, 3, 2, 20.00, '2025-12-14 16:55:51');

-- ----------------------------
-- Table structure for major
-- ----------------------------
DROP TABLE IF EXISTS `major`;
CREATE TABLE `major`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `major_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '专业编号',
  `major_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '专业名称',
  `department_id` bigint NOT NULL COMMENT '所属院系ID',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `major_code`(`major_code` ASC) USING BTREE,
  INDEX `idx_department`(`department_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '专业表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of major
-- ----------------------------
INSERT INTO `major` VALUES (1, 'CS01', '计算机科学与技术', 1, '2025-12-14 16:00:00');
INSERT INTO `major` VALUES (2, 'CS02', '网络工程', 1, '2025-12-14 16:00:00');
INSERT INTO `major` VALUES (3, 'SE01', '软件工程', 2, '2025-12-14 16:00:00');
INSERT INTO `major` VALUES (4, 'AI01', '人工智能', 3, '2025-12-14 16:00:00');

-- ----------------------------
-- Table structure for offering_score
-- ----------------------------
DROP TABLE IF EXISTS `offering_score`;
CREATE TABLE `offering_score`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_course_id` bigint NOT NULL COMMENT '学生选课记录ID（关联student_course表）',
  `score` decimal(5, 2) NULL DEFAULT NULL COMMENT '成绩',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_student_course`(`student_course_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '选课成绩表（开课课程成绩）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of offering_score
-- ----------------------------
INSERT INTO `offering_score` VALUES (1, 1, 22.00, '2025-12-14 15:48:32');
INSERT INTO `offering_score` VALUES (2, 2, 92.00, '2025-12-14 14:25:15');
INSERT INTO `offering_score` VALUES (3, 3, 85.00, '2025-12-14 14:25:15');
INSERT INTO `offering_score` VALUES (4, 4, 91.50, '2025-12-14 14:25:15');
INSERT INTO `offering_score` VALUES (5, 5, 78.00, '2025-12-14 14:25:15');
INSERT INTO `offering_score` VALUES (6, 6, 90.00, '2025-12-14 14:25:15');
INSERT INTO `offering_score` VALUES (7, 7, 87.50, '2025-12-14 14:25:15');
INSERT INTO `offering_score` VALUES (8, 8, 94.00, '2025-12-14 14:25:15');
INSERT INTO `offering_score` VALUES (9, 9, 96.50, '2025-12-14 14:25:15');
INSERT INTO `offering_score` VALUES (10, 10, 82.00, '2025-12-14 14:25:15');
INSERT INTO `offering_score` VALUES (11, 12, 100.00, '2025-12-14 16:40:00');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `age` int NULL DEFAULT NULL,
  `gender` enum('男','女','未知') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '未知',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `department` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '院系',
  `major` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专业',
  `class_id` bigint NULL DEFAULT NULL COMMENT '班级ID',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `student_no`(`student_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, 'S2025001', '张三', 20, '男', '13900000001', '计算机学院', '计算机科学与技术', 1, '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `student` VALUES (2, 'S2025002', '李四', 19, '女', '13900000002', '软件学院', '软件工程', 2, '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `student` VALUES (3, 'S2025003', '王五', 21, '男', '13900000003', '计算机学院', '计算机科学与技术', 1, '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `student` VALUES (4, 'S2025004', '赵六', 20, '女', '13900000004', '软件学院', '软件工程', 2, '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `student` VALUES (5, 'S2024001', '刘七', 21, '男', '13900000005', '人工智能学院', '人工智能', 3, '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `student` VALUES (6, 'S2024002', '陈八', 22, '女', '13900000006', '人工智能学院', '人工智能', 3, '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `student` VALUES (7, '2022900116', '测试', 11, '女', '111', '计算机学院', '网络工程', 4, '2025-12-14 14:47:16', '2025-12-14 16:37:16');

-- ----------------------------
-- Table structure for student_course
-- ----------------------------
DROP TABLE IF EXISTS `student_course`;
CREATE TABLE `student_course`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `course_offering_id` bigint NOT NULL COMMENT '开课ID（关联course_offering开课表）',
  `status` enum('ENROLLED','DROPPED','COMPLETED') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'ENROLLED' COMMENT '选课状态：已选课、已退课、已完成',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_student_offering`(`student_id` ASC, `course_offering_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '选课记录表（学生从course_offering选课，数据来自开课表）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of student_course
-- ----------------------------
INSERT INTO `student_course` VALUES (1, 1, 1, 'ENROLLED', '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `student_course` VALUES (2, 1, 2, 'ENROLLED', '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `student_course` VALUES (3, 1, 5, 'ENROLLED', '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `student_course` VALUES (4, 2, 3, 'ENROLLED', '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `student_course` VALUES (5, 2, 1, 'ENROLLED', '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `student_course` VALUES (6, 3, 1, 'ENROLLED', '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `student_course` VALUES (7, 3, 2, 'ENROLLED', '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `student_course` VALUES (8, 4, 3, 'ENROLLED', '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `student_course` VALUES (9, 5, 4, 'COMPLETED', '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `student_course` VALUES (10, 5, 6, 'COMPLETED', '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `student_course` VALUES (11, 6, 4, 'COMPLETED', '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `student_course` VALUES (12, 7, 5, 'ENROLLED', '2025-12-14 15:42:51', '2025-12-14 15:42:51');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录账号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '加密密码',
  `role` enum('STUDENT','TEACHER','ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色',
  `related_id` bigint NULL DEFAULT NULL COMMENT '关联业务ID（学生ID或教师ID）',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：1正常 0禁用',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$ZmQ3qvu6H8Bs49Ev/EuPauAi17oGx0ls55coYxPyeTAC3AnVzhqke', 'ADMIN', NULL, 1, '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `sys_user` VALUES (2, 'T001', '$2a$10$ZmQ3qvu6H8Bs49Ev/EuPauAi17oGx0ls55coYxPyeTAC3AnVzhqke', 'TEACHER', 1, 1, '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `sys_user` VALUES (3, 'T002', '$2a$10$ZmQ3qvu6H8Bs49Ev/EuPauAi17oGx0ls55coYxPyeTAC3AnVzhqke', 'TEACHER', 2, 1, '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `sys_user` VALUES (4, 'T003', '$2a$10$ZmQ3qvu6H8Bs49Ev/EuPauAi17oGx0ls55coYxPyeTAC3AnVzhqke', 'TEACHER', 3, 1, '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `sys_user` VALUES (5, 'S2025001', '$2a$10$ZmQ3qvu6H8Bs49Ev/EuPauAi17oGx0ls55coYxPyeTAC3AnVzhqke', 'STUDENT', 1, 1, '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `sys_user` VALUES (6, 'S2025002', '$2a$10$ZmQ3qvu6H8Bs49Ev/EuPauAi17oGx0ls55coYxPyeTAC3AnVzhqke', 'STUDENT', 2, 1, '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `sys_user` VALUES (7, 'S2025003', '$2a$10$ZmQ3qvu6H8Bs49Ev/EuPauAi17oGx0ls55coYxPyeTAC3AnVzhqke', 'STUDENT', 3, 1, '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `sys_user` VALUES (8, 'S2025004', '$2a$10$ZmQ3qvu6H8Bs49Ev/EuPauAi17oGx0ls55coYxPyeTAC3AnVzhqke', 'STUDENT', 4, 1, '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `sys_user` VALUES (9, 'S2024001', '$2a$10$ZmQ3qvu6H8Bs49Ev/EuPauAi17oGx0ls55coYxPyeTAC3AnVzhqke', 'STUDENT', 5, 1, '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `sys_user` VALUES (10, 'S2024002', '$2a$10$ZmQ3qvu6H8Bs49Ev/EuPauAi17oGx0ls55coYxPyeTAC3AnVzhqke', 'STUDENT', 6, 1, '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `sys_user` VALUES (11, '2022900116', '$2a$10$RqKPzNBf7KSqrQwSAmAdvOE4mXP1uj755nDbmHq8Eotep2wEX5xoS', 'STUDENT', 7, 1, '2025-12-14 14:47:16', '2025-12-14 14:47:16');

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `teacher_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '教师工号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `department` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `teacher_no`(`teacher_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (1, 'T001', '赵明', '13800001001', '计算机学院', '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `teacher` VALUES (2, 'T002', '钱强', '13800001002', '软件学院', '2025-12-14 14:25:15', '2025-12-14 14:25:15');
INSERT INTO `teacher` VALUES (3, 'T003', '孙华', '13800001003', '人工智能学院', '2025-12-14 14:25:15', '2025-12-14 14:25:15');

SET FOREIGN_KEY_CHECKS = 1;
