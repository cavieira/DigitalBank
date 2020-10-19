package com.project.DigitalBank.handlers;

import com.project.DigitalBank.dtos.ErrorDto;
import com.project.DigitalBank.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ValidationException;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class DigitalBankExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ErrorDto<Map<String, String>> errors = ErrorDto.<Map<String, String>>builder()
                .error(exception.getBindingResult().getFieldErrors()
                        .stream()
                        .collect(Collectors.toMap(
                                FieldError::getField,
                                (value) -> String.format("%s is not valid", value.getRejectedValue())
                        )))
                .build();

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ErrorDto<String>> handleValidationException(ValidationException exception) {
        return ResponseEntity.badRequest().body(ErrorDto.<String>builder().error(exception.getMessage()).build());
    }

    @ExceptionHandler(value = RegistrationNotFound.class)
    public ResponseEntity<?> handleRegistrationNotFound(RegistrationNotFound exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = {RegistrationRequiredStepNotCompleted.class, RegistrationStepAlreadyCompleted.class})
    public ResponseEntity<ErrorDto<String>> handleRegistrationAddressNotCompleted(RegistrationStepUnprocessable exception) {
        return ResponseEntity.unprocessableEntity().body(ErrorDto.<String>builder().error(exception.getMessage()).build());
    }

    @ExceptionHandler(value = RegistrationDocumentSaveFailed.class)
    public ResponseEntity<ErrorDto<String>> handleRegistrationDocumentSaveFailed(RegistrationDocumentSaveFailed exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDto.<String>builder().error(exception.getMessage()).build());
    }

    @ExceptionHandler(value = RegistrationDocumentValidationFailed.class)
    public ResponseEntity<ErrorDto<String>> handleRegistrationDocumentSaveFailed(RegistrationDocumentValidationFailed exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDto.<String>builder().error(exception.getMessage()).build());
    }
}
