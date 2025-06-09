package com.virtual.lab.backend.dto;

import com.virtual.lab.backend.model.TestStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class TestCreationRequest {

    @NotNull(message = "La date de début est obligatoire")
    @FutureOrPresent(message = "La date doit être dans le futur ou présent")
    private LocalDateTime dateDebut;  // Utilisez camelCase partout

    @NotNull(message = "La date de fin est obligatoire")
    @Future(message = "La date doit être dans le futur")
    private LocalDateTime dateFin;


    @NotNull(message = "Le status est obligatoire")
    private TestStatus status;

    @NotNull(message = "L'ID produit est obligatoire")
    @Positive(message = "L'ID produit doit être positif")
    private Long productId;

    @NotNull(message = "L'ID technicien est obligatoire")
    @Positive(message = "L'ID technicien doit être positif")
    private Long technicienId;

    @NotNull(message = "L'ID client est obligatoire")
    @Positive(message = "L'ID client doit être positif")
    private Long clientId;


    public TestCreationRequest(){}

    public @NotNull(message = "La date de début est obligatoire") @FutureOrPresent(message = "La date doit être dans le futur ou présent") LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(@NotNull(message = "La date de début est obligatoire") @FutureOrPresent(message = "La date doit être dans le futur ou présent") LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public @NotNull(message = "La date de fin est obligatoire") @Future(message = "La date doit être dans le futur") LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(@NotNull(message = "La date de fin est obligatoire") @Future(message = "La date doit être dans le futur") LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public @NotNull(message = "Le statut est obligatoire") TestStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull(message = "Le statut est obligatoire") TestStatus status) {
        this.status = status;
    }

    public @NotNull(message = "L'ID produit est obligatoire") @Positive(message = "L'ID produit doit être positif") Long getProductId() {
        return productId;
    }

    public void setProductId(@NotNull(message = "L'ID produit est obligatoire") @Positive(message = "L'ID produit doit être positif") Long productId) {
        this.productId = productId;
    }

    public @NotNull(message = "L'ID technicien est obligatoire") @Positive(message = "L'ID technicien doit être positif") Long getTechnicienId() {
        return technicienId;
    }

    public void setTechnicienId(@NotNull(message = "L'ID technicien est obligatoire") @Positive(message = "L'ID technicien doit être positif") Long technicienId) {
        this.technicienId = technicienId;
    }

    public @NotNull(message = "L'ID client est obligatoire") @Positive(message = "L'ID client doit être positif") Long getClientId() {
        return clientId;
    }

    public void setClientId(@NotNull(message = "L'ID client est obligatoire") @Positive(message = "L'ID client doit être positif") Long clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "TestCreationRequest{" +
                "dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", status=" + status +
                ", productId=" + productId +
                ", technicienId=" + technicienId +
                ", clientId=" + clientId +
                '}';
    }
}
