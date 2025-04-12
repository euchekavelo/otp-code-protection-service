package ru.promo_z.otpcodeprotectionservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.promo_z.otpcodeprotectionservice.dto.response.UserResponseDto;
import ru.promo_z.otpcodeprotectionservice.exception.UserNotFoundException;
import ru.promo_z.otpcodeprotectionservice.mapper.UserMapper;
import ru.promo_z.otpcodeprotectionservice.model.User;
import ru.promo_z.otpcodeprotectionservice.repository.UserRepository;
import ru.promo_z.otpcodeprotectionservice.security.AuthUser;
import ru.promo_z.otpcodeprotectionservice.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserResponseDto> getAllUsersExcludingAdmin() {
        return userMapper.userListToUserResponseDtoList(userRepository.findAllByRoleNot("ROLE_ADMIN"));
    }

    @Transactional
    @Override
    public void deleteAllUsersExcludingAdmin() {
        userRepository.deleteAllByRoleNot("ROLE_ADMIN");
    }

    @Transactional
    @Override
    public void deleteUserById(long userId) throws UserNotFoundException {
        User currentAuthUser = getAuthUser();
        if (currentAuthUser.getId() == userId) {
            throw new UnsupportedOperationException("Cannot delete current auth user.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("The user with the specified ID was not found."));

        userRepository.delete(user);
    }

    private User getAuthUser() {
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return authUser.getUser();
    }
}
