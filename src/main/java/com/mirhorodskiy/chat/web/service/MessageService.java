package com.mirhorodskiy.chat.web.service;

import com.mirhorodskiy.chat.model.dto.MessageDto;
import com.mirhorodskiy.chat.model.entity.Chat;
import com.mirhorodskiy.chat.model.entity.Message;
import com.mirhorodskiy.chat.model.entity.User;
import com.mirhorodskiy.chat.model.repository.ChatRepository;
import com.mirhorodskiy.chat.model.repository.MessageRepository;
import com.mirhorodskiy.chat.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    public void saveMessage(MessageDto messageDto) {
        // Знаходимо користувача за senderId
        User sender = userRepository.findById(messageDto.getSenderId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Знаходимо чат за chatId
        Chat chat = chatRepository.findById(messageDto.getChatId())
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        // Створюємо нове повідомлення
        Message message = new Message();
        message.setText(messageDto.getText());
        message.setUser(sender);
        message.setChat(chat);
        message.setTimestamp(messageDto.getCreatedAt());

        // Зберігаємо повідомлення в базі даних
        messageRepository.save(message);
    }
}

