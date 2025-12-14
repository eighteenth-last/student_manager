package com.studentmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    private Long id;
    private String deptCode;
    private String deptName;
    private LocalDateTime createdAt;
}
