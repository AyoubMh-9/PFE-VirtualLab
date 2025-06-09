package com.virtual.lab.backend.dto;

public class MessageResponseDto {
    private Long id;
    private String content;
    private String timestamp;
    private String senderUsername;
    private String senderRole;
    private String receiverUsername;
    private String productName;
    private boolean read;

    public MessageResponseDto() {}

    public MessageResponseDto(Long id, String content, String timestamp, String senderUsername, String senderRole, String receiverUsername, String productName, boolean read ) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.senderUsername = senderUsername;
        this.senderRole = senderRole;
        this.receiverUsername = receiverUsername;
        this.productName = productName;
        this.read = read;
    }

    // getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public String getSenderUsername() {
        return senderUsername;
    }
    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }
    public String getSenderRole() {
        return senderRole;
    }
    public void setSenderRole(String senderRole) {
        this.senderRole = senderRole;
    }
    public String getReceiverUsername() {
        return receiverUsername;
    }
    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public boolean isRead() {
        return read;
    }
    public void setRead(boolean read) {
        this.read = read;
    }

}
