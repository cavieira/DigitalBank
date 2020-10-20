package com.project.DigitalBank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.DigitalBank.dtos.RegistrationAddressDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegistrationAddress {

    @NotNull
    @Id
    @Column(name = "id")
    private String id;

    @NotNull
    private String cep;

    @NotNull
    private String rua;

    @NotNull
    private String bairro;

    @NotNull
    private String complemento;

    @NotNull
    private String cidade;

    @NotNull
    private String estado;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Registration registration;

    public RegistrationAddress(RegistrationAddressDto registrationAddressDto) {
        this.cep = registrationAddressDto.getCep();
        this.rua = registrationAddressDto.getRua();
        this.bairro = registrationAddressDto.getBairro();
        this.complemento = registrationAddressDto.getComplemento();
        this.cidade = registrationAddressDto.getCidade();
        this.estado = registrationAddressDto.getEstado();
    }
}
