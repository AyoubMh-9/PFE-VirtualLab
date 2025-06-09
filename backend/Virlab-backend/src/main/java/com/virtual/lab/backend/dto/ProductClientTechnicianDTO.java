package com.virtual.lab.backend.dto;



public class ProductClientTechnicianDTO {
    private String nomProduct;
    private String clientUsername;
    private String description;
    private String technicianUsername;
    private String accessCode;

    // Constructeur
    public ProductClientTechnicianDTO(String nomProduct,  String accessCode, String clientUsername, String description, String technicianUsername) {
        this.nomProduct = nomProduct;
        this.clientUsername = clientUsername;
        this.description = description;
        this.technicianUsername = technicianUsername;
        this.accessCode = accessCode;
    }

    // Getters et setters
    public String getNomProduct() { return nomProduct; }
    public void setNomProduct(String nomProduct) { this.nomProduct = nomProduct; }

    public String getClientUsername() { return clientUsername; }
    public void setClientUsername(String clientUsername) { this.clientUsername = clientUsername; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTechnicianUsername() { return technicianUsername; }
    public void setTechnicianUsername(String technicianUsername) { this.technicianUsername = technicianUsername; }

    public String getAccessCode() { return accessCode; }
    public void setAccessCode(String accessCode) { this.accessCode = accessCode; }
}

