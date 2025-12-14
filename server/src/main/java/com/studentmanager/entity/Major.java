package com.studentmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Major {
    private Long id;
    private String majorCode;
    private String majorName;
    private Long departmentId;
    private LocalDateTime createdAt;
    
    // 关联字段
    private String departmentName;
}
