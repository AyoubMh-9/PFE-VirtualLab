package com.virtual.lab.backend.repository;

import com.virtual.lab.backend.model.TestGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestGroupRepository extends JpaRepository<TestGroup, Long> {
    // Custom queries if needed, e.g., find by product and group number
    List<TestGroup> findByProductId(Long productId);
}