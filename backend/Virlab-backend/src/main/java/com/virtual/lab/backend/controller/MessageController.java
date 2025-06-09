package com.virtual.lab.backend.controller;

import com.virtual.lab.backend.dto.MessageDto;
import com.virtual.lab.backend.dto.MessageResponseDto;
import com.virtual.lab.backend.dto.MessageResponseDto;
import com.virtual.lab.backend.model.Message;
import com.virtual.lab.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * Send a new message
     * @param messageDto the message data
     * @return the saved message
     */
    @PostMapping
    public ResponseEntity<MessageResponseDto> sendMessage(@RequestBody MessageDto messageDto) {
        Message message = messageService.processAndSaveMessage(
                messageDto.getContent(),
                messageDto.getSenderId(),
                messageDto.getProductId(),
                messageDto.getReceiverId()
        );
        MessageResponseDto responseDto = messageService.mapMessageToDto(message);
        return ResponseEntity.ok(responseDto);
    }


    /**
     * Get all messages for a specific product
     * @param productId the product identifier
     * @return list of messages
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<MessageResponseDto>> getMessagesByProduct(
            @PathVariable Long productId,
            @RequestParam Long userId) {

        List<Message> messages = messageService.getMessagesByProduct(productId, userId);
        List<MessageResponseDto> dtos = messages.stream()
                .map(messageService::mapMessageToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }


    /**
     * Get conversation between two users for a specific product
     * @param userId1 first user identifier
     * @param userId2 second user identifier
     * @param productId the product identifier
     * @return list of messages
     */
    @GetMapping("/conversation")
    public ResponseEntity<List<Message>> getConversation(
            @RequestParam Long userId1,
            @RequestParam Long userId2,
            @RequestParam Long productId) {
        return ResponseEntity.ok(messageService.getConversation(userId1, userId2, productId));
    }

    /**
     * Mark a message as read
     * @param messageId the message identifier
     * @return the updated message
     */
    @PutMapping("/{messageId}/read")
    public ResponseEntity<Message> markAsRead(@PathVariable Long messageId) {
        return ResponseEntity.ok(messageService.markAsRead(messageId));
    }

    /**
     * Count unread messages for a user
     * @param userId the user identifier
     * @return count of unread messages
     */
    @GetMapping("/unread/count")
    public ResponseEntity<Map<String, Long>> countUnreadMessages(@RequestParam Long userId) {
        Long count = messageService.countUnreadMessages(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }
}