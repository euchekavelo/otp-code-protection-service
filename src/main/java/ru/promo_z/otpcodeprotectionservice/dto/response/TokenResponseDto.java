package ru.promo_z.otpcodeprotectionservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenResponseDto {

    private String token;
}
