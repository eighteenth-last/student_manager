package com.studentmanager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScoreRequest {
    @NotNull
    private Long studentId;

    @NotNull
    private Long courseId;

    @NotNull
    private Double score;
}
