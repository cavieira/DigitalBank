package com.project.DigitalBank.enumerations;

public enum UserStatus {
    ATIVO(0),
    TOKEN_ENVIADO(1);

    private final int value;

    UserStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
