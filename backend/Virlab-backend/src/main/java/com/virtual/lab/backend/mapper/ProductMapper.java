package com.virtual.lab.backend.mapper;

import com.virtual.lab.backend.dto.ProductResponseDTO;
import com.virtual.lab.backend.model.Product;
import com.virtual.lab.backend.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductMapper {

    @Autowired // Inject your TestMapper
    private TestMapper testMapper;

    private final TestRepository testRepository; // <--- NEW: Inject TestRepository

    @Autowired // Add this if you have a UserMapper
    private UserMapper userMapper;

    // <--- NEW: Constructor for dependency injection
    public ProductMapper(TestRepository testRepository) {
        this.testRepository = testRepository;
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
        dto.setAccessCode(product.getAccessCode()); // Correct casing for DTO field

        // Map nested TestGroups
        if (product.getTestGroups() != null && !product.getTestGroups().isEmpty()) {
            dto.setTestGroups(product.getTestGroups().stream()
                    .map(testMapper::toDto)
                    .collect(Collectors.toList()));
        }

        // Map totalTestsCount
        if (product.getId() != null) {
            Long totalTests = testRepository.countTestsByProductId(product.getId());
            dto.setTotalTestsCount(totalTests);
        } else {
            dto.setTotalTestsCount(0L);
        }

        // --- NEW: Map Client and Technician ---
        // Assuming your Product entity has getClient() and getTechnician() methods
        // and they return Client/Technician (or User) entities.
        if (product.getClient() != null) {
            dto.setClient(userMapper.toDto(product.getClient())); // Convert Client entity to UserResponseDTO
        } else {
            dto.setClient(null); // Explicitly set to null if no client
        }

        if (product.getTechnician() != null) {
            dto.setTechnician(userMapper.toDto(product.getTechnician())); // Convert Technician entity to UserResponseDTO
        } else {
            dto.setTechnician(null); // Explicitly set to null if no technician
        }

        return dto;
    }
}
