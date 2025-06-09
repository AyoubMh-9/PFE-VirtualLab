package com.virtual.lab.backend.service;

import com.virtual.lab.backend.dto.NotificationDto;
import com.virtual.lab.backend.dto.NotificationResponse;
import com.virtual.lab.backend.model.Notification;
import com.virtual.lab.backend.model.Product;
import com.virtual.lab.backend.model.User;
import com.virtual.lab.backend.repository.NotificationRepository;
import com.virtual.lab.backend.repository.ProductRepository;
import com.virtual.lab.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public Notification createNotification(String msg, Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Notification notif = new Notification();
        notif.setMessage(msg);
        notif.setRecipient(user);
        notif.setProduct(product);
        return notificationRepository.save(notif);
    }

    public List<NotificationResponse> getNotificationsByUser(Long userId) {

        List<Notification> notifications = notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId);
        return notifications.stream()
                .map(n -> new NotificationResponse(
                        n.getId(),
                        n.getMessage(),
                        n.isRead(),
                        n.getCreatedAt().toString(), // ou formatter
                        n.getRecipient().getUsername(),
                        n.getProduct().getNomProduct()
                ))
                .toList();
    }

    public void markAsRead(Long id) {
        Notification notif = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notif.setRead(true);
        notificationRepository.save(notif);
    }
}

