package ru.promo_z.otpcodeprotectionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.promo_z.otpcodeprotectionservice.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    Optional<User> findByRole(String role);

    List<User> findAllByRoleNot(String role);

    void deleteAllByRoleNot(String role);
}
