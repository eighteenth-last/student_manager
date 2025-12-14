package com.studentmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseOffering {
    private Long id;
    private String courseNo;
    private String courseName;
    private Double credit;
    private String semester;
    private Long teacherId;
    private String classTime;
    private String classroom;
    private Integer maxStudents;
    private Integer currentStudents;
    private String status; // OPEN, CLOSED, FINISHED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 用于显示的冗余字段
    private String teacherName;
}
