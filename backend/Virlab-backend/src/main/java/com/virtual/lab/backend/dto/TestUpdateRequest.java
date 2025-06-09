package com.virtual.lab.backend.dto;


import com.virtual.lab.backend.model.TestStatus;

import java.time.LocalDateTime;

public class TestUpdateRequest {
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private Long technicienId;

    // Getters et Setters
    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    // ... (autres getters/setters)

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }
    /*
    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    */
    public Long getTechnicienId() {
        return technicienId;
    }

    public void setTechnicienId(Long technicienId) {
        this.technicienId = technicienId;
    }
}
