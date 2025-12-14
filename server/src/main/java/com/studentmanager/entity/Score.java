package com.studentmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Score {
    private Long id;
    private Long studentCourseId;
    private Long courseId; // 这里存储的是course_offering_id
    private Long studentId;
    private Double score;
    private LocalDateTime updatedAt;
}
