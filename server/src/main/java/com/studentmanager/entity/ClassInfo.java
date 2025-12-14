package com.studentmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassInfo {
    private Long id;
    private String classCode;
    private String major;
    private String headTeacher;
    private Integer entryYear;
    private Integer studentCount;
    private LocalDateTime createdAt;
}
