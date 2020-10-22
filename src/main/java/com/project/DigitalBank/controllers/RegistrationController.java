package com.project.DigitalBank.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.DigitalBank.dtos.*;
import com.project.DigitalBank.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.project.DigitalBank.utils.URIUtil.createURI;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/registration/")
    public ResponseEntity<String> beginRegistration(
            @RequestBody @Valid @NotNull RegistrationDto registrationDto) {
        String id = registrationService.validateAndSaveIdentificationInformation(registrationDto);

        return createURI("/registration/{id}/address/", id, id);
    }

    @PostMapping("/registration/{id}/address/")
    public ResponseEntity<String> addressRegistration(
            @PathVariable String id,
            @RequestBody @Valid @NotNull RegistrationAddressDto registrationAddressDto) {
        registrationService.validateAndSaveAddressInformation(id, registrationAddressDto);

        return createURI("/registration/{id}/document/", id, id);
    }

    @PostMapping("/registration/{id}/document/")
    public ResponseEntity<String> documentRegistration(
            @PathVariable String id,
            @RequestBody @Valid @NotNull RegistrationDocumentDto registrationDocumentDto) {

        registrationService.validateAndSaveCPFFile(id, registrationDocumentDto);

        return createURI("/registration/{id}/proposalinfo/", id, id);
    }

    @GetMapping("registration/{id}/proposalinfo/")
    public ResponseEntity<RegistrationInformationDto> proposalInformation(
            @PathVariable String id) {
        RegistrationInformationDto information = registrationService.getRegistrationInfo(id);
        return createURI("/registration/{id}/proposalaccept/", id, information);
    }

    @PostMapping("/registration/{id}/proposalaccept/")
    public ResponseEntity<String> proposalAcceptation(
            @PathVariable String id,
            @RequestBody @Valid @NotNull ProposalAcceptationDto proposalAcceptationDto)
            throws JsonProcessingException {
        ProposalAcceptationDto proposalAcceptationDtoWithId = proposalAcceptationDto
                .toBuilder()
                .id(id)
                .build();
        if (proposalAcceptationDto.isAccept()) {
            registrationService.acceptRegistration(proposalAcceptationDtoWithId);
        } else {
            registrationService.rejectedRegistration(proposalAcceptationDtoWithId);
        }

        return ResponseEntity.ok().build();
    }
}
