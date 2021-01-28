package com.gmail.eugeneknysh21.blog.services;

public interface MailSenderService {
    void sendMessage(String emailTo, String subject, String text);
}
