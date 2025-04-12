package ru.promo_z.otpcodeprotectionservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.promo_z.otpcodeprotectionservice.service.TelegramService;

@Service
@Slf4j
public class TelegramServiceImpl implements TelegramService {

    @Value("${telegram.bot-token}")
    private String botToken;

    @Value("${telegram.chat-id}")
    private String chatId;

    private final RestTemplate restTemplate;

    @Autowired
    public TelegramServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void sendMessage(String message) {
        String url = String.format("https://api.telegram.org/bot%s/sendMessage", botToken);
        String requestUrl = String.format("%s?chat_id=%s&text=%s", url, chatId, message);

        String response = restTemplate.getForObject(requestUrl, String.class);
        log.info("Response from Telegram: {}", response);
    }
}
