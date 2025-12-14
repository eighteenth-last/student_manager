package com.studentmanager.service;

import com.studentmanager.dto.ScoreRequest;
import com.studentmanager.dto.ScoreResponse;

import java.util.List;

public interface ScoreService {
    ScoreResponse recordOfferingScore(ScoreRequest request);
    
    ScoreResponse recordEnrollmentScore(ScoreRequest request);

    List<ScoreResponse> listByStudent(Long studentId);

    List<ScoreResponse> listByCourse(Long courseId, String type); // type: OFFERING or ENROLLMENT

    List<ScoreResponse> listAll(String type);
    
    List<Object> getStudentsByCourse(Long courseId, String type);
    
    Object getTeacherCourses(Long teacherId, String type);
}
