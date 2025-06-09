package com.virtual.lab.backend.mapper;

import com.virtual.lab.backend.dto.UserResponseDTO;
import com.virtual.lab.backend.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDTO toDto(User user) { // Or Client client, Technician technician
        if (user == null) {
            return null;
        }
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail()); // Add other relevant fields
        return dto;
    }
}

