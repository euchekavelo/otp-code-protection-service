package ru.promo_z.otpcodeprotectionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.promo_z.otpcodeprotectionservice.model.OtpCode;
import ru.promo_z.otpcodeprotectionservice.model.User;
import ru.promo_z.otpcodeprotectionservice.model.enums.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {

    Optional<OtpCode> findByUserAndOperationId(User user, Long operationId);

    Optional<OtpCode> findByValueAndOperationIdAndUser(int value, long operationId, User user);

    List<OtpCode> findAllByStatusAndExpiryDateBefore(Status status, LocalDateTime expiryDate);
}
