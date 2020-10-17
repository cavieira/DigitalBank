package com.project.DigitalBank.dtos;

import com.project.DigitalBank.validations.LegalAge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegistrationDto {

    @NotNull
    @NotBlank
    String firstName;

    @NotNull
    @NotBlank
    String lastName;

    @NotNull
    @NotBlank
    @Email
    String email;

    @NotNull
    @LegalAge
    LocalDate birthDate;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$")
    String cpf;
}
