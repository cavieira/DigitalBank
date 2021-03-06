package com.project.DigitalBank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.DigitalBank.enumerations.UserStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    @NotNull
    @Id
    @Column(name = "id")
    private String id;

    @NotNull
    private String firstName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "^[0-9]{11}$")
    private String cpf;

    private String password;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Account account;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserToken userToken;
}
