package com.studentmanager.controller;

import com.studentmanager.common.ApiResponse;
import com.studentmanager.entity.CourseOffering;
import com.studentmanager.entity.SysUser;
import com.studentmanager.exception.BusinessException;
import com.studentmanager.mapper.SysUserMapper;
import com.studentmanager.security.SecurityUtil;
import com.studentmanager.service.CourseOfferingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-offerings")
@RequiredArgsConstructor
public class CourseOfferingController {

    private final CourseOfferingService courseOfferingService;
    private final SysUserMapper sysUserMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public ApiResponse<List<CourseOffering>> list(@RequestParam(value = "page", defaultValue = "1") int page,
                                                   @RequestParam(value = "size", defaultValue = "20") int size) {
        return ApiResponse.success(courseOfferingService.list(page, size));
    }

    @GetMapping("/semester/{semester}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public ApiResponse<List<CourseOffering>> listBySemester(@PathVariable("semester") String semester) {
        return ApiResponse.success(courseOfferingService.listBySemester(semester));
    }

    @GetMapping("/open")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public ApiResponse<List<CourseOffering>> listOpenOfferings(@RequestParam(value = "semester", required = false, defaultValue = "2025-1") String semester) {
        return ApiResponse.success(courseOfferingService.listOpenOfferings(semester));
    }

    @GetMapping("/my-teaching")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<List<CourseOffering>> myTeaching() {
        Long teacherId = currentTeacherId();
        return ApiResponse.success(courseOfferingService.listByTeacherId(teacherId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ApiResponse<CourseOffering> get(@PathVariable("id") Long id) {
        return ApiResponse.success(courseOfferingService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CourseOffering> create(@RequestBody @Valid CourseOffering offering) {
        return ApiResponse.success(courseOfferingService.create(offering));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CourseOffering> update(@PathVariable("id") Long id, @RequestBody @Valid CourseOffering offering) {
        return ApiResponse.success(courseOfferingService.update(id, offering));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        courseOfferingService.delete(id);
        return ApiResponse.success(null);
    }

    private Long currentTeacherId() {
        String username = SecurityUtil.currentUsername();
        SysUser user = sysUserMapper.findByUsername(username);
        if (user == null || user.getRelatedId() == null) {
            throw new BusinessException("教师信息不存在");
        }
        return user.getRelatedId();
    }
}
