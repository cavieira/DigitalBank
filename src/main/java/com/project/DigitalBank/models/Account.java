package com.project.DigitalBank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Account {

    @NotNull
    @Id
    private String id;

    @NotNull
    @Size(min = 4, max = 4)
    String branchNumber;

    @NotNull
    @Size(min = 8, max = 8)
    String accountNumber;

    @NotNull
    @Size(min = 3, max = 3)
    String bankCode;

    @NotNull
    BigDecimal balance;

    @OneToOne
    @MapsId
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Registration registration;
}
