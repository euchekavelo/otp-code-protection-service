package ru.promo_z.otpcodeprotectionservice.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.promo_z.otpcodeprotectionservice.dto.request.UserRequestDto;
import ru.promo_z.otpcodeprotectionservice.dto.response.TokenResponseDto;
import ru.promo_z.otpcodeprotectionservice.dto.response.UserResponseDto;
import ru.promo_z.otpcodeprotectionservice.exception.RegistrationException;
import ru.promo_z.otpcodeprotectionservice.exception.UserNotFoundException;
import ru.promo_z.otpcodeprotectionservice.mapper.UserMapper;
import ru.promo_z.otpcodeprotectionservice.model.User;
import ru.promo_z.otpcodeprotectionservice.repository.UserRepository;
import ru.promo_z.otpcodeprotectionservice.security.JwtUtil;
import ru.promo_z.otpcodeprotectionservice.service.AuthService;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper, JwtUtil jwtUtil,
                           PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDto register(UserRequestDto userRequestDto) throws RegistrationException {
        String role = userRequestDto.getRole();

        if (role.equals("ROLE_ADMIN")) {
            Optional<User> optionalExistingAdmin = userRepository.findByRole(role);
            if (optionalExistingAdmin.isPresent()) {
                throw new RegistrationException("A user with the administrator role already exists.");
            }
        }

        User newUser = userMapper.userRequestDtoToUser(userRequestDto);
        User savedUser = userRepository.save(newUser);

        return userMapper.userToUserResponseDto(savedUser);
    }

    @Override
    public TokenResponseDto login(UserRequestDto userRequestDto) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findByLogin(userRequestDto.getLogin());

        if (optionalUser.isEmpty() || !passwordEncoder.matches(userRequestDto.getPassword(),
                optionalUser.get().getPassword())) {

            throw new UserNotFoundException("The user with the specified data was not found.");
        }

        return TokenResponseDto.builder()
                .token(jwtUtil.generateTokenForUser(userRequestDto.getLogin()))
                .build();
    }
}
