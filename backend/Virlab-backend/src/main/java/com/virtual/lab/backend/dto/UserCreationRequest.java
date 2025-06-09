package com.virtual.lab.backend.dto;

import jakarta.validation.constraints.*;

public class UserCreationRequest {

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 20, message = "Le nom d'utilisateur doit contenir entre 3 et 20 caractères")
    private String username;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).+$",
            message = "Le mot de passe doit contenir au moins 1 majuscule et 1 chiffre"
    )
    private String password;

    @NotBlank(message = "Le type d'utilisateur est obligatoire")
    @Pattern(regexp = "ADMIN|CLIENT|TECHNICIEN",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Le type doit être ADMIN, CLIENT ou TECHNICIEN")
    private String dtype;

    // Constructeurs
    public UserCreationRequest() {}

    public UserCreationRequest(String username, String email, String password, String dtype) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.dtype = dtype;
    }

    // Getters et Setters
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

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    @Override
    public String toString() {
        return "UserCreationRequest{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                ", dtype='" + dtype + '\'' +
                '}';
    }
}