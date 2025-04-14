package ru.promo_z.otpcodeprotectionservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "otp_code_configurations")
@Data
public class OtpCodeConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long lifeTimeInMilliseconds;

    private int length;
}
