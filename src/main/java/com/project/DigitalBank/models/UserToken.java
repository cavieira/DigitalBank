package com.project.DigitalBank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.DigitalBank.enumerations.UserStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserToken {

    @NotNull
    @Id
    @Column(name = "id")
    private String id;

    @NotNull
    @Size(min = 6, max = 6)
    private String firstAccessToken;

    @NotNull
    private LocalDateTime dateOfCreation;

    @NotNull
    private UserStatus userStatus;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private User user;
}
