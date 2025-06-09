package com.virtual.lab.backend.dto;

public class NotificationDto {
    private String message;
    private String sender;
    private String recipient;
    private String timestamp;
    private String type = "MESSAGE"; // Default type

    // Constructors
    public NotificationDto() {
    }

    public NotificationDto(String message, String sender, String timestamp) {
        this.message = message;
        this.sender = sender;
        this.timestamp = timestamp;
    }

    public NotificationDto(String message, String sender, String recipient, String timestamp) {
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
        this.timestamp = timestamp;
    }

    public NotificationDto(String message, String sender, String recipient, String timestamp, String type) {
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
        this.timestamp = timestamp;
        this.type = type;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}