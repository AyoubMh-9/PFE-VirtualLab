package com.virtual.lab.backend.controller;


import com.virtual.lab.backend.dto.ProductCreationRequest;
import com.virtual.lab.backend.dto.ProductDTO;
import com.virtual.lab.backend.dto.ProductResponseDTO;
import com.virtual.lab.backend.model.*;
import com.virtual.lab.backend.repository.*;
import com.virtual.lab.backend.service.ProductService;
import com.virtual.lab.backend.service.TestService;
import com.virtual.lab.backend.util.QRCodeUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.virtual.lab.backend.service.UserService;
import com.virtual.lab.backend.security.SecurityUtil;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ClientRepository clientRepository;
    private  final TechnicianRepository technicianRepository;
    private final AdminRepository adminRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final TestService testService;

    public ProductController(ProductService productService, ClientRepository clientRepository, TechnicianRepository technicianRepository, AdminRepository adminRepository, ProductRepository productRepository, UserRepository userRepository, UserService userService, TestService testService) {
        this.productService = productService;
        this.clientRepository = clientRepository;
        this.technicianRepository = technicianRepository;
        this.adminRepository = adminRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;

        this.userService = userService;
        this.testService = testService;
    }
/*
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest request, Principal connectedUser) {
        Admin admin = adminRepository.findByEmail(connectedUser.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

        Technician technician = technicianRepository.findById(request.getTechnicianId())
                .orElseThrow(() -> new UsernameNotFoundException("Technician not found"));

        Product product = new Product();
        product.setNomProduct(request.getName());
        product.setDescription(request.getDescription());
        product.setAdmin(admin);
        product.setTechnician(technician);

        if (request.getClientId() != null) {
            Client client = clientRepository.findById(request.getClientId())
                    .orElseThrow(() -> new UsernameNotFoundException("Client not found"));
            product.setClient(client);
        } else {
            product.setAccessCode(UUID.randomUUID().toString());
        }

        return ResponseEntity.ok(productService.create(product));
    }


 */

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductCreationRequest productDTO) {
        try {
            // Récupérer le username (ou email) depuis le token JWT
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            Admin admin = adminRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("Admin non trouvé avec le username : " + username));

            Product product = new Product();

            // Associer l'admin actuel (via JWT)
            product.setAdmin(admin);

            // Associer le technicien (si fourni)
            Technician technician = null;
            if (productDTO.getTechnicienId() != null) {
                technician = technicianRepository.findById(productDTO.getTechnicienId())
                        .orElseThrow(() -> new EntityNotFoundException("Technicien non trouvé avec l'ID : " + productDTO.getTechnicienId()));
                product.setTechnician(technician);
            }

            // Associer le client (si fourni)
            if (productDTO.getClientId() != null) {
                Client client = clientRepository.findById(productDTO.getClientId())
                        .orElseThrow(() -> new EntityNotFoundException("Client non trouvé avec l'ID : " + productDTO.getClientId()));
                product.setClient(client);
            }

            product.setNomProduct(productDTO.getNomProduct());
            product.setAccessCode(productDTO.getAccessCode());
            product.setDescription(productDTO.getDescription());

            Product savedProduct = productService.create(product);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Produit créé avec succès");
            response.put("accessCode", savedProduct.getAccessCode());
            return ok(response);

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    /*
    @PostMapping("/join/{accessCode}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<?> joinProduct(@PathVariable String accessCode, @AuthenticationPrincipal UserDetails userDetails) {
        Product product = productRepository.findByAccessCode(accessCode)
                .orElseThrow(() -> new EntityNotFoundException("Produit non trouvé avec le code : " + accessCode));

        Client client = clientRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé"));

        product.setClient(client);
        productRepository.save(product);

        return ResponseEntity.ok("Client ajouté au produit");
    }

     */

    @PostMapping("/join/{accessCode}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<?> joinProductByAccessCode(@PathVariable String accessCode, Authentication authentication) {

        Product product = productRepository.findByAccessCode(accessCode)
                .orElseThrow(() -> new EntityNotFoundException("Produit non trouvé pour ce code"));

        if (product.isAccessCodeUsed()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Le code d'accès a déjà été utilisé.");
        }

        // Récupérer client connecté
        Client client = clientRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé"));

        // Associer le client au produit (ajouter dans la collection si ManyToMany)
        product.setClient(client);

        // Marquer le code comme utilisé
        product.setAccessCodeUsed(true);

        productRepository.save(product);

        return ok("Client ajouté au produit avec succès.");
    }

    @GetMapping("/qrcode/{accessCode}")
    @PreAuthorize("CLIENT")
    public ResponseEntity<byte[]> getQRCode(@PathVariable String accessCode) throws Exception {
        byte[] image = QRCodeUtil.generateQRCodeImage(accessCode, 250, 250);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }




    // CORRECT WAY: Perform DTO conversion within a transactional method
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        // Delegate conversion to service
        ProductResponseDTO productDTO = productService.getProductDtoById(id);
        return ok(productDTO);
    }


    @GetMapping("/client/getAll")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<ProductResponseDTO>> getAllProjectsForClient(Authentication authentication) {
        String username = authentication.getName();
        User clientUser = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Client non trouvé: " + username));

        List<ProductResponseDTO> productDTOs = productService.getAllProjectsForClient(clientUser.getId());
        return ok(productDTOs);
    }


    // Use ProductResponseDTO for return type
    @GetMapping("/getAll")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        // Delegate conversion to service
        List<ProductResponseDTO> productDTOs = productService.getAllProductsDto();
        System.out.println(productDTOs);
        return ok(productDTOs);
    }

    // Use ProductResponseDTO for return type
    @GetMapping("/technician/assigned-projects")
    @PreAuthorize("hasRole('TECHNICIAN')") // Assuming technician role
    public ResponseEntity<List<ProductResponseDTO>> getAssignedProjectsForTechnician(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));
        // Use the service method to get DTOs
        List<ProductResponseDTO> productDTOs = productService.findByTechnicianId(user.getId());
        return ok(productDTOs);
    }

    // Note: The @RequestBody Product updatedProject should ideally be a ProductUpdateRequestDTO
    // to control what fields can be updated by the client.
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> updateProject(@PathVariable Long id, @RequestBody Product updatedProject) {
        // Pass the entity to the service for update, then convert the saved entity to DTO
        ProductResponseDTO updatedProductDTO = productService.updateProduct(id, updatedProject);
        return ok(updatedProductDTO);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Existing count endpoints - these don't return ProductDTOs, so no change needed here.
    @GetMapping("/technician/{id}/active-projects/count")
    public ResponseEntity<Long> getActiveProjectCountByTechnician(@PathVariable Long id) {
        Long count = productRepository.countActiveProjectsByTechnician(id);
        return ok(count);
    }

    @GetMapping("/client/active-projects/count")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Long> getActiveProjectCountByClient(Authentication authentication){
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));

        Long count = productRepository.countActiveProjectsByClient(user.getId());
        return ResponseEntity.ok(count);

    }

    @GetMapping("/technician/{id}/no-active-projects/count")
    public ResponseEntity<Long> getNoActiveProjectCountByTechnician(@PathVariable Long id) {
        Long count = productRepository.countNoActiveProjectsByTechnician(id);
        return ok(count);
    }

    @GetMapping("/technician/{id}/done-active-projects/count")
    public ResponseEntity<Long> getDoneProjectCountByTechnician(@PathVariable Long id) {
        Long count = productRepository.countTermineeProjectsByTechnician(id);
        return ok(count);
    }


    @GetMapping("/client/tests/count/all")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Long> getAllTestsCountForClient(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));

        Long count = testService.getAllTestsCountForClient(user.getId()); // Pass client ID to service
        // Or, if using the alternative service method:
        // Long count = testService.getAllTestsCountForAuthenticatedClient(username);
        return ResponseEntity.ok(count);
    }




}