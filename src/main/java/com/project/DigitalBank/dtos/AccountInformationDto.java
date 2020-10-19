package com.project.DigitalBank.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class AccountInformationDto {

    @NotNull
    String firstName;

    @NotNull
    String email;

    @NotNull
    @Size(min = 4, max = 4)
    String branchNumber;

    @NotNull
    @Size(min = 8, max = 8)
    String accountNumber;

}
