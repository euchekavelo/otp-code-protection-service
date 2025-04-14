package ru.promo_z.otpcodeprotectionservice.dto.response;

import lombok.Data;

@Data
public class UserResponseDto {

    private long id;
    private String login;
    private String role;
}
