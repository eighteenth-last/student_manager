package com.studentmanager.controller;

import com.studentmanager.common.ApiResponse;
import com.studentmanager.entity.Department;
import com.studentmanager.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public ApiResponse<List<Department>> list() {
        return ApiResponse.success(departmentService.listAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Department> getById(@PathVariable Long id) {
        return ApiResponse.success(departmentService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Department> create(@RequestBody Department department) {
        return ApiResponse.success(departmentService.create(department));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Department> update(@PathVariable Long id, @RequestBody Department department) {
        return ApiResponse.success(departmentService.update(id, department));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ApiResponse.success(null);
    }
}
