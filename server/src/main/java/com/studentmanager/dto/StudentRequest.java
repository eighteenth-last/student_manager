package com.studentmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentRequest {
    @NotBlank
    private String studentNo;

    @NotBlank
    private String name;

    private Integer age;

    private String gender;

    private String phone;

    private String department;

    private String major;

    @NotNull
    private Long classId;
}
