package com.virtual.lab.backend.dto;

import com.virtual.lab.backend.model.TestGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestGroupDTO {
    private Long id;
    private Integer groupNumber;
    private Long productId; // To link to the parent product
    private List<TestDto> tests; // Potentially include tests when fetching a group

    // Constructors
    public TestGroupDTO() {}

    public TestGroupDTO(Long id, Integer groupNumber, Long productId, List<TestDto> tests) {
        this.id = id;
        this.groupNumber = groupNumber;
        this.productId = productId;
        this.tests = tests;
    }

    /**
     * Constructor to map from TestGroup entity to TestGroupDTO.
     * This constructor should ideally be called within an active Hibernate session
     * (e.g., within a @Transactional method in a service layer) to avoid
     * LazyInitializationException for associated lazy-loaded collections/entities.
     *
     * @param group The TestGroup entity to convert.
     */
    public TestGroupDTO(TestGroup group) {
        this(); // Call the default constructor to initialize 'tests' list

        this.id = group.getId();
        this.groupNumber = group.getGroupNumber();

        // Safely get product ID if product is not null.
        // Note: If 'product' itself is lazy-loaded, this might still cause L.I.E.
        // unless 'product' is fetched eagerly via @EntityGraph or initialized.
        this.productId = (group.getProduct() != null) ? group.getProduct().getId() : null;

        // CRITICAL: Map 'tests' collection from entities to DTOs
        if (group.getTests() != null && !group.getTests().isEmpty()) {
            this.tests = group.getTests().stream()
                    .map(TestDto::new) // Assuming TestDTO has a constructor: public TestDTO(Test test)
                    .collect(Collectors.toList());
        } else {
            // Ensure the list is empty, not null, if no tests are present
            this.tests = new ArrayList<>();
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getGroupNumber() { return groupNumber; }
    public void setGroupNumber(Integer groupNumber) { this.groupNumber = groupNumber; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public List<TestDto> getTests() { return tests; }
    public void setTests(List<TestDto> tests) { this.tests = tests; }

    @Override
    public String toString() {
        return "TestGroupDTO{" +
                "id=" + id +
                ", groupNumber=" + groupNumber +
                ", productId=" + productId +
                ", tests=" + tests + // This will call tests.toString() (which calls TestDTO.toString() for each item)
                '}';
    }
}
