package com.studentmanager.controller;

import com.studentmanager.common.ApiResponse;
import com.studentmanager.dto.EnrollmentRequest;
import com.studentmanager.entity.CourseOffering;
import com.studentmanager.entity.StudentCourse;
import com.studentmanager.entity.SysUser;
import com.studentmanager.exception.BusinessException;
import com.studentmanager.mapper.SysUserMapper;
import com.studentmanager.security.SecurityUtil;
import com.studentmanager.service.CourseOfferingService;
import com.studentmanager.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final CourseOfferingService courseOfferingService;
    private final SysUserMapper sysUserMapper;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<StudentCourse> enroll(@RequestBody @Valid EnrollmentRequest request) {
        Long studentId = currentStudentId();
        return ApiResponse.success(enrollmentService.enroll(studentId, request.getCourseId()));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<List<StudentCourse>> myEnrollments() {
        Long studentId = currentStudentId();
        return ApiResponse.success(enrollmentService.listByStudent(studentId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<Void> drop(@PathVariable("id") Long id) {
        // 仅允许本人的选课记录删除
        Long studentId = currentStudentId();
        List<StudentCourse> my = enrollmentService.listByStudent(studentId);
        boolean owned = my.stream().anyMatch(sc -> sc.getId().equals(id));
        if (!owned) {
            throw new BusinessException("无权操作该选课记录");
        }
        enrollmentService.drop(id);
        return ApiResponse.success(null);
    }

    @GetMapping("/by-course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ApiResponse<List<StudentCourse>> byCourse(@PathVariable("courseId") Long courseId) {
        // courseId=0表示查询所有选课记录（仅管理员）
        if (courseId == 0) {
            return ApiResponse.success(enrollmentService.listAll());
        }
        return ApiResponse.success(enrollmentService.listByCourse(courseId));
    }

    private Long currentStudentId() {
        String username = SecurityUtil.currentUsername();
        SysUser user = sysUserMapper.findByUsername(username);
        if (user == null || user.getRelatedId() == null) {
            throw new BusinessException("当前学生账号缺少绑定信息");
        }
        return user.getRelatedId();
    }

    private Long currentTeacherId() {
        String username = SecurityUtil.currentUsername();
        SysUser user = sysUserMapper.findByUsername(username);
        if (user == null || user.getRelatedId() == null) {
            throw new BusinessException("当前教师账号缺少绑定信息");
        }
        return user.getRelatedId();
    }
}
