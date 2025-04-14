package ru.promo_z.otpcodeprotectionservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponseDto {

    private String message;
    private boolean result;
}
