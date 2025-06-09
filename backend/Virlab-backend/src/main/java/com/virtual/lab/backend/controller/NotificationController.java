package com.virtual.lab.backend.controller;

import com.virtual.lab.backend.dto.NotificationRequest;
import com.virtual.lab.backend.dto.NotificationResponse;
import com.virtual.lab.backend.service.MessageService;
import com.virtual.lab.backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
        messageService.sendNotificationToUser(request.getUsername(), request.getMessage());
        notificationService.createNotification(request.getMessage(), request.getUserId(), request.getProductId());
        return ResponseEntity.ok("Notification envoyée !");
    }

    @Autowired
    private MessageService messageService;

    @MessageMapping("/notify")
    @SendTo("/topic/notification")
    public Map<String, String> sendNotification(Map<String, String> message) {
        String msg = message.get("message");
        System.out.println("Message reçu du client : " + msg);
        return Map.of("message", "Notification reçue : " + msg);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUser(userId));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}

