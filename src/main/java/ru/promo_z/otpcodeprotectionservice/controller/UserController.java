package ru.promo_z.otpcodeprotectionservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.promo_z.otpcodeprotectionservice.dto.response.UserResponseDto;
import ru.promo_z.otpcodeprotectionservice.exception.UserNotFoundException;
import ru.promo_z.otpcodeprotectionservice.service.TelegramService;
import ru.promo_z.otpcodeprotectionservice.service.UserService;
import ru.promo_z.otpcodeprotectionservice.service.EmailService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final TelegramService telegramService;

    @Autowired
    public UserController(UserService userService, EmailService emailService, TelegramService telegramService) {
        this.userService = userService;
        this.emailService = emailService;
        this.telegramService = telegramService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsersExcludingAdmin() {
        return ResponseEntity.ok(userService.getAllUsersExcludingAdmin());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllUsersExcludingAdmin() {
        userService.deleteAllUsersExcludingAdmin();

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable long userId) throws UserNotFoundException {
        userService.deleteUserById(userId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        emailService.sendEmail("euchekavelo@gmail.com", "test message", "123213");
        //telegramService.sendMessage("Hello World");
        return ResponseEntity.ok("test");
    }
}
