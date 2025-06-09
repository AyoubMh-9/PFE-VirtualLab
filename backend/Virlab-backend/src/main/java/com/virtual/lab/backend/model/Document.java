package com.virtual.lab.backend.model;

import jakarta.persistence.*;

@Entity
public class Document {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String nomFichier;
        private String type;

        @ManyToOne
        private User uploader;



}
