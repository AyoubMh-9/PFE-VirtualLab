package com.virtual.lab.backend.dto;


public class NotificationResponse {

    private Long id;
    private String message;
    private boolean read;
    private String createdAt;
    private String recipientUsername;
    private String productName;

    public NotificationResponse() {}

    public NotificationResponse(Long id, String message, boolean read, String createdAt, String recipientUsername, String productName) {
        this.id = id;
        this.message = message;
        this.read = read;
        this.createdAt = createdAt;
        this.recipientUsername = recipientUsername;
        this.productName = productName;
    }

    // Getters et setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
