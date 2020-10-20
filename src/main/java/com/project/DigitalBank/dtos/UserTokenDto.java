package com.project.DigitalBank.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserTokenDto {
    @NotNull
    String firstName;

    @NotNull
    String email;

    @NotNull
    @Size(min = 6, max = 6)
    String firstAccessToken;
}
