package ru.promo_z.otpcodeprotectionservice.dto.request;

import lombok.Data;

@Data
public class OtpCodeRequestDto {

    private int otpCode;
    private long operationId;
}
