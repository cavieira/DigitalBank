package com.project.DigitalBank.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TokenInformationDto {
    @NotNull
    @Size(min = 6, max = 6)
    String token;

    @NotNull
    @Size(min = 8, max = 8)
    String password;
}
