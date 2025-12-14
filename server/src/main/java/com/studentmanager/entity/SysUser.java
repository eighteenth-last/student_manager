package com.studentmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUser {
    private Long id;
    private String username;
    private String password;
    private String role; // STUDENT / TEACHER / ADMIN
    private Long relatedId; // student_id or teacher_id
    private Integer status; // 1 enabled, 0 disabled
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
