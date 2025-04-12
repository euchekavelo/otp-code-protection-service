package ru.promo_z.otpcodeprotectionservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.promo_z.otpcodeprotectionservice.dto.request.OtpCodeConfigRequestDto;
import ru.promo_z.otpcodeprotectionservice.dto.request.OperationRequestDto;
import ru.promo_z.otpcodeprotectionservice.dto.request.OtpCodeRequestDto;
import ru.promo_z.otpcodeprotectionservice.dto.response.ResponseDto;
import ru.promo_z.otpcodeprotectionservice.exception.ConfigurationOtpCodeException;
import ru.promo_z.otpcodeprotectionservice.exception.OtpCodeConfigurationNotFoundException;
import ru.promo_z.otpcodeprotectionservice.exception.OtpCodeNotFoundException;
import ru.promo_z.otpcodeprotectionservice.exception.OtpCodeValidationException;
import ru.promo_z.otpcodeprotectionservice.service.OtpCodeService;

@RestController
@RequestMapping("/otp-codes")
public class OtpCodeController {

    private final OtpCodeService otpCodeService;

    @Autowired
    public OtpCodeController(OtpCodeService otpCodeService) {
        this.otpCodeService = otpCodeService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> generateOtpCode(@RequestBody OperationRequestDto operationRequestDto)
            throws OtpCodeConfigurationNotFoundException {

        return ResponseEntity.status(HttpStatus.CREATED).body(otpCodeService.generateOtpCode(operationRequestDto));
    }

    @PostMapping("/validation")
    public ResponseEntity<ResponseDto> validateOtpCode(@RequestBody OtpCodeRequestDto otpCodeRequestDto)
            throws OtpCodeValidationException, OtpCodeNotFoundException {

        return ResponseEntity.ok(otpCodeService.validateOtpCode(otpCodeRequestDto));
    }

    @PostMapping("/configuration")
    public ResponseEntity<ResponseDto> installConfigurationOtpCodes(@RequestBody OtpCodeConfigRequestDto otpCodeConfigRequestDto)
            throws ConfigurationOtpCodeException {

        return ResponseEntity.ok(otpCodeService.installConfigurationOtpCodes(otpCodeConfigRequestDto));
    }
}
