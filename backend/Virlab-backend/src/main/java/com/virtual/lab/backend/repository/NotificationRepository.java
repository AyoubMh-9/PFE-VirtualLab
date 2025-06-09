package com.virtual.lab.backend.repository;

import com.virtual.lab.backend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification , Long> {
    List<Notification> findByRecipientIdOrderByCreatedAtDesc(Long userId);
}
