package ru.promo_z.otpcodeprotectionservice.controller;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import ru.promo_z.otpcodeprotectionservice.dto.response.ErrorResponseDto;
import ru.promo_z.otpcodeprotectionservice.exception.*;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

    @ExceptionHandler({UserNotFoundException.class, OtpCodeConfigurationNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleExceptionForNotFoundHttpStatus(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler({RegistrationException.class, UnsupportedOperationException.class, ConfigurationOtpCodeException.class,
            OtpCodeValidationException.class})
    public ResponseEntity<ErrorResponseDto> handleExceptionForBadRequestHttpStatus(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler({RestClientException.class, IOException.class, MessagingException.class, MailException.class})
    public ResponseEntity<ErrorResponseDto> handleExceptionForInternalServerErrorHttpStatus(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getErrorResponse(ex.getMessage()));
    }

    private ErrorResponseDto getErrorResponse(String message) {
        return ErrorResponseDto.builder()
                .message(message)
                .result(false)
                .build();
    }
}
