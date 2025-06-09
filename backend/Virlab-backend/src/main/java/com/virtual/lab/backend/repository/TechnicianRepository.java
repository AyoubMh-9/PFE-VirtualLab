package com.virtual.lab.backend.repository;

import com.virtual.lab.backend.model.Technician;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnicianRepository  extends JpaRepository<Technician, Long> {
}
