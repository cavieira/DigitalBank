package com.project.DigitalBank.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReceivedTransfer {

    @NotNull
    @Id
    String transferUniqueCode;

    @NotNull
    BigDecimal value;

    @NotNull
    LocalDateTime date;

    @NotNull
    @Pattern(regexp = "^[0-9]{11}$")
    String sourceCPF;

    @NotNull
    @Size(min = 3, max = 3)
    String sourceBankCode;

    @NotNull
    @Size(min = 4, max = 4)
    String sourceBranchNumber;

    @NotNull
    @Size(min = 8, max = 8)
    String sourceAccountNumber;

    @NotNull
    @Pattern(regexp = "^[0-9]{11}$")
    String destinationCPF;

    @NotNull
    @Size(min = 4, max = 4)
    String destinationBranchNumber;

    @NotNull
    @Size(min = 8, max = 8)
    String destinationAccountNumber;
}
