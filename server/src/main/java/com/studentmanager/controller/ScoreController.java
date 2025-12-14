package com.studentmanager.controller;

import com.studentmanager.common.ApiResponse;
import com.studentmanager.dto.ScoreRequest;
import com.studentmanager.dto.ScoreResponse;
import com.studentmanager.entity.SysUser;
import com.studentmanager.exception.BusinessException;
import com.studentmanager.mapper.SysUserMapper;
import com.studentmanager.security.SecurityUtil;
import com.studentmanager.service.ScoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;
    private final SysUserMapper sysUserMapper;

    @PostMapping("/offering")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ApiResponse<ScoreResponse> recordOfferingScore(@RequestBody @Valid ScoreRequest request) {
        return ApiResponse.success(scoreService.recordOfferingScore(request));
    }

    @PostMapping("/enrollment")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ApiResponse<ScoreResponse> recordEnrollmentScore(@RequestBody @Valid ScoreRequest request) {
        return ApiResponse.success(scoreService.recordEnrollmentScore(request));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<List<ScoreResponse>> myScores() {
        Long studentId = currentStudentId();
        return ApiResponse.success(scoreService.listByStudent(studentId));
    }

    @GetMapping("/by-course/{courseId}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ApiResponse<List<ScoreResponse>> byCourse(
            @PathVariable("courseId") Long courseId,
            @RequestParam(value = "type", defaultValue = "OFFERING") String type) {
        if (SecurityUtil.currentRole().equals("ADMIN") && courseId == 0) {
            return ApiResponse.success(scoreService.listAll(type));
        }
        return ApiResponse.success(scoreService.listByCourse(courseId, type));
    }

    @GetMapping("/students-by-course/{courseId}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ApiResponse<List<Object>> getStudentsByCourse(
            @PathVariable("courseId") Long courseId,
            @RequestParam(value = "type", defaultValue = "OFFERING") String type) {
        return ApiResponse.success(scoreService.getStudentsByCourse(courseId, type));
    }

    @GetMapping("/teacher-courses")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ApiResponse<Object> getTeacherCourses(@RequestParam(value = "type", defaultValue = "OFFERING") String type) {
        Long teacherId = SecurityUtil.currentRole().equals("ADMIN") ? null : currentTeacherId();
        return ApiResponse.success(scoreService.getTeacherCourses(teacherId, type));
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
