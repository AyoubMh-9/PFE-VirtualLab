package com.virtual.lab.backend.mapper;

import com.virtual.lab.backend.dto.ProductResponseDTO;
import com.virtual.lab.backend.dto.TestGroupResponseDTO;
import com.virtual.lab.backend.dto.TestResponseDTO;
import com.virtual.lab.backend.model.Product;
import com.virtual.lab.backend.model.Test;
import com.virtual.lab.backend.model.TestGroup;
import com.virtual.lab.backend.repository.TestRepository;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TestMapper {

    private final TestRepository testRepository;

    public TestMapper(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public TestResponseDTO toDto(Test test) {
        if (test == null) {
            return null;
        }
        TestResponseDTO dto = new TestResponseDTO();
        dto.setId(test.getId());
        dto.setName(test.getName());
        dto.setStatus(test.getStatus());
        dto.setScore(test.getScore());
        dto.setConnectors(test.getConnectors());
        dto.setTestToDo(test.getTestToDo());
        dto.setTypeOfTest(test.getTypeOfTest());
        // Do NOT map the parent TestGroup here to avoid potential circular references
        // in the immediate response, as TestController only returns TestResponseDTO.
        // If you need it, you would map it explicitly in the controller or service
        // by creating a TestGroupResponseDTO and setting it.
        return dto;
    }

    public TestGroupResponseDTO toDto(TestGroup testGroup) {
        if (testGroup == null) {
            return null;
        }
        TestGroupResponseDTO dto = new TestGroupResponseDTO();
        dto.setId(testGroup.getId());
        dto.setGroupNumber(testGroup.getGroupNumber());
        dto.setCalculatedStatus(testGroup.getCalculatedStatus());
        dto.setCalculatedProgress(testGroup.getCalculatedProgress());

        // Map the tests within this group (be careful with lazy loading here)
        if (testGroup.getTests() != null && !testGroup.getTests().isEmpty()) {
            dto.setTests(testGroup.getTests().stream()
                    .map(this::toDto) // Use the TestMapper to map each test
                    .collect(Collectors.toList()));
        }

        // Map the parent product (be careful with lazy loading here)
        // If Product is lazy loaded and not fetched, this will cause error.
        // It's safer to only map it if it's already initialized or if the parent DTO needs it.
        // For the purpose of returning TestResponseDTO, we typically don't need the full Product object in TestGroup DTO.
        // if (testGroup.getProduct() != null) {
        //     dto.setProduct(toDto(testGroup.getProduct())); // Call product mapper
        // }
        return dto;
    }

    public ProductResponseDTO toDto(Product product) {
        if (product == null) {
            return null;
        }
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setNomProduct(product.getNomProduct());
        dto.setDescription(product.getDescription());
        dto.setCalculatedStatus(product.getCalculatedStatus());
        dto.setCalculatedProgress(product.getCalculatedProgress());
        dto.setCreatedAt(product.getCreatedAt());

        // Map the test groups within this product (be careful with lazy loading here)
        if (product.getTestGroups() != null && !product.getTestGroups().isEmpty()) {
            dto.setTestGroups(product.getTestGroups().stream()
                    .map(this::toDto) // Use the TestMapper to map each test group
                    .collect(Collectors.toList()));
        }

        if (product.getId() != null) {
            Long totalTests = testRepository.countTestsByProductId(product.getId());
            dto.setTotalTestsCount(totalTests);
        } else {
            dto.setTotalTestsCount(0L); // Default to 0 if product ID is somehow null
        }

        // Do NOT map the files here directly unless you've eagerly fetched them or have a separate DTO for them,
        // as this was the source of your LazyInitializationException.
        return dto;
    }
}