package com.studentmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClassInfoRequest {
    @NotBlank
    private String classCode;

    private String major;

    private String headTeacher;

    @NotNull
    private Integer entryYear;
}
