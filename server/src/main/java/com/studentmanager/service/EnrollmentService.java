package com.studentmanager.service;

import com.studentmanager.entity.StudentCourse;

import java.util.List;

public interface EnrollmentService {
    StudentCourse enroll(Long studentId, Long courseId);

    List<StudentCourse> listByStudent(Long studentId);

    List<StudentCourse> listByCourse(Long courseId);

    List<StudentCourse> listAll();

    void drop(Long enrollmentId);
}
