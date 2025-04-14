package ru.promo_z.otpcodeprotectionservice.service;

public interface EmailService {

    void sendEmail(String to, String subject, String body);
}
