package com.studentmanager.controller;

import com.studentmanager.common.ApiResponse;
import com.studentmanager.entity.Major;
import com.studentmanager.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/majors")
@RequiredArgsConstructor
public class MajorController {

    private final MajorService majorService;

    @GetMapping
    public ApiResponse<List<Major>> list(@RequestParam(value = "departmentId", required = false) Long departmentId) {
        if (departmentId != null) {
            return ApiResponse.success(majorService.listByDepartment(departmentId));
        }
        return ApiResponse.success(majorService.listAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Major> getById(@PathVariable Long id) {
        return ApiResponse.success(majorService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Major> create(@RequestBody Major major) {
        return ApiResponse.success(majorService.create(major));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Major> update(@PathVariable Long id, @RequestBody Major major) {
        return ApiResponse.success(majorService.update(id, major));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        majorService.delete(id);
        return ApiResponse.success(null);
    }
}
