package com.virtual.lab.backend.dto;

import java.time.LocalDateTime;

public class MessageDto {
    private String content;
    private Long senderId;
    private Long receiverId;
    private Long productId;
    private LocalDateTime timestamp;

    // Constructors
    public MessageDto() {
        this.timestamp = LocalDateTime.now();
    }

    public MessageDto(String content, Long senderId, Long receiverId, Long productId) {
        this.content = content;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.productId = productId;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}