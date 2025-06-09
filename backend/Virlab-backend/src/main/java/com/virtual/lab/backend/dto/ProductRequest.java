package com.virtual.lab.backend.dto;

public class ProductRequest {
    private String name;
    private String description;

    private Long technicianId; // Obligatoire (affecté par l'admin)
    private Long clientId;     // Optionnel (peut être null)

    // Getters et Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
