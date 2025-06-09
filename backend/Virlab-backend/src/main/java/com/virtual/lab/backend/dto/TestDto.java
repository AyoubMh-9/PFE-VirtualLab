package com.virtual.lab.backend.dto;

import com.virtual.lab.backend.model.Test;

public class TestDto {
    private Long id;
    private String name;
    private String connectors;
    private String standardsComments;
    private String testToDo;
    private String typeOfTest;
    private Long testGroupId; // To link to the parent group

    // Constructors
    public TestDto() {}

    public TestDto(Long id, String name, String connectors, String standardsComments, String testToDo, String typeOfTest, Long testGroupId) {
        this.id = id;
        this.name = name;
        this.connectors = connectors;
        this.standardsComments = standardsComments;
        this.testToDo = testToDo;
        this.typeOfTest = typeOfTest;
        this.testGroupId = testGroupId;
    }

    /**
     * Constructor to map from Test entity to TestDTO.
     * This is used when converting entities fetched from the database to DTOs.
     *
     * @param test The Test entity to convert.
     */
    public TestDto(Test test) {
        // Populate DTO fields from the entity's fields
        this.id = test.getId();
        this.name = test.getName();
        this.connectors = test.getConnectors();
        this.standardsComments = test.getStandardsComments();
        this.testToDo = test.getTestToDo();
        this.typeOfTest = test.getTypeOfTest();
        // Map other fields from 'test' entity to 'this' DTO as needed
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getConnectors() { return connectors; }
    public void setConnectors(String connectors) { this.connectors = connectors; }
    public String getStandardsComments() { return standardsComments; }
    public void setStandardsComments(String standardsComments) { this.standardsComments = standardsComments; }
    public String getTestToDo() { return testToDo; }
    public void setTestToDo(String testToDo) { this.testToDo = testToDo; }
    public String getTypeOfTest() { return typeOfTest; }
    public void setTypeOfTest(String typeOfTest) { this.typeOfTest = typeOfTest; }
    public Long getTestGroupId() { return testGroupId; }
    public void setTestGroupId(Long testGroupId) { this.testGroupId = testGroupId; }

    @Override
    public String toString() {
        return "TestDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", connectors='" + connectors + '\'' +
                ", standardsComments='" + standardsComments + '\'' +
                ", testToDo='" + testToDo + '\'' +
                ", typeOfTest='" + typeOfTest + '\'' +
                '}';
    }

}
