package com.project.DigitalBank.models;

import com.project.DigitalBank.dtos.RegistrationDto;
import com.project.DigitalBank.validations.LegalAge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Registration {

    @NotNull
    @Id
    private String id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @LegalAge
    private LocalDate birthDate;

    @NotNull
    @Pattern(regexp = "^[0-9]{11}$")
    private String cpf;

    @OneToOne(mappedBy = "registration",cascade = CascadeType.ALL)
    private RegistrationAddress registrationAddress;

    @OneToOne(mappedBy = "registration",cascade = CascadeType.ALL)
    private RegistrationDocument registrationDocument;

    public Registration(String id, RegistrationDto registrationDto) {
        this.id = id;
        this.firstName = registrationDto.getFirstName();
        this.lastName = registrationDto.getLastName();
        this.email = registrationDto.getEmail();
        this.birthDate = registrationDto.getBirthDate();
        this.cpf = registrationDto.getCpf();
    }
}
