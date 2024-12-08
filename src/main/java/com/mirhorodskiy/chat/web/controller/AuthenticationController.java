package com.mirhorodskiy.chat.web.controller;

import com.mirhorodskiy.chat.model.dto.AuthenticationRequest;
import com.mirhorodskiy.chat.model.dto.SignUpDto;
import com.mirhorodskiy.chat.model.enums.Role;
import com.mirhorodskiy.chat.web.error.AuthenticationException;
import com.mirhorodskiy.chat.web.service.AuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = "*",
        allowedHeaders = "*",
        exposedHeaders = "*",
        methods = {GET, POST, PUT, DELETE},
        maxAge = 3600)
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpDto signUpDto, HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        authenticationService.register(signUpDto, token);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User successfully registered");
        response.put("status", HttpStatus.CREATED.toString());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        String token = authenticationService.login(request);
        if (token == null) {
            throw new AuthenticationException("Invalid username/password combination", HttpStatus.UNAUTHORIZED);
        }

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("status", HttpStatus.OK.toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Successfully logged out");
        responseBody.put("status", HttpStatus.OK.toString());
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/role")
    public ResponseEntity<?> getRole(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println(token);
        Role role = authenticationService.getRole(token);

        Map<String, Object> response = new HashMap<>();
        response.put("role", role);
        response.put("status", HttpStatus.OK.toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        boolean isValid = authenticationService.validateToken(token);

        Map<String, Object> response = new HashMap<>();
        response.put("valid", isValid);
        response.put("status", HttpStatus.OK.toString());
        return ResponseEntity.ok(response);
    }
}