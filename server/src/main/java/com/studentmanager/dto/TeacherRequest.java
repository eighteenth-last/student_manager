package com.studentmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeacherRequest {
    @NotBlank
    private String teacherNo;

    @NotBlank
    private String name;

    private String phone;

    private String department;
}
