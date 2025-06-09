package com.virtual.lab.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@DiscriminatorColumn(name = "dtype")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;

    //@JsonIgnore
    @Column(nullable = false, updatable = false)
    private String password;

    // La colonne discriminante qui différencie les types d'utilisateurs
    //@Column(name = "dtype", insertable = false, updatable = false)
    @Column(insertable=false, updatable=false)
    private String dtype; // ADMIN, CLIENT, TECHNICIEN (peut être une valeur utilisée pour l'implémentation d'héritage)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "uploader", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<UploadedFile> uploadedFiles;



    // Constructeur par défaut
    public User() {}



    // Constructeur avec paramètres
    public User(String username, String email, String password, Role role, List<UploadedFile> uploadedFiles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.uploadedFiles = uploadedFiles;
        //this.dtype = dtype;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    /*
    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

     */

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    public List<UploadedFile> getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(List<UploadedFile> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                //", dtype='" + dtype + '\'' +
                '}';
    }
}
