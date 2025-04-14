package ru.promo_z.otpcodeprotectionservice.dto.request;

import lombok.Data;

@Data
public class UserRequestDto {

    private String login;
    private String password;
    private String phoneNumber;
    private String role;
}
