package com.studentmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentScore {
    private Long id;
    private Long studentId;  // 学生ID
    private Long courseId;   // 公共课程ID
    private Double score;
    private LocalDateTime updatedAt;
}
