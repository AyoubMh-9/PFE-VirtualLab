package com.virtual.lab.backend.controller;

import com.virtual.lab.backend.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
public class ClientController {
    @GetMapping("/profile")
    public ResponseEntity<String> clientProfile() {
        return ResponseEntity.ok("Bienvenue Client !");
    }


}