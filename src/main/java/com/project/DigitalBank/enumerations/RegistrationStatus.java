package com.project.DigitalBank.enumerations;

public enum RegistrationStatus {
    INICIADO(0),
    INICIADO_INFORMACOES(1),
    INICIADO_ENDERECO(2),
    PENDENTE(3),
    PENDENTE_VALIDACAO(4),
    ACEITO(5),
    COMPLETO(6);

    private final int value;

    RegistrationStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean before(RegistrationStatus other) {
        return value < other.value;
    }

    public boolean after(RegistrationStatus other) {
        return value > other.value;
    }
}
