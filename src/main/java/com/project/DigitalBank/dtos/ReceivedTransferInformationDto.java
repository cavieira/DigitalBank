package com.project.DigitalBank.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReceivedTransferInformationDto {
    @NotNull
    @NotBlank
    BigDecimal value;

    @NotNull
    @NotBlank
    LocalDateTime date;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$")
    String sourceCPF;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 3)
    String sourceBankCode;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 4)
    String sourceBranchNumber;

    @NotNull
    @NotBlank
    @Size(min = 8, max = 8)
    String sourceAccountNumber;

    @NotNull
    @NotBlank
    String transferUniqueCode;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$")
    String destinationCPF;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 4)
    String destinationBranchNumber;

    @NotNull
    @NotBlank
    @Size(min = 8, max = 8)
    String destinationAccountNumber;
}
