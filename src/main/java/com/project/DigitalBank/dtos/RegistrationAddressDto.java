package com.project.DigitalBank.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.persistence.Id;
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
    private String cep;

    @NotNull
    @NotBlank
    private String rua;

    @NotNull
    @NotBlank
    private String bairro;

    @NotNull
    @NotBlank
    private String complemento;

    @NotNull
    @NotBlank
    private String cidade;

    @NotNull
    @NotBlank
    private String estado;
}



