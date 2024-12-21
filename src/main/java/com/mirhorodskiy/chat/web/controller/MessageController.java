package com.mirhorodskiy.chat.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mirhorodskiy.chat.web.service.MessageApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageApiService messageApiService;

    public MessageController(MessageApiService messageApiService) {
        this.messageApiService = messageApiService;
    }

//    @GetMapping("/chat/{chatId}/summary")
//    public ResponseEntity<String> getChatMessagesSummary(@PathVariable Long chatId) {
//        try {
//            String summary = messageApiService.getChatMessagesSummary(chatId);
//            return ResponseEntity.ok(summary);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

//    @GetMapping("/sendTestHello")
//    public ResponseEntity<String> sendTestHelloMessage() {
//        // Викликаємо метод для тесту
//        String response = messageApiService.testSendHelloMessage();
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/sendAllMessagesAsText/{chatId}")
    public ResponseEntity<String> sendAllMessagesAsText(@PathVariable Long chatId) {
        // Викликаємо метод для тесту
        String response = messageApiService.testSendAllMessagesAsText(chatId);
        return ResponseEntity.ok(response);
    }
}

