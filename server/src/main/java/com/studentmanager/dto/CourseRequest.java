package com.studentmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseRequest {
    @NotBlank
    private String courseNo;

    @NotBlank
    private String courseName;

    @NotNull
    private Double credit;

    @NotNull
    private Long teacherId;

    private String classTime;
}
