package com.virtual.lab.backend.dto;

import com.virtual.lab.backend.model.UploadedFile;


import java.time.LocalDateTime;

public class UploadedFileDTO {
    private Long id;
    private String fileName;
    private String fileType;
    private LocalDateTime uploadTime;

    private String uploaderUsername;  // juste le nom de l'uploader
    private Long uploaderId;
    private String projectName;       // juste le nom du projet
    private Long projectId;

    // Constructeur, getters, setters
    public UploadedFileDTO(Long id, String fileName, String fileType, LocalDateTime uploadTime,
                           String uploaderUsername, Long uploaderId, String projectName, Long projectId) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.uploadTime = uploadTime;
        this.uploaderUsername = uploaderUsername;
        this.uploaderId = uploaderId;
        this.projectName = projectName;
        this.projectId = projectId;
    }

    public UploadedFileDTO(UploadedFile file) {
        this.id = file.getId();
        this.fileName = file.getFileName();
        this.fileType = file.getFileType();
        this.uploadTime = file.getUploadTime();
        this.uploaderUsername = file.getUploader().getUsername();
        this.uploaderId = file.getUploader().getId();
        this.projectName = file.getProject() != null ? file.getProject().getNomProduct() : null;
        this.projectId = file.getProject() != null  ? file.getProject().getId() : null;
    }

    // getters & setters...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUploaderUsername() {
        return uploaderUsername;
    }

    public void setUploaderUsername(String uploaderUsername) {
        this.uploaderUsername = uploaderUsername;
    }

    public Long getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(Long uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getProjectId(){
        return projectId;
    }

    public void setProjectId(Long projectId){
        this.projectId = projectId;
    }
}
