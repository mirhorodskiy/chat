package com.mirhorodskiy.chat.web.service;

import com.mirhorodskiy.chat.model.dto.UserDto;
import com.mirhorodskiy.chat.model.entity.User;
import com.mirhorodskiy.chat.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getDepartment().getName(),
                user.getPosition(),
                user.getRole().name()
        );
    }
}
