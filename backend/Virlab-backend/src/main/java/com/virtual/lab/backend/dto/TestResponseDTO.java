package com.virtual.lab.backend.dto;

import com.virtual.lab.backend.model.TestStatus;

public class TestResponseDTO {
    private Long id;
    private String name;
    private TestStatus status;
    private Double score;
    private String connectors; // Added this based on your Test entity
    private String testToDo; // Added this based on your Test entity
    private String typeOfTest; // Added this based on your Test entity

    // Only include parent DTO if you need to return it
    // private TestGroupResponseDTO testGroup;

    // Constructors
    public TestResponseDTO() {}

    public TestResponseDTO(Long id, String name, TestStatus status, Double score, String connectors, String testToDo, String typeOfTest) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.score = score;
        this.connectors = connectors;
        this.testToDo = testToDo;
        this.typeOfTest = typeOfTest;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public TestStatus getStatus() { return status; }
    public void setStatus(TestStatus status) { this.status = status; }
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    public String getConnectors() { return connectors; }
    public void setConnectors(String connectors) { this.connectors = connectors; }
    public String getTestToDo() { return testToDo; }
    public void setTestToDo(String testToDo) { this.testToDo = testToDo; }
    public String getTypeOfTest() { return typeOfTest; }
    public void setTypeOfTest(String typeOfTest) { this.typeOfTest = typeOfTest; }

    // If you decide to include nested TestGroup:
    // public TestGroupResponseDTO getTestGroup() { return testGroup; }
    // public void setTestGroup(TestGroupResponseDTO testGroup) { this.testGroup = testGroup; }
}