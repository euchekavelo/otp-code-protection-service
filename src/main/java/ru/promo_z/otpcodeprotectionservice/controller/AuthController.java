package ru.promo_z.otpcodeprotectionservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.promo_z.otpcodeprotectionservice.dto.request.UserRequestDto;
import ru.promo_z.otpcodeprotectionservice.dto.response.TokenResponseDto;
import ru.promo_z.otpcodeprotectionservice.dto.response.UserResponseDto;
import ru.promo_z.otpcodeprotectionservice.exception.RegistrationException;
import ru.promo_z.otpcodeprotectionservice.exception.UserNotFoundException;
import ru.promo_z.otpcodeprotectionservice.service.AuthService;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registration")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto userRequestDto)
            throws RegistrationException {

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(userRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody UserRequestDto userRequestDto)
            throws UserNotFoundException {

        return ResponseEntity.ok(authService.login(userRequestDto));
    }
}
