package com.studentmanager.service;

import com.studentmanager.entity.CourseOffering;

import java.util.List;

public interface CourseOfferingService {
    List<CourseOffering> list(int page, int size);
    
    List<CourseOffering> listBySemester(String semester);
    
    List<CourseOffering> listOpenOfferings(String semester);
    
    List<CourseOffering> listByTeacherId(Long teacherId);
    
    CourseOffering getById(Long id);
    
    CourseOffering create(CourseOffering offering);
    
    CourseOffering update(Long id, CourseOffering offering);
    
    void delete(Long id);
    
    void incrementStudentCount(Long id);
    
    void decrementStudentCount(Long id);
}
