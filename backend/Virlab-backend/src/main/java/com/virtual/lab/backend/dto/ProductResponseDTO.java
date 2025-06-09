package com.virtual.lab.backend.dto;

import com.virtual.lab.backend.model.TestStatus; // Ensure this enum is correctly imported
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductResponseDTO {
    private Long id;
    private String nomProduct;
    private String description;
    private TestStatus calculatedStatus;
    private Double calculatedProgress;
    private LocalDateTime createdAt;
    private String accessCode;
    private List<TestGroupResponseDTO> testGroups; // Important for nested data
    private Long totalTestsCount;
    private Long totalFilesCount;

    private UserResponseDTO client; // Or ClientResponseDTO/TechnicianResponseDTO if distinct
    private UserResponseDTO technician;

    public ProductResponseDTO() {
    }

    public ProductResponseDTO(Long id, String nomProduct, String description, TestStatus calculatedStatus, Double calculatedProgress, List<TestGroupResponseDTO> testGroups, Long totalTestsCount, UserResponseDTO cleint, UserResponseDTO technicien) {
        this.id = id;
        this.nomProduct = nomProduct;
        this.description = description;
        this.calculatedStatus = calculatedStatus;
        this.calculatedProgress = calculatedProgress;
        this.testGroups = testGroups;
        this.totalTestsCount = totalTestsCount;
        this.client = cleint;
        this.technician = technicien;
    }




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomProduct() {
        return nomProduct;
    }

    public void setNomProduct(String nomProduct) {
        this.nomProduct = nomProduct;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TestStatus getCalculatedStatus() {
        return calculatedStatus;
    }

    public void setCalculatedStatus(TestStatus calculatedStatus) {
        this.calculatedStatus = calculatedStatus;
    }

    public Double getCalculatedProgress() {
        return calculatedProgress;
    }

    public void setCalculatedProgress(Double calculatedProgress) {
        this.calculatedProgress = calculatedProgress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public List<TestGroupResponseDTO> getTestGroups() {
        return testGroups;
    }

    public void setTestGroups(List<TestGroupResponseDTO> testGroups) {
        this.testGroups = testGroups;
    }

    public Long getTotalTestsCount() {
        return totalTestsCount;
    }

    public void setTotalTestsCount(Long totalTestsCount) {
        this.totalTestsCount = totalTestsCount;
    }

    public Long getTotalFilesCount() {
        return totalFilesCount;
    }

    public void setTotalFilesCount(Long totalFilesCount) {
        this.totalFilesCount = totalFilesCount;
    }

    public UserResponseDTO getClient() {
        return client;
    }

    public void setClient(UserResponseDTO client) {
        this.client = client;
    }

    public UserResponseDTO getTechnician() {
        return technician;
    }

    public void setTechnician(UserResponseDTO technician) {
        this.technician = technician;
    }
}