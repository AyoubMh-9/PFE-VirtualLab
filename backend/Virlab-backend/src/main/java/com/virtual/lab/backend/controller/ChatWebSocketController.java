package com.virtual.lab.backend.controller;

import com.virtual.lab.backend.dto.MessageDto;
import com.virtual.lab.backend.model.Message;
import com.virtual.lab.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @Autowired
    private MessageService messageService;

    /**
     * Handles messages sent to a specific product chat
     * @param productId the product identifier
     * @param messageDto the message data
     * @return the saved message that will be broadcast back to clients
     */
    @MessageMapping("/chat.product.{productId}")
    public Message sendProductMessage(
            @DestinationVariable Long productId,
            @Payload MessageDto messageDto) {

        // Process and store the message
        Message savedMessage = messageService.processAndSaveMessage(
                messageDto.getContent(),
                messageDto.getSenderId(),
                productId,
                messageDto.getReceiverId()
        );

        return savedMessage;
    }
}