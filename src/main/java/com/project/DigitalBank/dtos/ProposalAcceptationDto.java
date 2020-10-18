package com.project.DigitalBank.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProposalAcceptationDto {

    @NotNull
    boolean accept;

    String id;
}
