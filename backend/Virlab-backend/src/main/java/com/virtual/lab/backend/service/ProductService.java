package com.virtual.lab.backend.service;

import com.virtual.lab.backend.dto.ProductDTO;
import com.virtual.lab.backend.dto.ProductResponseDTO;
import com.virtual.lab.backend.dto.TestGroupDTO; // Use the top-level DTOs
import com.virtual.lab.backend.mapper.ProductMapper;
import com.virtual.lab.backend.model.*;
import com.virtual.lab.backend.repository.*;
import com.virtual.lab.backend.util.CodeGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;
    private final TechnicianRepository technicianRepository;
    private final TestGroupRepository testGroupRepository; // New: Inject TestGroupRepository
    private final TestRepository testRepository; // New: Inject TestRepository

    // If you created separate TestGroupService and TestService, inject them here
    private final TestGroupService testGroupService;
    private final TestService testService;
    private final ProductMapper productMapper; // Inject the ProductMapper
    private final UserRepository userRepository;


    @Autowired
    public ProductService(ProductRepository productRepository, AdminRepository adminRepository,
                          ClientRepository clientRepository, TechnicianRepository technicianRepository,
                          TestGroupRepository testGroupRepository, TestRepository testRepository,
                          TestGroupService testGroupService, TestService testService, ProductMapper productMapper, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
        this.technicianRepository = technicianRepository;
        this.testGroupRepository = testGroupRepository;
        this.testRepository = testRepository;
        this.testGroupService = testGroupService; // Inject the service
        this.testService = testService;         // Inject the service
        this.productMapper = productMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    public Product create(Product product) {
        if (product.getAccessCode() == null || product.getAccessCode().isEmpty()) {
            String generatedCode;
            do {
                generatedCode = CodeGenerator.generateAccessCode();
            } while (productRepository.findByAccessCode(generatedCode).isPresent());

            product.setAccessCode(generatedCode);
        }

        return productRepository.save(product);
    }

    // In ProductService.java
    @Transactional(readOnly = true) // This transaction keeps the session open
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getProductDtoById(Long id) {
        Product product = getById(id); // Fetch the entity
        return productMapper.toDto(product); // Convert using MapStruct mapper
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProductsDto() {
        List<Product> products = productRepository.findAll(); // Fetch all entities
        return products.stream()
                .map(productMapper::toDto) // Convert each entity to DTO
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProjectsForClient(Long clientId) {
        // Assuming your Product entity has a direct Client relationship
        // or a query to get products by client ID
        List<Product> products = productRepository.findByClientId(clientId); // Example method, you need to add this to ProductRepository
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findByTechnicianId(Long technicianId) {
        List<Product> products = productRepository.findByTechnicianId(technicianId); // Add this to ProductRepository
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public ProductResponseDTO updateProduct(Long id, Product updatedProductDetails) {
        System.out.println("--- Entering ProductService.updateProduct ---");
        System.out.println("Product ID to update: " + id);
        System.out.println("Incoming updatedProductDetails DTO: " + updatedProductDetails); // This will print Product.toString()

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("ERROR: Product not found with ID: " + id);
                    return new EntityNotFoundException("Product not found with ID: " + id);
                });

        System.out.println("Existing Product before update: " + existingProduct);
        if (existingProduct.getTechnician() != null) {
            System.out.println("Existing Product Technician ID: " + existingProduct.getTechnician().getId() + ", Username: " + existingProduct.getTechnician().getUsername());
        } else {
            System.out.println("Existing Product has no Technician.");
        }


        // 1. Update nomProduct (optional: add null check if it can be omitted from update)
        if (updatedProductDetails.getNomProduct() != null) {
            existingProduct.setNomProduct(updatedProductDetails.getNomProduct());
            System.out.println("Updating nomProduct to: " + updatedProductDetails.getNomProduct());
        } else {
            System.out.println("nomProduct not provided in DTO. Keeping existing: " + existingProduct.getNomProduct());
        }

        // 2. Update description (optional: add null check if it can be omitted from update)
        if (updatedProductDetails.getDescription() != null) {
            existingProduct.setDescription(updatedProductDetails.getDescription());
            System.out.println("Updating description to: " + updatedProductDetails.getDescription());
        } else {
            System.out.println("description not provided in DTO. Keeping existing: " + existingProduct.getDescription());
        }

        // 3. Optional Update for calculatedStatus
        if (updatedProductDetails.getCalculatedStatus() != null) {
            existingProduct.setCalculatedStatus(updatedProductDetails.getCalculatedStatus());
            System.out.println("Updating calculatedStatus to: " + updatedProductDetails.getCalculatedStatus());
        } else {
            System.out.println("calculatedStatus not provided in DTO. Keeping existing: " + existingProduct.getCalculatedStatus());
        }

        // 4. Optional Update for accessCode
        if (updatedProductDetails.getAccessCode() != null) {
            existingProduct.setAccessCode(updatedProductDetails.getAccessCode());
            System.out.println("Updating accessCode to: " + updatedProductDetails.getAccessCode());
        } else {
            System.out.println("accessCode not provided in DTO. Keeping existing: " + existingProduct.getAccessCode());
        }


        // 5. Handle Technician Relationship (Optional Update)
        System.out.println("--- Technician Update Logic ---");
        if (updatedProductDetails.getTechnician() != null) {
            System.out.println("updatedProductDetails.getTechnician() is NOT null. Checking ID...");
            Long newTechnicianId = updatedProductDetails.getTechnician().getId();
            System.out.println("Incoming Technician ID from DTO: " + newTechnicianId);

            if (newTechnicianId != null) {
                Optional<Technician> technicianOpt = technicianRepository.findById(newTechnicianId);
                if (technicianOpt.isPresent()) {
                    Technician newTechnician = technicianOpt.get();
                    existingProduct.setTechnician(newTechnician);
                    System.out.println("Found Technician: ID=" + newTechnician.getId() + ", Username=" + newTechnician.getUsername());
                    System.out.println("Existing Product Technician set to new Technician with ID: " + newTechnician.getId());
                } else {
                    System.out.println("ERROR: Technician not found with ID: " + newTechnicianId + ". Throwing EntityNotFoundException.");
                    throw new EntityNotFoundException("Technician not found with ID: " + newTechnicianId);
                }
            } else {
                // 'technician' field was present but its 'id' was null.
                System.out.println("Incoming Technician ID is null. Explicitly disassociating Technician.");
                existingProduct.setTechnician(null);
            }
        } else {
            System.out.println("updatedProductDetails.getTechnician() is null (field omitted from request). Preserving existing Technician.");
            // No action needed, existingProduct.getTechnician() remains unchanged
        }
        System.out.println("--- End Technician Update Logic ---");


        // 6. Handle Client Relationship (DO NOT UPDATE via this endpoint)
        System.out.println("--- Client Update Logic (Ignored) ---");
        // No logic to update client. This ensures the client relationship on existingProduct
        // remains as it was before the update, regardless of the incoming DTO.
        System.out.println("Client field is not updated by this endpoint. Preserving existing client.");
        System.out.println("--- End Client Update Logic ---");


        System.out.println("Product object STATE before calling save(): " + existingProduct);
        if (existingProduct.getTechnician() != null) {
            System.out.println("Product's Technician ID before saving: " + existingProduct.getTechnician().getId() + ", Username: " + existingProduct.getTechnician().getUsername());
        } else {
            System.out.println("Product has no Technician before saving.");
        }


        Product savedProduct = productRepository.save(existingProduct); // This persists changes to DB
        System.out.println("Product saved successfully. Returned entity: " + savedProduct);
        if (savedProduct.getTechnician() != null) {
            System.out.println("Saved Product Technician ID: " + savedProduct.getTechnician().getId() + ", Username: " + savedProduct.getTechnician().getUsername());
        } else {
            System.out.println("Saved Product has no Technician.");
        }

        ProductResponseDTO responseDTO = productMapper.toDto(savedProduct); // Convert persisted entity to DTO for response
        System.out.println("Response DTO generated for frontend: " + responseDTO);
        if (responseDTO.getTechnician() != null) {
            System.out.println("Response DTO Technician ID: " + responseDTO.getTechnician().getId() + ", Username: " + responseDTO.getTechnician().getUsername());
        } else {
            System.out.println("Response DTO has no Technician.");
        }
        System.out.println("--- Exiting ProductService.updateProduct ---");
        return responseDTO;
    }

    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    // Method to update test status (called from TestController or ProductService itself)
    @Transactional
    public Test updateTestStatus(Long testId, TestStatus newStatus, Double newScore) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new EntityNotFoundException("Test not found with ID: " + testId));

        test.setStatus(newStatus);
        test.setScore(newScore); // Score can be null, backend logic handles it
        Test savedTest = testRepository.save(test);

        // After updating a test, you MUST recalculate the progress/status
        // of its parent TestGroup and Product. This is crucial.
        // Assuming `Test` has a `getTestGroup()` method and `TestGroup` has a `getProduct()` method.
        if (savedTest.getTestGroup() != null) {
            TestGroup testGroup = savedTest.getTestGroup();
            recalculateTestGroupProgressAndStatus(testGroup);
            if (testGroup.getProduct() != null) {
                recalculateProductProgressAndStatus(testGroup.getProduct());
            }
        }
        return savedTest;
    }


    // --- Recalculation Logic (Important for progress bars) ---
    @Transactional // Ensure this method is transactional if it modifies entities
    public void recalculateTestGroupProgressAndStatus(TestGroup testGroup) {
        if (testGroup == null || testGroup.getTests() == null || testGroup.getTests().isEmpty()) {
            testGroup.setCalculatedProgress(0.0);
            testGroup.setCalculatedStatus(TestStatus.EN_ATTENTE);
            return;
        }

        long completedTests = testGroup.getTests().stream()
                .filter(test -> test.getStatus() == TestStatus.RÉUSSI || test.getStatus() == TestStatus.ÉCHOUÉ)
                .count();
        double progress = (double) completedTests / testGroup.getTests().size() * 100;
        testGroup.setCalculatedProgress(progress);

        boolean allTestsSuccessful = testGroup.getTests().stream()
                .allMatch(test -> test.getStatus() == TestStatus.RÉUSSI);
        boolean anyTestFailed = testGroup.getTests().stream()
                .anyMatch(test -> test.getStatus() == TestStatus.ÉCHOUÉ);
        boolean anyTestInProgress = testGroup.getTests().stream()
                .anyMatch(test -> test.getStatus() == TestStatus.EN_COURS);

        if (allTestsSuccessful) {
            testGroup.setCalculatedStatus(TestStatus.RÉUSSI);
        } else if (anyTestFailed) {
            testGroup.setCalculatedStatus(TestStatus.ÉCHOUÉ);
        } else if (anyTestInProgress) {
            testGroup.setCalculatedStatus(TestStatus.EN_COURS);
        } else if (completedTests > 0) { // Some tests done, but not all successful, and no failures or in progress
            testGroup.setCalculatedStatus(TestStatus.EN_COURS); // Partially completed
        } else {
            testGroup.setCalculatedStatus(TestStatus.EN_ATTENTE);
        }

        // You might need to save the testGroup explicitly if it's not cascaded from Product
        // testGroupRepository.save(testGroup);
    }

    @Transactional // Ensure this method is transactional if it modifies entities
    public void recalculateProductProgressAndStatus(Product product) {
        if (product == null || product.getTestGroups() == null || product.getTestGroups().isEmpty()) {
            product.setCalculatedProgress(0.0);
            product.setCalculatedStatus(TestStatus.EN_ATTENTE);
            return;
        }

        double totalGroupProgress = product.getTestGroups().stream()
                .mapToDouble(TestGroup::getCalculatedProgress)
                .sum();
        double overallProgress = totalGroupProgress / product.getTestGroups().size();
        product.setCalculatedProgress(overallProgress);

        boolean allGroupsSuccessful = product.getTestGroups().stream()
                .allMatch(group -> group.getCalculatedStatus() == TestStatus.RÉUSSI);
        boolean anyGroupFailed = product.getTestGroups().stream()
                .anyMatch(group -> group.getCalculatedStatus() == TestStatus.ÉCHOUÉ);
        boolean anyGroupInProgress = product.getTestGroups().stream()
                .anyMatch(group -> group.getCalculatedStatus() == TestStatus.EN_COURS);

        if (allGroupsSuccessful) {
            product.setCalculatedStatus(TestStatus.RÉUSSI);
        } else if (anyGroupFailed) {
            product.setCalculatedStatus(TestStatus.ÉCHOUÉ);
        } else if (anyGroupInProgress) {
            product.setCalculatedStatus(TestStatus.EN_COURS);
        } else if (overallProgress > 0) {
            product.setCalculatedStatus(TestStatus.EN_COURS); // Partially completed
        }
        else {
            product.setCalculatedStatus(TestStatus.EN_ATTENTE);
        }

        // You might need to save the product explicitly
        // productRepository.save(product);
    }

    public List<ProductResponseDTO> getProjectForClient(Long id) {


        List<Product> products = productRepository.findByClientId(id);

        return products.stream().map(product -> {
            ProductResponseDTO dto = new ProductResponseDTO();
            dto.setId(product.getId());
            dto.setNomProduct(product.getNomProduct());
            dto.setDescription(product.getDescription());
            dto.setCalculatedStatus(product.getCalculatedStatus());
            dto.setCalculatedProgress(product.getCalculatedProgress());

            // *** THIS IS THE KEY PART ***
            // Calculate the test count for the current product
            Long totalTests = testRepository.countTestsByProductId(product.getId());
            dto.setTotalTestsCount(totalTests); // Set the calculated count in the DTO

            // If you are also mapping nested TestGroups
            // if (product.getTestGroups() != null) {
            //     dto.setTestGroups(product.getTestGroups().stream()
            //         .map(testGroup -> new TestGroupResponseDTO(testGroup.getId(), testGroup.getTestGroupName(), testGroup.getStatus(), /* ... other fields ... */))
            //         .collect(Collectors.toList()));
            // }


            return dto;
        }).collect(Collectors.toList());
    }


    public Optional<Product> getProductById(Long id){ return productRepository.findById(id); }

    //public List<Product> findByTechnicianId(Long id){ return productRepository.findByTechnicianId(id); }

    //public List<Product> findByClientId(Long id) { return productRepository.findByClientId(id); }
}