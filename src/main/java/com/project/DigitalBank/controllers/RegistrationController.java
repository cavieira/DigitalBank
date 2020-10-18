package com.project.DigitalBank.controllers;

import com.project.DigitalBank.dtos.RegistrationAddressDto;
import com.project.DigitalBank.dtos.RegistrationDocumentDto;
import com.project.DigitalBank.dtos.RegistrationDto;
import com.project.DigitalBank.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/registration/")
    public ResponseEntity<String> beginRegistration(@RequestBody @Valid @NotNull RegistrationDto registrationDto) {
        String id = registrationService.validateAndSaveIdentificationInformation(registrationDto);

        return createURI("/registration/{id}/address/", id);
    }

    @PostMapping("/registration/{id}/address/")
    public ResponseEntity<String> addressRegistration(
            @PathVariable String id,
            @RequestBody @Valid @NotNull RegistrationAddressDto registrationAddressDto) {
        registrationService.validateAndSaveAddressInformation(id, registrationAddressDto);

        return createURI("/registration/{id}/document/", id);
    }

    @PostMapping("/registration/{id}/document/")
    public ResponseEntity<String> documentRegistration(
            @PathVariable String id,
            @RequestBody @Valid @NotNull RegistrationDocumentDto registrationDocumentDto) {

        registrationService.validateAndSaveCPFFile(id, registrationDocumentDto);

        return createURI("/registration/{id}/proposalinfo/", id);
    }

    private ResponseEntity<String> createURI(String path, String id) {
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(path)
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).body(id);
    }
}
