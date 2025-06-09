package com.virtual.lab.backend.dto;

import com.virtual.lab.backend.model.Client;
import com.virtual.lab.backend.model.Product;
import com.virtual.lab.backend.model.Technician;
import com.virtual.lab.backend.model.TestStatus;
import com.virtual.lab.backend.model.TestGroup; // Import TestGroup entity
import com.virtual.lab.backend.model.Test; // Import Test entity


import com.virtual.lab.backend.dto.TestGroupDTO; // Make sure this file exists
import com.virtual.lab.backend.dto.TestDto;     // Make sure this file exists

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDTO {

    private Long id;
    private String nomProduct;
    private ClientDTO client;
    private TechnicianDTO technician;
    private String description;
    private String accesscode;
    private TestStatus status;
    // NEW: Add a list of TestGroupDTOs
    private List<TestGroupDTO> testGroups;




    public ProductDTO(){}

    public ProductDTO(Product product){
        this.id = product.getId();
        this.nomProduct = product.getNomProduct();
        if(product.getClient() != null){
            this.client = new ClientDTO(product.getClient());
        }
        if(product.getTechnician() != null){
            this.technician = new TechnicianDTO(product.getTechnician());
        }
        this.description = product.getDescription();
        this.accesscode = product.getAccessCode();
        this.status = product.getCalculatedStatus();

        // NEW: Populate the list of TestGroupDTOs
        if (product.getTestGroups() != null && !product.getTestGroups().isEmpty()) {
            this.testGroups = product.getTestGroups().stream()
                    .map(group -> {
                        // Assume TestGroupDTO has a constructor accepting TestGroup entity
                        TestGroupDTO groupDto = new TestGroupDTO(group);
                        // Also populate tests within each group using top-level TestDTO
                        if (group.getTests() != null) {
                            groupDto.setTests(group.getTests().stream()
                                    .map(TestDto::new) // Assumes TestDTO has a constructor from Test entity
                                    .collect(Collectors.toList()));
                        } else {
                            groupDto.setTests(new ArrayList<>());
                        }
                        return groupDto;
                    })
                    .collect(Collectors.toList());
        } else {
            this.testGroups = new ArrayList<>();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomProduct() { return nomProduct; }
    public void setNomProduct(String nomProduct) { this.nomProduct = nomProduct; }
    public ClientDTO getClient() { return client; }
    public void setClient(ClientDTO client) { this.client = client; }
    public TechnicianDTO getTechnician() { return technician; }
    public void setTechnician(TechnicianDTO technician) { this.technician = technician; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAccesscode() { return accesscode; }
    public void setAccesscode(String accesscode) { this.accesscode = accesscode; }
    public TestStatus getStatus(){ return status; }
    public void setStatus(TestStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", nomProduct='" + nomProduct + '\'' +
                ", client=" + client + // This will call client.toString()
                ", technician=" + technician + // This will call technician.toString()
                ", description='" + description + '\'' +
                ", accesscode='" + accesscode + '\'' +
                ", status=" + status +
                ", testGroups=" + testGroups + // This will call testGroups.toString() (which in turn calls TestGroupDTO.toString() for each item)
                '}';
    }



    // NEW: Getter and Setter for testGroups
    public List<TestGroupDTO> getTestGroups() {
        return testGroups;
    }
    public void setTestGroups(List<TestGroupDTO> testGroups) {
        this.testGroups = testGroups;
    }


    public static class ClientDTO{
        private Long id;
        private String username;
        private String email;

        public ClientDTO(){}

        public ClientDTO(Client client){
            this.id = client.getId();
            this.username = client.getUsername();
            this.email = client.getEmail();
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class TechnicianDTO{
        private Long id;
        private String username;
        private String email;

        public TechnicianDTO(){}

        public TechnicianDTO(Technician technician){
            this.id = technician.getId();
            this.username = technician.getUsername();
            this.email = technician.getEmail();
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }


}