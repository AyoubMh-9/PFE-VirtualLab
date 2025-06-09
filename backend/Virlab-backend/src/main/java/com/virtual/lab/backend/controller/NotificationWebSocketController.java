package com.virtual.lab.backend.controller;

import com.virtual.lab.backend.dto.NotificationDto;
import com.virtual.lab.backend.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Handles WebSocket messages sent to the /app/notification endpoint
     * Routes the message to the appropriate user's queue
     */
    @MessageMapping("/notification")
    public void processNotification(@Payload NotificationDto notification) {
        // Convert and send the message to the specific user's queue
        messagingTemplate.convertAndSendToUser(
                notification.getRecipient(),
                "/queue/notifications",
                notification
        );
    }

    /**
     * Sends notification to a specific user
     * @param username the recipient's username
     * @param notification the notification to send
     */
    public void sendNotificationToUser(String username, NotificationDto notification) {
        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/notifications",
                notification
        );
    }

    /**
     * Broadcasts a message to all subscribers of a product topic
     * @param productId the product identifier
     * @param message the message to broadcast
     */
    public void broadcastToProduct(Long productId, Message message) {
        messagingTemplate.convertAndSend(
                "/topic/product." + productId,
                message
        );
    }
}