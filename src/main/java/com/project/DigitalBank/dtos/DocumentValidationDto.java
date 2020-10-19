package com.project.DigitalBank.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class DocumentValidationDto {

    @NotNull
    @NotBlank
    String firstName;

    @NotNull
    @NotBlank
    String lastName;

    @NotNull
    @NotBlank
    LocalDate birthDate;

    @NotNull
    @NotBlank
    String cpf;

    @NotNull
    @NotBlank
    String document;
}
