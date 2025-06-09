package com.virtual.lab.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductCreationRequest {

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String nomProduct;

    //@NotNull(message = "L'ID client est obligatoire")
    private Long clientId;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;


    private String accessCode;

    // Optionnel : si vous voulez aussi pouvoir associer un technicien/admin à la création
    private Long technicienId;
    private Long adminId;

    // Constructeurs
    public ProductCreationRequest() {
    }

    public ProductCreationRequest(String nomProduct, Long clientId, String description, String accessCode) {
        this.nomProduct = nomProduct;
        this.clientId = clientId;
        this.description = description;
        this.accessCode = accessCode;
    }

    // Getters et Setters
    public String getNomProduct() {
        return nomProduct;
    }

    public void setNomProduct(String nomProduct) {
        this.nomProduct = nomProduct;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTechnicienId() {
        return technicienId;
    }

    public void setTechnicienId(Long technicienId) {
        this.technicienId = technicienId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
}