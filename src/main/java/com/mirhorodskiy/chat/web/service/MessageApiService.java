package com.mirhorodskiy.chat.web.service;

import com.mirhorodskiy.chat.model.dto.MessageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MessageApiService {

    private final RestTemplate restTemplate;
    private final MessageService messageService;

    @Value("${api.google.key}")
    private String apiKey;

    private final String apiUrlTemplate = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=%s";

    public MessageApiService(RestTemplate restTemplate, MessageService messageService) {
        this.restTemplate = restTemplate;
        this.messageService = messageService;
    }

    /**
     * Відправляє запит на зовнішнє API для аналізу тексту
     */
    public String sendTestMessage(String combinedText) {
        // Створюємо запит з комбінованим текстом усіх повідомлень
        List<Map<String, Object>> contents = List.of(
                Map.of("parts", List.of(Map.of("text", combinedText)))
        );

        Map<String, Object> requestPayload = Map.of("contents", contents);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestPayload, headers);

        String apiUrl = String.format(apiUrlTemplate, apiKey);

        try {
            // Відправляємо POST-запит
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("API responded with an error: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error while sending request: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send data to external API", e);
        }
    }

    /**
     * Викликає sendTestMessage для тестування API
     * Об'єднуємо всі повідомлення в одну строку з додаванням інформації про користувача та дату
     */
    public String testSendAllMessagesAsText(Long chatId) {
        // Отримуємо список повідомлень із чату
        List<MessageDto> messages = messageService.getMessagesByChatId(chatId);

        if (messages.isEmpty()) {
            throw new RuntimeException("No messages found for the chat with ID: " + chatId);
        }

        // Додаємо промпт на початку
        String prompt = "I am sending you chat messages, count how many messages each user has written in the chat. Also say hello in the beginning.";

        // Формуємо комбінований текст з усіх повідомлень
        String combinedText = messages.stream()
                .map(message -> String.format("User %d wrote at %s: %s", message.getSenderId(), message.getCreatedAt(), message.getText()))
                .collect(Collectors.joining(" ")); // Об'єднуємо текст повідомлень через пробіл

        // Додаємо промпт до початку
        combinedText = prompt + combinedText;

        // Викликаємо метод sendTestMessage для відправки комбінованого тексту
        return sendTestMessage(combinedText);
    }
}
