package com.virtual.lab.backend.service;

import com.virtual.lab.backend.model.User;
import com.virtual.lab.backend.model.UserPrincipal;
import com.virtual.lab.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("=== DEBUG: Recherche utilisateur: {} ===", username);

        User user = repo.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("=== DEBUG: Utilisateur non trouvé: {} ===", username);
                    return new UsernameNotFoundException("Utilisateur non trouvé : " + username);
                });

        logger.info("=== DEBUG: Utilisateur trouvé ===");
        logger.info("ID: {}", user.getId());
        logger.info("Username: {}", user.getUsername());
        logger.info("Role: {}", user.getRole());
        logger.info("Password hash: {}", user.getPassword());
        logger.info("========================");

        return new UserPrincipal(user);
    }
}
