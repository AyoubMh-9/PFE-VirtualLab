package com.virtual.lab.backend.repository;

import com.virtual.lab.backend.model.Role;
import com.virtual.lab.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    //void deleteUserById(long id);
    Optional<User> findUserById(long id);
    Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);
}

