package com.virtual.lab.backend.model;

import jakarta.persistence.*;


import java.util.List;

@Entity
@DiscriminatorValue("TECHNICIAN")
public class Technician extends User {
    // Attributs et méthodes spécifiques au Technicien
    /*
    public Technician(String username, String email, String password) {
        super(username, email, password, "TECHNICIAN");
    }

     */

}
