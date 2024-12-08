package com.mirhorodskiy.chat.web.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
    @Service
    public class EmailService {

        private final JavaMailSender mailSender;

        @Autowired
        public EmailService(JavaMailSender mailSender) {
            this.mailSender = mailSender;
        }

        public void sendEmail(String to, String subject, String text) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("leneyka777@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        }

        public void sendTestEmail(String toEmail) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("leneyka777@gmail.com");
            message.setTo(toEmail);
            message.setSubject("Test Email from Spring Boot");
            message.setText("This is a test email sent from the Spring Boot application.");

            mailSender.send(message);
        }
    }
