package com.virtual.lab.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "test") // Table for individual tests
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., "Project study", "Visual and mechanical inspection - MIL"
    private String connectors; // "Connectors" column from image
    private String standardsComments; // "Standards / Targets / Comments" column
    private String testToDo; // "Test to do" column
    private String typeOfTest; // "Type of test" column

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestStatus status = TestStatus.EN_ATTENTE; // NEW: Status for individual test

    private Double score; // NEW: Score for individual test (e.g., 92%)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_group_id", nullable = false) // Links to TestGroup
    private TestGroup testGroup;

    // Constructors, Getters, and Setters
    public Test() {}

    public Test(String name, String connectors, String standardsComments, String testToDo, String typeOfTest,  TestStatus status ,Double score,TestGroup testGroup) {
        this.name = name;
        this.connectors = connectors;
        this.standardsComments = standardsComments;
        this.testToDo = testToDo;
        this.typeOfTest = typeOfTest;
        this.status = status;
        this.score = score;
        this.testGroup = testGroup;
    }

    @PrePersist
    @PreUpdate
    public void onStatusChange() {
         if (this.testGroup != null) {
            this.testGroup.recalculateStatusAndProgress();
         }
     }

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
    public TestStatus getStatus() { return status; }
    public void setStatus(TestStatus status) { this.status = status; }
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    public TestGroup getTestGroup() { return testGroup; }
    public void setTestGroup(TestGroup testGroup) { this.testGroup = testGroup; }
}
