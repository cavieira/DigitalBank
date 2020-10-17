package com.project.DigitalBank.models;

import com.project.DigitalBank.dtos.RegistrationAddressDto;
import com.project.DigitalBank.dtos.RegistrationDto;
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
public class RegistrationAddress {

    @NotNull
    @Id
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
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
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
