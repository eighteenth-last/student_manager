package com.studentmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    private Long id;
    private String teacherNo;
    private String name;
    private String phone;
    private String department;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
