package ru.promo_z.otpcodeprotectionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OtpCodeProtectionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OtpCodeProtectionServiceApplication.class, args);
	}
}