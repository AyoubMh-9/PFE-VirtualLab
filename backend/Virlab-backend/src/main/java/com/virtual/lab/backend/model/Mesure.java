package com.virtual.lab.backend.model;


import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Mesure implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Column(nullable = false, unique = true)
    private String type;
    
}
