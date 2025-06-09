package com.virtual.lab.backend.dto;

import com.virtual.lab.backend.model.TestStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email; // Include other relevant fields if needed
    // Add constructor, getters, setters if not using Lombok's @Data


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


