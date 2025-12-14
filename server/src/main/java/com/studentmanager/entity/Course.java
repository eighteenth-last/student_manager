package com.studentmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private Long id;
    private String courseNo;
    private String courseName;
    private Double credit;
    private Long teacherId;
    private String classTime;
    private LocalDateTime createdAt;
}
