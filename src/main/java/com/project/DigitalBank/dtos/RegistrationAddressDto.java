package com.project.DigitalBank.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegistrationAddressDto {

    @NotNull
    @NotBlank
    @Pattern(regexp = "^[0-9]{5}-[0-9]{3}$")
    String cep;

    @NotNull
    @NotBlank
    String rua;

    @NotNull
    @NotBlank
    String bairro;

    @NotNull
    @NotBlank
    String complemento;

    @NotNull
    @NotBlank
    String cidade;

    @NotNull
    @NotBlank
    String estado;
}



