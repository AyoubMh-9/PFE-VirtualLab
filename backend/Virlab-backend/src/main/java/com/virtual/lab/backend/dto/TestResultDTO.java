package com.virtual.lab.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

public class TestResultDTO {
    private Long id;
    private String value;
    private String comments;
    private LocalDateTime timestamp;
    private MeasureDTO measure;

    @Data
    public static class MeasureDTO {
        private Long id;
        private String name;
        private String unit;
        // Exclure la référence circulaire à TestType

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public MeasureDTO getMeasure() {
        return measure;
    }

    public void setMeasure(MeasureDTO measure) {
        this.measure = measure;
    }
}