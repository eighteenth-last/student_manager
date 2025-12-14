package com.studentmanager.service;

import com.studentmanager.dto.TeacherRequest;
import com.studentmanager.entity.Teacher;

import java.util.List;

public interface TeacherService {
    List<Teacher> list(int page, int size);

    Teacher getById(Long id);

    Teacher create(TeacherRequest request);

    Teacher update(Long id, TeacherRequest request);

    void delete(Long id);
}
