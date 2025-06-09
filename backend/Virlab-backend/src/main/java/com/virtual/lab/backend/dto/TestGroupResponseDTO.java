package com.virtual.lab.backend.dto;

import com.virtual.lab.backend.model.TestStatus;
import java.util.List;
import java.util.ArrayList;

public class TestGroupResponseDTO {
    private Long id;
    private Integer groupNumber;
    private TestStatus calculatedStatus;
    private Double calculatedProgress;
    // List of Tests in this group - important for recalculation insight
    private List<TestResponseDTO> tests = new ArrayList<>();

    // Only include parent DTO if you need to return it
    // private ProductResponseDTO product;

    // Constructors
    public TestGroupResponseDTO() {}

    public TestGroupResponseDTO(Long id, Integer groupNumber, TestStatus calculatedStatus, Double calculatedProgress) {
        this.id = id;
        this.groupNumber = groupNumber;
        this.calculatedStatus = calculatedStatus;
        this.calculatedProgress = calculatedProgress;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getGroupNumber() { return groupNumber; }
    public void setGroupNumber(Integer groupNumber) { this.groupNumber = groupNumber; }
    public TestStatus getCalculatedStatus() { return calculatedStatus; }
    public void setCalculatedStatus(TestStatus calculatedStatus) { this.calculatedStatus = calculatedStatus; }
    public Double getCalculatedProgress() { return calculatedProgress; }
    public void setCalculatedProgress(Double calculatedProgress) { this.calculatedProgress = calculatedProgress; }

    public List<TestResponseDTO> getTests() { return tests; }
    public void setTests(List<TestResponseDTO> tests) { this.tests = tests; }

    // If you decide to include nested Product:
    // public ProductResponseDTO getProduct() { return product; }
    // public void setProduct(ProductResponseDTO product) { this.product = product; }
}