package com.mirhorodskiy.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private Long chatId;
    private Long senderId;
    private String text;
    private LocalDateTime createdAt;
}
