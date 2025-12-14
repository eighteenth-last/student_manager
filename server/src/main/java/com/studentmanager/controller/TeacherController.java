package com.studentmanager.controller;

import com.studentmanager.common.ApiResponse;
import com.studentmanager.dto.TeacherRequest;
import com.studentmanager.entity.SysUser;
import com.studentmanager.entity.Teacher;
import com.studentmanager.exception.BusinessException;
import com.studentmanager.mapper.SysUserMapper;
import com.studentmanager.security.SecurityUtil;
import com.studentmanager.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;
    private final SysUserMapper sysUserMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<Teacher>> list(@RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "size", defaultValue = "20") int size) {
        return ApiResponse.success(teacherService.list(page, size));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<Teacher> me() {
        Long teacherId = currentTeacherId();
        return ApiResponse.success(teacherService.getById(teacherId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ApiResponse<Teacher> get(@PathVariable("id") Long id) {
        return ApiResponse.success(teacherService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Teacher> create(@RequestBody @Valid TeacherRequest request) {
        return ApiResponse.success(teacherService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Teacher> update(@PathVariable("id") Long id, @RequestBody @Valid TeacherRequest request) {
        return ApiResponse.success(teacherService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        teacherService.delete(id);
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
