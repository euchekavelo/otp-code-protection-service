package ru.promo_z.otpcodeprotectionservice.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.promo_z.otpcodeprotectionservice.model.enums.Status;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_codes")
@Data
public class OtpCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(unique = true)
    private long operationId;

    private int value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime expiryDate;
}
