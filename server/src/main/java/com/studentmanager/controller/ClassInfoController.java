package com.studentmanager.controller;

import com.studentmanager.common.ApiResponse;
import com.studentmanager.dto.ClassInfoRequest;
import com.studentmanager.entity.ClassInfo;
import com.studentmanager.service.ClassInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassInfoController {

    private final ClassInfoService classInfoService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<ClassInfo>> list(@RequestParam(value = "page", defaultValue = "1") int page,
                                             @RequestParam(value = "size", defaultValue = "20") int size) {
        return ApiResponse.success(classInfoService.list(page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ClassInfo> get(@PathVariable("id") Long id) {
        return ApiResponse.success(classInfoService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ClassInfo> create(@RequestBody @Valid ClassInfoRequest request) {
        return ApiResponse.success(classInfoService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ClassInfo> update(@PathVariable("id") Long id, @RequestBody @Valid ClassInfoRequest request) {
        return ApiResponse.success(classInfoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        classInfoService.delete(id);
        return ApiResponse.success(null);
    }
}
