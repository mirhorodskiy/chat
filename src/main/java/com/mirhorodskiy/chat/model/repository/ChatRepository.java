package com.mirhorodskiy.chat.model.repository;

import com.mirhorodskiy.chat.model.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
