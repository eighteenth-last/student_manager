package com.studentmanager.service;

import com.studentmanager.dto.StudentRequest;
import com.studentmanager.entity.Student;

import java.util.List;

public interface StudentService {
    List<Student> list(int page, int size);

    Student getById(Long id);

    Student create(StudentRequest request);

    Student update(Long id, StudentRequest request);

    void delete(Long id);
}
