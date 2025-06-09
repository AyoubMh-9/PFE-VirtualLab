package com.virtual.lab.backend.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.virtual.lab.backend.model.User;

public class UserPrincipal implements UserDetails {
    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = "ROLE_" + user.getRole().name(); // Utilisez .name() si c'est un enum
        System.out.println("Authorities créées: " + role);
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // Retourne le hash BCrypt
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // ... autres méthodes inchangées
    public User getUser() {
        return this.user;
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // ou false selon la logique de ton projet
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // ou false selon la logique de ton projet
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // ou false selon la logique de ton projet
    }

    @Override
    public boolean isEnabled() {
        return true; // ou par exemple: return user.isEnabled();
    }

}