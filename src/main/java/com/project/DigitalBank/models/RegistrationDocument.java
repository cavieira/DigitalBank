package com.project.DigitalBank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegistrationDocument {

    @NotNull
    @Id
    @Column(name = "id")
    private String id;

    @NotNull
    private String document;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Registration registration;

    public RegistrationDocument(@NotNull String document) {
        this.document = document;
    }
}
