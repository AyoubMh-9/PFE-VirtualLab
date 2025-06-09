package com.virtual.lab.backend.repository;

import com.virtual.lab.backend.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface ClientRepository  extends JpaRepository<Client, Long> {
    Optional<Client> findByUsername(String username);

    Optional<Client> findByEmail(String email);
}
