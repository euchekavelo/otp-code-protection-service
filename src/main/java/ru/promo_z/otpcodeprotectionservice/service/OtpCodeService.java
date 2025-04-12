package ru.promo_z.otpcodeprotectionservice.service;

import ru.promo_z.otpcodeprotectionservice.dto.request.OtpCodeConfigRequestDto;
import ru.promo_z.otpcodeprotectionservice.dto.request.OperationRequestDto;
import ru.promo_z.otpcodeprotectionservice.dto.request.OtpCodeRequestDto;
import ru.promo_z.otpcodeprotectionservice.dto.response.ResponseDto;
import ru.promo_z.otpcodeprotectionservice.exception.ConfigurationOtpCodeException;
import ru.promo_z.otpcodeprotectionservice.exception.OtpCodeConfigurationNotFoundException;
import ru.promo_z.otpcodeprotectionservice.exception.OtpCodeNotFoundException;
import ru.promo_z.otpcodeprotectionservice.exception.OtpCodeValidationException;

public interface OtpCodeService {

    ResponseDto generateOtpCode(OperationRequestDto operationRequestDto) throws OtpCodeConfigurationNotFoundException;

    ResponseDto installConfigurationOtpCodes(OtpCodeConfigRequestDto otpCodeConfigRequestDto) throws ConfigurationOtpCodeException;

    ResponseDto validateOtpCode(OtpCodeRequestDto otpCodeRequestDto) throws OtpCodeNotFoundException,
            OtpCodeValidationException;

    void updateExpiredOtpCodes();
}