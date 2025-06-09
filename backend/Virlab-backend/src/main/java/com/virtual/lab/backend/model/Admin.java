package com.virtual.lab.backend.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {
    // Attributs et méthodes spécifiques à l'Admin
    /*
    public Admin(String username, String email, String password) {
        super(username, email, password, "ADMIN");
    }
    */

}

