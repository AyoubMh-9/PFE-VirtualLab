package com.virtual.lab.backend.config;

import com.virtual.lab.backend.model.*;
import com.virtual.lab.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String email = "admin@virtual-lab.com";
            if (userRepository.findByEmail(email).isEmpty()) {
                User admin = new User();
                admin.setUsername("Ayoub Mouhib");
                admin.setEmail(email);
                admin.setPassword(passwordEncoder.encode("Ayoub@123")); // encodé automatiquement
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("✅ Admin créé avec succès !");
            } else {
                System.out.println("ℹ️ Admin déjà existant.");
            }
        };
    }
}
