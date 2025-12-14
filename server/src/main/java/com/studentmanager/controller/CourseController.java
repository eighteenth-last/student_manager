package com.studentmanager.controller;

import com.studentmanager.common.ApiResponse;
import com.studentmanager.dto.CourseRequest;
import com.studentmanager.entity.Course;
import com.studentmanager.entity.SysUser;
import com.studentmanager.exception.BusinessException;
import com.studentmanager.mapper.SysUserMapper;
import com.studentmanager.security.SecurityUtil;
import com.studentmanager.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final SysUserMapper sysUserMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public ApiResponse<List<Course>> list(@RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "size", defaultValue = "20") int size) {
        return ApiResponse.success(courseService.list(page, size));
    }

    @GetMapping("/my-teaching")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<List<Course>> myTeaching() {
        Long teacherId = currentTeacherId();
        return ApiResponse.success(courseService.listByTeacher(teacherId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public ApiResponse<Course> get(@PathVariable("id") Long id) {
        return ApiResponse.success(courseService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Course> create(@RequestBody @Valid CourseRequest request) {
        return ApiResponse.success(courseService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Course> update(@PathVariable("id") Long id, @RequestBody @Valid CourseRequest request) {
        return ApiResponse.success(courseService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        courseService.delete(id);
        return ApiResponse.success(null);
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
