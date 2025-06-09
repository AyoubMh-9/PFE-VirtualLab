package com.virtual.lab.backend.service;

import com.virtual.lab.backend.model.UploadedFile;
import com.virtual.lab.backend.model.Product;
import com.virtual.lab.backend.model.User;
import com.virtual.lab.backend.repository.UploadedFileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ByteArrayResource;
import java.nio.charset.StandardCharsets;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.virtual.lab.backend.dto.UploadedFileDTO;

@Service
public class UploadedFileService {

    private final UploadedFileRepository uploadedFileRepository;

    public UploadedFileService(UploadedFileRepository uploadedFileRepository) {
        this.uploadedFileRepository = uploadedFileRepository;
    }

    // Chemin de stockage des fichiers (local)
    private final String uploadDir = "C:/Users/ayoub/OneDrive/Bureau/VirLab/backend/Virlab-backend/uploads/";

    public UploadedFileDTO saveFile(MultipartFile file, User uploader, Product project) throws IOException {
        // Générer un nom de fichier unique
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath = uploadDir + fileName;

        // Sauvegarde physique du fichier
        File dest = new File(filePath);
        dest.getParentFile().mkdirs(); // Créer le dossier si besoin
        file.transferTo(dest);

        // Créer l'objet UploadedFile
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFileName(file.getOriginalFilename());
        uploadedFile.setFilePath(filePath);
        uploadedFile.setFileType(file.getContentType());
        uploadedFile.setUploader(uploader);
        uploadedFile.setProject(project);
        uploadedFile.setUploadTime(LocalDateTime.now());

        //return uploadedFileRepository.save(uploadedFile);
        UploadedFileDTO fileDTO = convertToDTO(uploadedFileRepository.save(uploadedFile));
        return fileDTO;
    }

    public UploadedFileDTO convertToDTO(UploadedFile uploadedFile) {
        return new UploadedFileDTO(
                uploadedFile.getId(),
                uploadedFile.getFileName(),
                uploadedFile.getFileType(),
                uploadedFile.getUploadTime(),
                uploadedFile.getUploader().getUsername(),
                uploadedFile.getUploader().getId(),
                uploadedFile.getProject().getNomProduct(),
                uploadedFile.getProject().getId()
        );
    }


    public Optional<UploadedFile> getFile(Long id) {
        return uploadedFileRepository.findById(id);
    }

    public List<UploadedFile> getFilesByUploader(User uploader) {
        return uploadedFileRepository.findByUploader(uploader);
    }

    public List<UploadedFile> getFilesByProject(Product project) {
        return uploadedFileRepository.findByProject(project);
    }

    public List<UploadedFile> getFilesByUploaderAndProject(User uploader, Product project) {
        return uploadedFileRepository.findByUploaderAndProject(uploader, project);
    }

    public List<UploadedFile> getAllFiles(){
        return uploadedFileRepository.findAll();
    }




    public Long getAllReportsCountForClient(Long clientId) {
        return uploadedFileRepository.countAllUploadedFilesByClientId(clientId);
    }

    public ByteArrayResource generateReportsExport(Product project) {
        List<UploadedFile> files = uploadedFileRepository.findByProject(project); // Get all files for the project

        StringBuilder csvContent = new StringBuilder();
        // CSV Header
        csvContent.append("ID,File Name,File Type,Upload Time,Uploader Username\n");

        // CSV Data
        for (UploadedFile file : files) {
            csvContent.append(file.getId()).append(",");
            csvContent.append("\"").append(file.getFileName()).append("\","); // Quote if names can have commas
            csvContent.append(file.getFileType()).append(",");
            csvContent.append(file.getUploadTime()).append(",");
            csvContent.append(file.getUploader().getUsername()).append("\n"); // Assuming Uploader is a User object with username
        }

        byte[] bytes = csvContent.toString().getBytes(StandardCharsets.UTF_8);
        return new ByteArrayResource(bytes);
    }

}
