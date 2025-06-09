package com.virtual.lab.backend.service;

import com.virtual.lab.backend.dto.TestGroupDTO;
import com.virtual.lab.backend.model.Product;
import com.virtual.lab.backend.model.TestGroup;
import com.virtual.lab.backend.repository.ProductRepository;
import com.virtual.lab.backend.repository.TestGroupRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestGroupService {

    @Autowired
    private TestGroupRepository testGroupRepository;

    @Autowired
    private ProductRepository productRepository; // To link groups to products

    // Helper to convert Entity to DTO
    public TestGroupDTO convertToDto(TestGroup testGroup) {
        if (testGroup == null) return null;
        TestGroupDTO dto = new TestGroupDTO();
        dto.setId(testGroup.getId());
        dto.setGroupNumber(testGroup.getGroupNumber());
        if (testGroup.getProduct() != null) {
            dto.setProductId(testGroup.getProduct().getId());
        }
        // If you want to include nested tests, you'd convert them here too
        // dto.setTests(testGroup.getTests().stream().map(testService::convertToDto).collect(Collectors.toList()));
        return dto;
    }

    // Helper to convert DTO to Entity (for creation/update)
    public TestGroup convertToEntity(TestGroupDTO dto) {
        TestGroup testGroup = new TestGroup();
        // ID is usually set by DB on creation, so no need to set here unless for update
        testGroup.setGroupNumber(dto.getGroupNumber());
        // Product needs to be fetched and set
        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + dto.getProductId()));
            testGroup.setProduct(product);
        }
        return testGroup;
    }


    // CRUD Operations

    @Transactional
    public TestGroupDTO createTestGroup(TestGroupDTO testGroupDto) {
        // Ensure product exists
        if (testGroupDto.getProductId() == null) {
            throw new IllegalArgumentException("TestGroup must be associated with a Product.");
        }
        Product product = productRepository.findById(testGroupDto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + testGroupDto.getProductId()));

        TestGroup testGroup = convertToEntity(testGroupDto);
        testGroup.setProduct(product); // Link it explicitly

        // Add to product's list if using bidirectional
        product.addTestGroup(testGroup); // This handles the bidirectional link

        TestGroup savedTestGroup = testGroupRepository.save(testGroup);
        productRepository.save(product); // Save product to persist the relationship if cascade isn't full on product side

        return convertToDto(savedTestGroup);
    }

    @Transactional(readOnly = true)
    public Optional<TestGroupDTO> getTestGroupById(Long id) {
        return testGroupRepository.findById(id).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<TestGroupDTO> getAllTestGroups() {
        return testGroupRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestGroupDTO> getTestGroupsByProductId(Long productId) {
        return testGroupRepository.findByProductId(productId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public TestGroupDTO updateTestGroup(Long id, TestGroupDTO testGroupDto) {
        TestGroup existingTestGroup = testGroupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TestGroup not found with ID: " + id));

        existingTestGroup.setGroupNumber(testGroupDto.getGroupNumber());
        // If product can be changed (unlikely for groups, usually set at creation):
        if (testGroupDto.getProductId() != null && !existingTestGroup.getProduct().getId().equals(testGroupDto.getProductId())) {
            Product newProduct = productRepository.findById(testGroupDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + testGroupDto.getProductId()));
            existingTestGroup.getProduct().removeTestGroup(existingTestGroup); // Remove from old product
            newProduct.addTestGroup(existingTestGroup); // Add to new product
            productRepository.save(existingTestGroup.getProduct()); // Save old product
            productRepository.save(newProduct); // Save new product
        }


        TestGroup updatedTestGroup = testGroupRepository.save(existingTestGroup);
        return convertToDto(updatedTestGroup);
    }

    @Transactional
    public void deleteTestGroup(Long id) {
        // Optional: Perform checks here, e.g., if group has tests, should it be deleted?
        TestGroup testGroup = testGroupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TestGroup not found with ID: " + id));

        // Important: If cascade options in entity are not perfect for bidirectional relationships,
        // manually remove from the parent product's list before deleting
        if (testGroup.getProduct() != null) {
            testGroup.getProduct().removeTestGroup(testGroup);
            productRepository.save(testGroup.getProduct()); // Save parent to reflect removal
        }
        testGroupRepository.delete(testGroup);
    }
}
