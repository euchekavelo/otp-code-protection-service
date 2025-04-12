package ru.promo_z.otpcodeprotectionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.promo_z.otpcodeprotectionservice.model.OtpCodeConfiguration;

import java.util.Optional;

@Repository
public interface OtpCodeConfigurationRepository extends JpaRepository<OtpCodeConfiguration, Long> {

    Optional<OtpCodeConfiguration> findFirstByOrderByIdDesc();
}
