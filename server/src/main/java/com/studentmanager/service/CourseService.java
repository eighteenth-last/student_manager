package com.studentmanager.service;

import com.studentmanager.dto.CourseRequest;
import com.studentmanager.entity.Course;

import java.util.List;

public interface CourseService {
    List<Course> list(int page, int size);

    List<Course> listByTeacher(Long teacherId);

    Course getById(Long id);

    Course create(CourseRequest request);

    Course update(Long id, CourseRequest request);

    void delete(Long id);
}
