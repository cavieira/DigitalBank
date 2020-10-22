package com.project.DigitalBank.exceptions;

public class UnprocessableOperation extends RuntimeException {

    public UnprocessableOperation(String message) {
        super(message);
    }
}
