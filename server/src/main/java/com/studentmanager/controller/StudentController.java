package com.studentmanager.controller;

import com.studentmanager.common.ApiResponse;
import com.studentmanager.dto.StudentRequest;
import com.studentmanager.entity.Student;
import com.studentmanager.entity.SysUser;
import com.studentmanager.exception.BusinessException;
import com.studentmanager.mapper.SysUserMapper;
import com.studentmanager.security.SecurityUtil;
import com.studentmanager.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final SysUserMapper sysUserMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ApiResponse<List<Student>> list(@RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "size", defaultValue = "20") int size) {
        return ApiResponse.success(studentService.list(page, size));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<Student> me() {
        Long studentId = currentStudentId();
        return ApiResponse.success(studentService.getById(studentId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ApiResponse<Student> get(@PathVariable("id") Long id) {
        return ApiResponse.success(studentService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Student> create(@RequestBody @Valid StudentRequest request) {
        return ApiResponse.success(studentService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Student> update(@PathVariable("id") Long id, @RequestBody @Valid StudentRequest request) {
        return ApiResponse.success(studentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        studentService.delete(id);
        return ApiResponse.success(null);
    }

    private Long currentStudentId() {
        String username = SecurityUtil.currentUsername();
        SysUser user = sysUserMapper.findByUsername(username);
        if (user == null || user.getRelatedId() == null) {
            throw new BusinessException("当前学生账号缺少绑定信息");
        }
        return user.getRelatedId();
    }
}
