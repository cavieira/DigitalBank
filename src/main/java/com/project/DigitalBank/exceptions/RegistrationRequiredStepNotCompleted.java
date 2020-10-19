package com.project.DigitalBank.exceptions;

public class RegistrationRequiredStepNotCompleted extends RuntimeException {

    public RegistrationRequiredStepNotCompleted(String message) {
        super(message);
    }
}
