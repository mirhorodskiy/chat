package com.mirhorodskiy.chat.model.repository;

import com.mirhorodskiy.chat.model.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c FROM Chat c JOIN c.participants p WHERE p.id = :userId")
    List<Chat> findAllByUserId(@Param("userId") Long userId);
}
