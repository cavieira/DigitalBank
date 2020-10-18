package com.project.DigitalBank.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Value;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

@Value
@AllArgsConstructor(onConstructor_ = @JsonCreator(mode = JsonCreator.Mode.PROPERTIES))
@Builder(toBuilder = true)
public class RegistrationDocumentDto {

    @NotNull
    @NotBlank
    String document;
}
