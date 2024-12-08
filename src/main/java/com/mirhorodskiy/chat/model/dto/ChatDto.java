package com.mirhorodskiy.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {
    private Long id;
    private String name;
    private Long projectId;
    private List<Long> participantIds;
}
