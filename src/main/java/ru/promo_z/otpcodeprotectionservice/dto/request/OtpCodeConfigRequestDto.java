package ru.promo_z.otpcodeprotectionservice.dto.request;

import lombok.Data;

@Data
public class OtpCodeConfigRequestDto {

    private long lifeTimeInMilliseconds;
    private int length;
}
