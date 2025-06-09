package com.virtual.lab.backend.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "test_group") // Good practice to use a clear table name
public class TestGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_number") // e.g., 0, 1, 2
    private Integer groupNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false) // Links to Product
    private Product product;

    // --- One-to-Many relationship with Test ---
    @OneToMany(mappedBy = "testGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Test> tests = new HashSet<>(); // NEW: Change to Set

    // NEW: Calculated fields for status and progress
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestStatus calculatedStatus = TestStatus.EN_ATTENTE; // Default initial status

    private Double calculatedProgress = 0.0; // Default initial progress

    // Constructors, Getters, and Setters
    public TestGroup() {}

    public TestGroup(Integer groupNumber, Product product) {
        this.groupNumber = groupNumber;
        this.product = product;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getGroupNumber() { return groupNumber; }
    public void setGroupNumber(Integer groupNumber) { this.groupNumber = groupNumber; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Set<Test> getTests() { return tests; }
    public void setTests(Set<Test> tests) { this.tests = tests; }
    public TestStatus getCalculatedStatus() { return calculatedStatus; }
    public void setCalculatedStatus(TestStatus calculatedStatus) { this.calculatedStatus = calculatedStatus; }
    public Double getCalculatedProgress() { return calculatedProgress; }
    public void setCalculatedProgress(Double calculatedProgress) { this.calculatedProgress = calculatedProgress; }

    // Helper methods for managing tests to ensure bidirectional link
    public void addTest(Test test) {
        this.tests.add(test);
        test.setTestGroup(this);
    }

    public void removeTest(Test test) {
        this.tests.remove(test);
        test.setTestGroup(null);
    }


    // NEW: Method to recalculate status and progress for this group
    @PrePersist // Ensure initial calculation on save
    @PreUpdate // Ensure recalculation on update
    public void recalculateStatusAndProgress() {
        if (this.tests == null || this.tests.isEmpty()) {
            this.setCalculatedStatus(TestStatus.EN_ATTENTE);
            this.setCalculatedProgress(0.0);
            return;
        }

        long totalTests = this.tests.size();
        long completedTests = this.tests.stream()
                .filter(test -> test.getStatus() == TestStatus.RÉUSSI || test.getStatus() == TestStatus.ÉCHOUÉ)
                .count();

        // Calculate Progress
        this.setCalculatedProgress((double) completedTests / totalTests * 100.0);

        // Determine Status (Hierarchy of Severity)
        if (this.tests.stream().anyMatch(test -> test.getStatus() == TestStatus.ÉCHOUÉ)) {
            this.setCalculatedStatus(TestStatus.ÉCHOUÉ);
        } else if (this.tests.stream().anyMatch(test -> test.getStatus() == TestStatus.EN_COURS)) {
            this.setCalculatedStatus(TestStatus.EN_COURS);
        } else if (this.tests.stream().anyMatch(test -> test.getStatus() == TestStatus.EN_ATTENTE)) {
            this.setCalculatedStatus(TestStatus.EN_ATTENTE);
        } else { // All tests must be RÉUSSI
            this.setCalculatedStatus(TestStatus.TERMINÉ);
        }

        // IMPORTANT: Cascade update to Product.
        // If using @PreUpdate/@PrePersist, you might need to manually trigger product update
        // in your service layer because JPA won't automatically trigger parent updates via child's @PreUpdate.
        // A service layer approach is usually more robust for this cascading logic.
        if (this.product != null) {
            this.product.recalculateStatusAndProgress(); // This will trigger Product's own recalculation
        }
    }
}