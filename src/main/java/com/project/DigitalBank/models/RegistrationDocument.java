package com.project.DigitalBank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.DigitalBank.dtos.RegistrationAddressDto;
import com.project.DigitalBank.dtos.RegistrationDocumentDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegistrationDocument {

    @NotNull
    @Id
    private String id;

    @NotNull
    private String document;

    @OneToOne
    @MapsId
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Registration registration;

    public RegistrationDocument(@NotNull String document) {
        this.document = document;
    }
}
