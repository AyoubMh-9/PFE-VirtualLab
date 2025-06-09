package com.virtual.lab.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product") // Spécifie explicitement le nom de table
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_product") // Correspond au nom de colonne en base
    private String nomProduct;

    @ManyToOne // Relation Many-to-One plus appropriée
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @JsonIgnore
    private Client client;

    @ManyToOne
    @JoinColumn(name = "technicien_id", referencedColumnName = "id")
    //@JsonIgnore
    private Technician technician;

    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    @JsonIgnore
    private Admin admin;

    private String accessCode; // / pour que le client rejoigne le projet

    private boolean accessCodeUsed = false;

    // Renamed for clarity: this is the calculated status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestStatus calculatedStatus = TestStatus.EN_ATTENTE; // Default initial status

    // NEW: Calculated progress for the product
    private Double calculatedProgress = 0.0; // Default initial progress



    private LocalDateTime createdAt;

    private String description;

    @OneToMany(mappedBy = "project")
    private List<UploadedFile> files;

    // --- New: One-to-Many relationship with TestGroup ---
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TestGroup> testGroups = new HashSet<>(); // NEW: Change to Set

    // Constructeurs
    public Product() {}

    public Product(String nomProduct, Client client, String description, List<UploadedFile> files, TestStatus status) {
        this.nomProduct = nomProduct;
        this.client = client;
        this.description = description;
        this.files = files;
        //this.status = status;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public String getNomProduct() {
        return nomProduct;
    }

    public void setNomProduct(String nomProduct) {
        this.nomProduct = nomProduct;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Technician getTechnician() {
        return technician;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public boolean isAccessCodeUsed() {
        return accessCodeUsed;
    }
    public void setAccessCodeUsed(boolean accessCodeUsed) {
        this.accessCodeUsed = accessCodeUsed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public List<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadedFile> files) {
        this.files = files;
    }
/*
    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }

 */
    public TestStatus getCalculatedStatus() {
        return calculatedStatus;
    }
    public void setCalculatedStatus(TestStatus calculatedStatus) {
        this.calculatedStatus = calculatedStatus;
    }
    public Double getCalculatedProgress() {
        return calculatedProgress;
    }
    public void setCalculatedProgress(Double calculatedProgress) {
        this.calculatedProgress = calculatedProgress;
    }


    public Set<TestGroup> getTestGroups() {
        return testGroups;
    }

    public void setTestGroups(Set<TestGroup> testGroups) {
        this.testGroups = testGroups;
    }

    // Helper methods for managing testGroups to ensure bidirectional link
    public void addTestGroup(TestGroup group) {
        this.testGroups.add(group);
        group.setProduct(this);
    }

    public void removeTestGroup(TestGroup group) {
        this.testGroups.remove(group);
        group.setProduct(null);
    }

    // NEW: Method to recalculate status and progress for this product
    // @PreUpdate // Can be used, but external service call is often more reliable for cascade
    public void recalculateStatusAndProgress() {
        if (this.testGroups == null || this.testGroups.isEmpty()) {
            this.setCalculatedStatus(TestStatus.EN_ATTENTE);
            this.setCalculatedProgress(0.0);
            return;
        }

        // Calculate Project Progress based on total tests
        long totalTestsInProduct = 0;
        long completedTestsInProduct = 0;

        for (TestGroup group : this.testGroups) {
            totalTestsInProduct += group.getTests().size();
            completedTestsInProduct += group.getTests().stream()
                    .filter(test -> test.getStatus() == TestStatus.RÉUSSI || test.getStatus() == TestStatus.ÉCHOUÉ)
                    .count();
        }

        this.setCalculatedProgress((double) completedTestsInProduct / totalTestsInProduct * 100.0);


        // Determine Project Status (Hierarchy of Severity)
        if (this.testGroups.stream().anyMatch(group -> group.getCalculatedStatus() == TestStatus.ÉCHOUÉ)) {
            this.setCalculatedStatus(TestStatus.ÉCHOUÉ);
        } else if (this.testGroups.stream().anyMatch(group -> group.getCalculatedStatus() == TestStatus.EN_COURS)) {
            this.setCalculatedStatus(TestStatus.EN_COURS);
        } else if (this.testGroups.stream().anyMatch(group -> group.getCalculatedStatus() == TestStatus.EN_ATTENTE)) {
            this.setCalculatedStatus(TestStatus.EN_ATTENTE);
        } else { // All test groups must be TERMINÉ
            this.setCalculatedStatus(TestStatus.TERMINÉ);
        }
    }

}