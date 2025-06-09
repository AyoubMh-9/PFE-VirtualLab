package com.virtual.lab.backend.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User {
    // Attributs et méthodes spécifiques au Client
    /*
    public Client(String username, String email, String password) {
        super(username, email, password, "CLIENT");
    }

     */
}
