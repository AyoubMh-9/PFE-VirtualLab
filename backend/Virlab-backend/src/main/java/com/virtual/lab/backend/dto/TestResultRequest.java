package com.virtual.lab.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestResultRequest {
    @NotNull
    private Long testId;

    @NotNull
    private Long measureId;

    @NotBlank
    private String value;

    private String comments;

    public @NotNull Long getTestId() {
        return testId;
    }

    public void setTestId(@NotNull Long testId) {
        this.testId = testId;
    }

    public @NotNull Long getMeasureId() {
        return measureId;
    }

    public void setMeasureId(@NotNull Long measureId) {
        this.measureId = measureId;
    }

    public @NotBlank String getValue() {
        return value;
    }

    public void setValue(@NotBlank String value) {
        this.value = value;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}