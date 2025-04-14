package ru.promo_z.otpcodeprotectionservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.promo_z.otpcodeprotectionservice.dto.enums.SendType;
import ru.promo_z.otpcodeprotectionservice.dto.request.OtpCodeConfigRequestDto;
import ru.promo_z.otpcodeprotectionservice.dto.request.OperationRequestDto;
import ru.promo_z.otpcodeprotectionservice.dto.request.OtpCodeRequestDto;
import ru.promo_z.otpcodeprotectionservice.dto.response.ResponseDto;
import ru.promo_z.otpcodeprotectionservice.exception.ConfigurationOtpCodeException;
import ru.promo_z.otpcodeprotectionservice.exception.OtpCodeConfigurationNotFoundException;
import ru.promo_z.otpcodeprotectionservice.exception.OtpCodeNotFoundException;
import ru.promo_z.otpcodeprotectionservice.exception.OtpCodeValidationException;
import ru.promo_z.otpcodeprotectionservice.mapper.OtpCodeConfigurationMapper;
import ru.promo_z.otpcodeprotectionservice.model.OtpCode;
import ru.promo_z.otpcodeprotectionservice.model.OtpCodeConfiguration;
import ru.promo_z.otpcodeprotectionservice.model.User;
import ru.promo_z.otpcodeprotectionservice.model.enums.Status;
import ru.promo_z.otpcodeprotectionservice.repository.OtpCodeConfigurationRepository;
import ru.promo_z.otpcodeprotectionservice.repository.OtpCodeRepository;
import ru.promo_z.otpcodeprotectionservice.security.AuthUser;
import ru.promo_z.otpcodeprotectionservice.service.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class OtpCodeServiceImpl implements OtpCodeService {

    private final OtpCodeRepository otpCodeRepository;
    private final OtpCodeConfigurationRepository otpCodeConfigurationRepository;
    private final OtpCodeConfigurationMapper otpCodeConfigurationMapper;
    private final TelegramService telegramService;
    private final EmailService emailService;
    private static final Random RANDOM = new Random();
    private final SmsService smsService;

    @Autowired
    public OtpCodeServiceImpl(OtpCodeRepository otpCodeRepository, OtpCodeConfigurationRepository otpCodeConfigurationRepository,
                              OtpCodeConfigurationMapper otpCodeConfigurationMapper, TelegramService telegramService,
                              EmailService emailService, SmsService smsService) {

        this.otpCodeRepository = otpCodeRepository;
        this.otpCodeConfigurationRepository = otpCodeConfigurationRepository;
        this.otpCodeConfigurationMapper = otpCodeConfigurationMapper;
        this.telegramService = telegramService;
        this.emailService = emailService;
        this.smsService = smsService;
    }

    @Transactional
    @Override
    public ResponseDto installConfigurationOtpCodes(OtpCodeConfigRequestDto otpCodeConfigRequestDto)
            throws ConfigurationOtpCodeException {

        if (otpCodeConfigRequestDto.getLength() > 9) {
            throw new ConfigurationOtpCodeException("The OTP-code length must not exceed 9 characters.");
        }

        otpCodeConfigurationRepository.deleteAll();

        OtpCodeConfiguration otpCodeConfiguration = otpCodeConfigurationMapper
                .otpCodeConfigRequestDtoToOtpCodeConfiguration(otpCodeConfigRequestDto);
        otpCodeConfigurationRepository.save(otpCodeConfiguration);

        log.info("OTP-code configuration installed.");
        return ResponseDto.builder()
                .message("The configuration has been successfully installed.")
                .build();
    }

    @Transactional
    @Override
    public ResponseDto generateOtpCode(OperationRequestDto operationRequestDto) throws OtpCodeConfigurationNotFoundException {

        User currentAuthUser = getAuthUser();
        Optional<OtpCode> optionalOtpCode = otpCodeRepository.findByUserAndOperationId(currentAuthUser,
                operationRequestDto.getOperationId());

        optionalOtpCode.ifPresent(otpCodeRepository::delete);
        otpCodeRepository.flush();
        Optional<OtpCodeConfiguration> optionalOtpCodeConfiguration
                = otpCodeConfigurationRepository.findFirstByOrderByIdDesc();

        if (optionalOtpCodeConfiguration.isEmpty()) {
            throw new OtpCodeConfigurationNotFoundException("The OTP-code configuration with the specified data was not found.");
        }

        OtpCodeConfiguration otpCodeConfiguration = optionalOtpCodeConfiguration.get();
        int otpCodeValue = generateOtpCodeValue(otpCodeConfiguration.getLength());

        OtpCode otpCode = new OtpCode();
        otpCode.setValue(otpCodeValue);
        otpCode.setUser(currentAuthUser);
        otpCode.setOperationId(operationRequestDto.getOperationId());
        otpCode.setStatus(Status.ACTIVE);
        otpCode.setExpiryDate(LocalDateTime.now().plus(otpCodeConfiguration.getLifeTimeInMilliseconds(), ChronoUnit.MILLIS));
        otpCodeRepository.save(otpCode);

        sendOtpCode(operationRequestDto.getSendType(), otpCodeValue, currentAuthUser);

        log.info("Generated OTP-code {}", otpCodeValue);
        return getResponseDto("The OTP-code for the specified operation has been generated and sent.");
    }

    @Override
    public ResponseDto validateOtpCode(OtpCodeRequestDto otpCodeRequestDto) throws OtpCodeNotFoundException,
            OtpCodeValidationException {

        int otpCodeValue = otpCodeRequestDto.getOtpCode();
        long operationId = otpCodeRequestDto.getOperationId();
        User currentAuthUser = getAuthUser();

        Optional<OtpCode> optionalOtpCode
                = otpCodeRepository.findByValueAndOperationIdAndUser(otpCodeValue, operationId, currentAuthUser);

        if (optionalOtpCode.isEmpty()) {
            throw new OtpCodeNotFoundException("OTP-code for the specified data was not found.");
        }

        OtpCode otpCode = optionalOtpCode.get();
        if (!otpCode.getStatus().equals(Status.ACTIVE) || otpCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new OtpCodeValidationException("The specified OTP-code is not valid.");
        }

        otpCode.setStatus(Status.USED);
        otpCodeRepository.save(otpCode);

        log.info("Validated OTP-code {}", otpCodeValue);
        return getResponseDto("OTP-code validated successfully.");
    }

    @Transactional
    @Scheduled(cron = "${app.otp-code.update-interval.cron}")
    @Override
    public void updateExpiredOtpCodes() {
        List<OtpCode> otpCodeList = otpCodeRepository.findAllByStatusAndExpiryDateBefore(Status.ACTIVE, LocalDateTime.now());
        otpCodeList.forEach(otpCode -> otpCode.setStatus(Status.EXPIRED));
        List<OtpCode> updatedOtpCodes = otpCodeRepository.saveAll(otpCodeList);

        log.info("Updated OTP-Codes for {} ", updatedOtpCodes.size());
    }

    private ResponseDto getResponseDto(String message) {
        return ResponseDto.builder()
                .message(message)
                .build();
    }

    private int generateOtpCodeValue(int length) {
        StringBuilder randomOtpCode = new StringBuilder();
        for (int i = 0; i < length; i++) {
            randomOtpCode.append(RANDOM.nextInt(10));
        }

        return Integer.parseInt(randomOtpCode.toString());
    }

    private User getAuthUser() {
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return authUser.getUser();
    }

    private void sendOtpCode(SendType sendType, int otpCodeValue, User user) {
        switch (sendType) {
            case TELEGRAM:
                telegramService.sendMessage("User with ID " + user.getId() + " sent OTP-code value to telegram: "
                        + otpCodeValue);
                break;
            case EMAIL:
                emailService.sendEmail(user.getLogin(), "OTP-code value to email: ",
                        "OTP-code value to email: " + otpCodeValue);
                break;
            case SMS:
                smsService.sendCode(user.getPhoneNumber(), "OTP-code value to sms: " + otpCodeValue);
                break;
            default:
                log.error("Unsupported send type: {}", sendType);
        }
    }
}
