package com.project.DigitalBank.exceptions;

public class RegistrationDocumentValidationFailed extends RuntimeException {
    public RegistrationDocumentValidationFailed(String message) {
        super(message);
    }
}
