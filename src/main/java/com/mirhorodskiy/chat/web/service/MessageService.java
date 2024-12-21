package com.mirhorodskiy.chat.web.service;

import com.mirhorodskiy.chat.model.dto.MessageDto;
import com.mirhorodskiy.chat.model.entity.Chat;
import com.mirhorodskiy.chat.model.entity.Message;
import com.mirhorodskiy.chat.model.entity.User;
import com.mirhorodskiy.chat.model.repository.ChatRepository;
import com.mirhorodskiy.chat.model.repository.MessageRepository;
import com.mirhorodskiy.chat.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    public MessageDto saveMessage(MessageDto messageDto) {
        User sender = userRepository.findById(messageDto.getSenderId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Chat chat = chatRepository.findById(messageDto.getChatId())
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        Message message = new Message();
        message.setText(messageDto.getText());
        message.setUser(sender);
        message.setChat(chat);
        message.setTimestamp(messageDto.getCreatedAt());

        Message savedMessage = messageRepository.save(message);
        return new MessageDto(
                savedMessage.getChat().getId(),
                savedMessage.getUser().getId(),
                savedMessage.getUser().getFirstName(), // Додано
                savedMessage.getUser().getLastName(),  // Додано
                savedMessage.getText(),
                savedMessage.getTimestamp()
        );
    }

    public List<MessageDto> getMessagesByChatId(Long chatId) {
        return messageRepository.findByChatId(chatId).stream()
                .map(message -> new MessageDto(
                        message.getChat().getId(),
                        message.getUser().getId(),
                        message.getUser().getFirstName(), // Додано
                        message.getUser().getLastName(),  // Додано
                        message.getText(),
                        message.getTimestamp()
                ))
                .toList();
    }

}

