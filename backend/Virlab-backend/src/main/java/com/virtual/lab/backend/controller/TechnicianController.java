package com.virtual.lab.backend.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/technician")
public class TechnicianController {
    @GetMapping("/test")
    @PreAuthorize("hasRole('TECHNICIEN')")
    public ResponseEntity<String> clientProfile() {
        return ResponseEntity.ok("Bienvenue Tech !");
    }


}
