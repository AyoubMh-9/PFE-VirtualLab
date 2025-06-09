package com.virtual.lab.backend.model;

import com.virtual.lab.backend.model.User;
import com.virtual.lab.backend.model.Product;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "uploaded_files")
public class UploadedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "uploader_id")
    private User uploader;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Product project;

    private LocalDateTime uploadTime;

    public UploadedFile(){}

    public UploadedFile(Long id, String fileName, String fileType, String filePath, User uploader, Product project, LocalDateTime uploadTime) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.uploader = uploader;
        this.project = project;
        this.uploadTime = uploadTime;
    }

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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
    }

    public Product getProject() {
        return project;
    }

    public void setProject(Product project) {
        this.project = project;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }
}
