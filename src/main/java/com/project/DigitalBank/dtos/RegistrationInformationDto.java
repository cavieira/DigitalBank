package com.project.DigitalBank.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegistrationInformationDto {

    String firstName;

    String lastName;

    String email;

    LocalDate birthDate;

    String cpf;

    String cep;

    String rua;

    String bairro;

    String complemento;

    String cidade;

    String estado;
}
