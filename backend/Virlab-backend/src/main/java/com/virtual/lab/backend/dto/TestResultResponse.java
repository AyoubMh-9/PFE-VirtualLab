package com.virtual.lab.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestResultResponse {
    private Long id;
    private String value;
    private String comments;
    private LocalDateTime timestamp;
    private MeasureInfo measure;

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

    public MeasureInfo getMeasure() {
        return measure;
    }

    public void setMeasure(MeasureInfo measure) {
        this.measure = measure;
    }

    @Data
    public static class MeasureInfo {
        private Long id;
        private String name;
        private String unit;

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

    @Data
    public static class TestInfo {
        private Long id;
        private LocalDateTime dateDebut;
        private LocalDateTime dateFin;
        private String status;
        private TestTypeInfo testType;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public LocalDateTime getDateDebut() {
            return dateDebut;
        }

        public void setDateDebut(LocalDateTime dateDebut) {
            this.dateDebut = dateDebut;
        }

        public LocalDateTime getDateFin() {
            return dateFin;
        }

        public void setDateFin(LocalDateTime dateFin) {
            this.dateFin = dateFin;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public TestTypeInfo getTestType() {
            return testType;
        }

        public void setTestType(TestTypeInfo testType) {
            this.testType = testType;
        }
    }

    @Data
    public static class TestTypeInfo {
        private Long id;
        private String name;
    }
}