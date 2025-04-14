package ru.promo_z.otpcodeprotectionservice.service;

public interface SmsService {

    void sendCode(String destination, String code);
}
