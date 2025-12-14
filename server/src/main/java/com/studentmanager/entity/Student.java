package com.studentmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Long id;
    private String studentNo;
    private String name;
    private Integer age;
    private String gender;
    private String phone;
    private String department;
    private String major;
    private Long classId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
