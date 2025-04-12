package ru.promo_z.otpcodeprotectionservice.service;

import ru.promo_z.otpcodeprotectionservice.dto.request.UserRequestDto;
import ru.promo_z.otpcodeprotectionservice.dto.response.TokenResponseDto;
import ru.promo_z.otpcodeprotectionservice.dto.response.UserResponseDto;
import ru.promo_z.otpcodeprotectionservice.exception.RegistrationException;
import ru.promo_z.otpcodeprotectionservice.exception.UserNotFoundException;

public interface AuthService {

    UserResponseDto register(UserRequestDto userRequestDto) throws RegistrationException;

    TokenResponseDto login(UserRequestDto userRequestDto) throws UserNotFoundException;
}
