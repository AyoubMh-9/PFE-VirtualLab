package com.virtual.lab.backend.service;

import com.virtual.lab.backend.controller.NotificationWebSocketController;
import com.virtual.lab.backend.dto.NotificationDto;
import com.virtual.lab.backend.dto.MessageResponseDto;
import com.virtual.lab.backend.model.Message;
import com.virtual.lab.backend.model.Product;
import com.virtual.lab.backend.model.User;
import com.virtual.lab.backend.repository.MessageRepository;
import com.virtual.lab.backend.repository.ProductRepository;
import com.virtual.lab.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NotificationWebSocketController websocketController;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Send and save a message with sender, receiver, and product information
     * @param content the message content
     * @param senderId the sender identifier
     * @param productId the product identifier
     * @param receiverId the receiver identifier (optional)
     * @return the saved message
     */
    @Transactional
    public Message processAndSaveMessage(String content, Long senderId, Long productId, Long receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found with ID: " + senderId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // Validate access
        validateUserHasAccessToProduct(senderId, product);

        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setProduct(product);

        if (receiverId != null) {
            User receiver = userRepository.findById(receiverId)
                    .orElseThrow(() -> new RuntimeException("Receiver not found with ID: " + receiverId));
            message.setReceiver(receiver);
        }

        Message savedMessage = messageRepository.save(message);
        websocketController.broadcastToProduct(productId, savedMessage);

        if (message.getReceiver() != null) {
            String now = LocalDateTime.now().format(FORMATTER);
            NotificationDto notification = new NotificationDto(
                    "New message from " + sender.getUsername(),
                    sender.getUsername(),
                    message.getReceiver().getUsername(),
                    now
            );
            websocketController.sendNotificationToUser(message.getReceiver().getUsername(), notification);
        }

        return savedMessage;
    }

    public MessageResponseDto mapMessageToDto(Message message) {
        String senderRole = null;
        if (message.getSender() != null && message.getSender().getRole() != null) {
            senderRole = message.getSender().getRole().name();
        }

        return new MessageResponseDto(
                message.getId(),
                message.getContent(),
                message.getTimestamp() != null ? message.getTimestamp().toString() : null,
                message.getSender() != null ? message.getSender().getUsername() : null,
                senderRole,
                message.getReceiver() != null ? message.getReceiver().getUsername() : null,
                message.getProduct() != null ? message.getProduct().getNomProduct() : null,
                message.isRead()
        );

    }



    /**
     * Send a message with only sender and product information
     * @param content the message content
     * @param senderId the sender identifier
     * @param productId the product identifier
     * @return the saved message
     */
    @Transactional
    public Message sendMessage(String content, Long senderId, Long productId) {
        return processAndSaveMessage(content, senderId, productId, null);
    }

    /**
     * Get all messages for a specific product
     * @param productId the product identifier
     * @return list of messages
     */
    public List<Message> getMessagesByProduct(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        validateUserHasAccessToProduct(userId, product);
        return messageRepository.findByProductIdOrderByTimestampAsc(productId);
    }

    /**
     * Get conversation between two users for a specific product
     * @param userId1 first user identifier
     * @param userId2 second user identifier
     * @param productId the product identifier
     * @return list of messages
     */
    public List<Message> getConversation(Long userId1, Long userId2, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        validateUserHasAccessToProduct(userId1, product);
        validateUserHasAccessToProduct(userId2, product);
        List<Message> messages = messageRepository.findBySenderIdAndReceiverIdAndProductIdOrderByTimestampAsc(userId1, userId2, productId);
        messages.addAll(messageRepository.findBySenderIdAndReceiverIdAndProductIdOrderByTimestampAsc(userId2, userId1, productId));
        messages.sort((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp())); // Trier par timestamp
        return messages;
    }

    /**
     * Mark a message as read
     * @param messageId the message identifier
     * @return the updated message
     */
    @Transactional
    public Message markAsRead(Long messageId) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isPresent()) {
            Message message = messageOpt.get();
            message.setRead(true);
            return messageRepository.save(message);
        }
        throw new RuntimeException("Message not found with ID: " + messageId);
    }

    /**
     * Count unread messages for a user
     * @param userId the user identifier
     * @return count of unread messages
     */
    public Long countUnreadMessages(Long userId) {
        return messageRepository.countByReceiverIdAndIsRead(userId, false);
    }

    public void sendNotificationToUser(String username, String message) {
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", message);
    }

    private void validateUserHasAccessToProduct(Long userId, Product product) {
        boolean isClient = product.getClient() != null && product.getClient().getId().equals(userId);
        boolean isTechnician = product.getTechnician() != null && product.getTechnician().getId().equals(userId);
        boolean isAdmin = product.getAdmin() != null && product.getAdmin().getId().equals(userId);

        if (!isClient && !isTechnician && !isAdmin) {
            throw new RuntimeException("User is not authorized to access chat for this product");
        }
    }
}