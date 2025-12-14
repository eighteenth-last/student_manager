package com.studentmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreResponse {
    private Long id;
    private String type;  // OFFERING or ENROLLMENT
    private Long studentId;
    private String studentNo;
    private String studentName;
    private Long courseId;
    private String courseNo;
    private String courseName;
    private Double score;
    private String semester;
    private String teacherName;
    private LocalDateTime updatedAt;
}
