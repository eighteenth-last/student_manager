package com.studentmanager.service;

import com.studentmanager.dto.ClassInfoRequest;
import com.studentmanager.entity.ClassInfo;

import java.util.List;

public interface ClassInfoService {
    List<ClassInfo> list(int page, int size);

    ClassInfo getById(Long id);

    ClassInfo create(ClassInfoRequest request);

    ClassInfo update(Long id, ClassInfoRequest request);

    void delete(Long id);
}
