package com.studentmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourse {
    private Long id;
    private Long studentId;
    private Long courseOfferingId;
    private String status; // ENROLLED, DROPPED, COMPLETED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
