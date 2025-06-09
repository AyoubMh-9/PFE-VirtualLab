package com.virtual.lab.backend.repository;

import com.virtual.lab.backend.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findFirstByOrderByIdAsc();

    Optional<Admin> findByEmail(String email);

    Optional<Admin> findByUsername(String username);
}
