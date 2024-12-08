package com.mirhorodskiy.chat.web.service;

import com.mirhorodskiy.chat.model.dto.ChatDto;
import com.mirhorodskiy.chat.model.dto.MessageDto;
import com.mirhorodskiy.chat.model.entity.Chat;
import com.mirhorodskiy.chat.model.entity.Message;
import com.mirhorodskiy.chat.model.entity.Project;
import com.mirhorodskiy.chat.model.entity.User;
import com.mirhorodskiy.chat.model.repository.ChatRepository;
import com.mirhorodskiy.chat.model.repository.MessageRepository;
import com.mirhorodskiy.chat.model.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final ProjectRepository projectRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository, ProjectRepository projectRepository, MessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.projectRepository = projectRepository;
        this.messageRepository = messageRepository;
    }

    // Створення чату
    public ChatDto createChat(Long projectId, ChatDto chatDto) {
        // Перевірка чи існує проект за переданим ID
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Створення нового чату
        Chat chat = new Chat();
        chat.setName(chatDto.getName());
        chat.setProject(project);
        chatRepository.save(chat);

        // Повернення даних чату
        return new ChatDto(chat.getId(), chat.getName(), chat.getProject().getId());
    }

    // Отримання всіх повідомлень чату
    public List<MessageDto> getMessages(Long chatId) {
        List<Message> messages = messageRepository.findByChatId(chatId);

        return messages.stream()
                .map(message -> new MessageDto(
                        message.getChat().getId(),
                        message.getUser().getId(),
                        message.getText(),
                        message.getTimestamp()
                ))
                .collect(Collectors.toList());
    }


}
