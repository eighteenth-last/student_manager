package com.studentmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferingScore {
    private Long id;
    private Long studentCourseId;  // 关联student_course表
    private Double score;
    private LocalDateTime updatedAt;
}
