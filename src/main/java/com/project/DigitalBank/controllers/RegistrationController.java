package com.project.DigitalBank.controllers;

import com.project.DigitalBank.dtos.RegistrationDto;
import com.project.DigitalBank.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/registration/{id}/address/")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).body(id);
    }
}
