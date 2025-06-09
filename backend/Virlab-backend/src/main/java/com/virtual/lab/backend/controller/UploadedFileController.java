package com.virtual.lab.backend.controller;

import com.virtual.lab.backend.model.UploadedFile;
import com.virtual.lab.backend.model.User;
import com.virtual.lab.backend.model.Product;
import com.virtual.lab.backend.service.UploadedFileService;
import com.virtual.lab.backend.service.UserService;
import com.virtual.lab.backend.service.ProductService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.ByteArrayResource;

import org.springframework.http.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.virtual.lab.backend.dto.UploadedFileDTO;

@RestController
@RequestMapping("/api/files")
public class UploadedFileController {

    private final UploadedFileService uploadedFileService;
    private final UserService userService;
    private final ProductService productService;

    public UploadedFileController(UploadedFileService uploadedFileService, UserService userService, ProductService productService) {
        this.uploadedFileService = uploadedFileService;
        this.userService = userService;
        this.productService = productService;
    }

    // Upload d'un fichier
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("projectId") Long projectId,
                                        Authentication authentication) throws IOException {

        // Récupérer l'utilisateur authentifié depuis le token
        String username = authentication.getName();
        User uploader = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Product project = productService.getProductById(projectId)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        // Vérification des droits d'accès
        if (uploader.getRole().equals("CLIENT")) {
            if (!project.getClient().getId().equals(uploader.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès interdit à ce projet.");
            }
        } else if (uploader.getRole().equals("TECHNICIEN")) {
            // Si tu as un lien Technicien-Project, vérifie ici
            if (!project.getTechnician().equals(uploader)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès interdit à ce projet.");
            }
        }
        // Si ADMIN : accès autorisé à tout

        UploadedFileDTO savedFile = uploadedFileService.saveFile(file, uploader, project);

        return ResponseEntity.ok(savedFile);
    }

    // Télécharger un fichier par ID
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        UploadedFile file = uploadedFileService.getFile(id).orElseThrow(() -> new RuntimeException("File not found"));

        Resource resource = new FileSystemResource(file.getFilePath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(resource);
    }

    // Obtenir les fichiers par utilisateur
    @GetMapping("/user/{userId}")
    public List<UploadedFileDTO> getFilesByUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<UploadedFile> files = uploadedFileService.getFilesByUploader(user);
        return files.stream()
                .map(UploadedFileDTO::new)
                .collect(Collectors.toList());
    }




    // Obtenir les fichiers par projet
    @GetMapping("/project/{projectId}")
    public List<UploadedFileDTO> getFilesByProject(@PathVariable Long projectId) {
        Product project = productService.getProductById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        List<UploadedFile> files = uploadedFileService.getFilesByProject(project);
        return files.stream()
                .map(UploadedFileDTO::new)
                .collect(Collectors.toList());
    }


    // Obtenir les fichiers par utilisateur et projet
    @GetMapping("/user/{userId}/project/{projectId}")
    public List<UploadedFileDTO> getFilesByUserAndProject(@PathVariable Long userId, @PathVariable Long projectId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product project = productService.getProductById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        List<UploadedFile> files = uploadedFileService.getFilesByUploaderAndProject(user, project);
        return files.stream()
                .map(UploadedFileDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/client/project/{projectId}")
    public List<UploadedFileDTO> getFilesByClientAndProject(Authentication authentication, @PathVariable Long projectId) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));

        Product project = productService.getProductById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // If Product has a direct client association (ManyToOne):


        List<UploadedFile> files = uploadedFileService.getFilesByProject(project);
        return files.stream()
                .map(UploadedFileDTO::new)
                .collect(Collectors.toList());
    }

    // Récupérer tous les fichiers (pour admin)
    @GetMapping("/all")
    public List<UploadedFileDTO> getAllFiles() {
        List<UploadedFile> files = uploadedFileService.getAllFiles();
        return files.stream()
                .map(UploadedFileDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/client/reports/count/all")
    public ResponseEntity<Long> getAllReportsCountForClient(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));

        Long count = uploadedFileService.getAllReportsCountForClient(user.getId());
        return ResponseEntity.ok(count);
    }

    @GetMapping("/products/{projectId}/export-reports") // New endpoint for exporting
    @PreAuthorize("hasRole('CLIENT')") // Ensure only authenticated clients can access
    public ResponseEntity<Resource> exportProjectReports(@PathVariable Long projectId, Authentication authentication) throws IOException {
        String username = authentication.getName();
        User authenticatedUser = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found: " + username));

        // --- Access Control Check (CRUCIAL) ---
        // Verify that the authenticated client has access to this project
        Product project = productService.getProductById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));

        // Assuming Product has a ManyToOne relationship to a Client entity,
        // and Client entity has a ManyToOne to a User entity.
        if (project.getClient() == null || !project.getClient().getId().equals(authenticatedUser.getId())) {
            throw new EntityNotFoundException("Client does not have access to export reports for this project.");
        }
        // --- End Access Control Check ---

        // Generate the report file (e.g., CSV or Excel of file metadata)
        // This part would be in your UploadedFileService or a dedicated ReportGenerationService
        ByteArrayResource resource = uploadedFileService.generateReportsExport(project); // Implement this method

        String filename = "project_reports_" + project.getNomProduct().replaceAll("\\s+", "_") + ".csv"; // Or .xlsx
        String contentType = "text/csv"; // Or "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" for Excel

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }


}
