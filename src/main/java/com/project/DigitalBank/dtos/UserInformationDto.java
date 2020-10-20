package com.project.DigitalBank.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserInformationDto {

    @NotNull
    @NotBlank
    @Email
    String email;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$")
    String cpf;
}
