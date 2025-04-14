package ru.promo_z.otpcodeprotectionservice.service;

import ru.promo_z.otpcodeprotectionservice.dto.response.UserResponseDto;
import ru.promo_z.otpcodeprotectionservice.exception.UserNotFoundException;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAllUsersExcludingAdmin();

    void deleteAllUsersExcludingAdmin();

    void deleteUserById(long userId) throws UserNotFoundException;
}
