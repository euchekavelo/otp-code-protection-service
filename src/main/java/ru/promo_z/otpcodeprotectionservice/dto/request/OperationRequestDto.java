package ru.promo_z.otpcodeprotectionservice.dto.request;

import lombok.Data;
import ru.promo_z.otpcodeprotectionservice.dto.enums.SendType;

@Data
public class OperationRequestDto {

    private SendType sendType;
    private long operationId;
}
