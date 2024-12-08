package com.mirhorodskiy.chat.web.controller;

import com.mirhorodskiy.chat.model.dto.ChatDto;
import com.mirhorodskiy.chat.model.dto.MessageDto;
import com.mirhorodskiy.chat.model.entity.Message;
import com.mirhorodskiy.chat.model.repository.ChatRepository;
import com.mirhorodskiy.chat.model.repository.MessageRepository;
import com.mirhorodskiy.chat.model.repository.UserRepository;
import com.mirhorodskiy.chat.web.service.ChatService;
import com.mirhorodskiy.chat.web.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

@RestController
@RequestMapping("api/chat")
@CrossOrigin(origins = "*",
        allowedHeaders = "*",
        exposedHeaders = "*",
        methods = {GET, POST, PUT, DELETE},
        maxAge = 3600)
public class ChatController {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageService messageService;
    private final ChatService chatService;

    public ChatController(MessageRepository messageRepository, UserRepository userRepository, ChatRepository chatRepository, MessageService messageService, ChatService chatService) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.messageService = messageService;
        this.chatService = chatService;
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageDto>> getMessages(@PathVariable Long chatId) {
        List<Message> messages = messageRepository.findByChatId(chatId);

        List<MessageDto> messageDtos = messages.stream()
                .map(message -> new MessageDto(
                        message.getChat().getId(),
                        message.getUser().getId(),
                        message.getText(),
                        message.getTimestamp()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(messageDtos);
    }

    @PostMapping("/message")
    public ResponseEntity<Void> receiveMessageFromExternalApi(@RequestBody MessageDto messageDto) {
        messageService.saveMessage(messageDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{projectId}/create")
    public ResponseEntity<ChatDto> createChat(@PathVariable Long projectId, @RequestBody ChatDto chatDto) {
        ChatDto createdChat = chatService.createChat(projectId, chatDto);
        return ResponseEntity.ok(createdChat);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDto> getChatById(@PathVariable Long chatId) {
        ChatDto chatDto = chatService.getChatById(chatId);
        return ResponseEntity.ok(chatDto);
    }

    @GetMapping
    public ResponseEntity<List<ChatDto>> getAllChats() {
        List<ChatDto> chats = chatService.getAllChats();
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatDto>> getChatsByUserId(@PathVariable Long userId) {
        List<ChatDto> chats = chatService.getChatsByUserId(userId);
        return ResponseEntity.ok(chats);
    }

}

