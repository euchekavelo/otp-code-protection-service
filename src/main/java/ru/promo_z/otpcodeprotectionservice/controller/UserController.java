package ru.promo_z.otpcodeprotectionservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.promo_z.otpcodeprotectionservice.dto.response.UserResponseDto;
import ru.promo_z.otpcodeprotectionservice.exception.UserNotFoundException;
import ru.promo_z.otpcodeprotectionservice.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsersExcludingAdmin() {

        log.info("getAllUsersExcludingAdmin");
        return ResponseEntity.ok(userService.getAllUsersExcludingAdmin());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllUsersExcludingAdmin() {
        log.info("deleteAllUsersExcludingAdmin");
        userService.deleteAllUsersExcludingAdmin();

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable long userId) throws UserNotFoundException {
        log.info("Deletion of user with ID {} begins.", userId);
        userService.deleteUserById(userId);

        return ResponseEntity.noContent().build();
    }
}
