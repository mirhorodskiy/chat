package com.mirhorodskiy.chat.model.repository;

import com.mirhorodskiy.chat.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Override
    Optional<Message> findById(Long aLong);

    List<Message> findByChatId(Long chatId);
}
