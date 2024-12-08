package com.mirhorodskiy.chat.web.service;

import com.mirhorodskiy.chat.model.dto.AuthenticationRequest;
import com.mirhorodskiy.chat.model.dto.SignUpDto;
import com.mirhorodskiy.chat.model.entity.Department;
import com.mirhorodskiy.chat.model.entity.User;
import com.mirhorodskiy.chat.model.enums.Role;
import com.mirhorodskiy.chat.model.repository.DepartmentRepository;
import com.mirhorodskiy.chat.model.repository.UserRepository;
import com.mirhorodskiy.chat.web.error.AuthenticationException;
import com.mirhorodskiy.chat.web.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public void register(SignUpDto signUpDto, String token) {

        Role currentUserRole = getCurrentUserRole(token);

        if (currentUserRole != Role.ADMIN && currentUserRole != Role.MANAGER ) {
            throw new AuthenticationException("Only ADMIN and Manager can register users", HttpStatus.FORBIDDEN);
        }

        if (signUpDto.getRole() == null) {
            signUpDto.setRole(Role.USER); // За замовчуванням - USER
        } else if (signUpDto.getRole() != Role.USER && signUpDto.getRole() != Role.MANAGER) {
            throw new AuthenticationException("ADMIN can only register MANAGER or USER roles", HttpStatus.FORBIDDEN);
        }

        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            throw new AuthenticationException("Username is already exists", HttpStatus.FORBIDDEN);
        }
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new AuthenticationException("Email is already exists", HttpStatus.FORBIDDEN);
        }

        Department department = departmentRepository.findDepartmentByName(signUpDto.getDepartment())
                .orElseThrow(() -> new AuthenticationException("Department not found", HttpStatus.FORBIDDEN));

        User user = User.builder()
                .username(signUpDto.getUsername())
                .email(signUpDto.getEmail())
                .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .password(passwordEncoder.encode("123456"))
                .department(department)
                .position(signUpDto.getPosition())
                .role(signUpDto.getRole())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    private Role getCurrentUserRole(String token) {
        return userRepository.findByEmail(getEmail(token))
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"))
                .getRole();
    }

    private String getEmailByToken(String token) {
        return jwtTokenProvider.getEmail(token);
    }

    private Role getRoleFromRegister(SignUpDto signUpDto) {
        if (signUpDto.getRole() != null)
            return signUpDto.getRole();
        return Role.USER;
    }

    public String login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
        return jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());
    }

    public Role getRole(String token) {
        return userRepository.findByEmail(getEmail(token))
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"))
                .getRole();
    }

    private String getEmail(String token) {
        return jwtTokenProvider.getEmail(token);
    }

    public boolean validateToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtTokenProvider.validateToken(token);
    }
}
