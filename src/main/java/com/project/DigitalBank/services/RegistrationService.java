package com.project.DigitalBank.services;

import com.project.DigitalBank.dtos.RegistrationDto;
import com.project.DigitalBank.models.Registration;
import com.project.DigitalBank.repositories.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepository repository;

    private final UUIDService uuidService;

    public String validateAndSaveIdentificationInformation(RegistrationDto registrationDto) {

        if (repository.findOneByCpf(registrationDto.getCpf()) != null) {
            throw new ValidationException("CPF já registrado");
        }

        if (repository.findOneByEmail(registrationDto.getEmail()) != null) {
            throw new ValidationException("Email já registrado");
        }


        final String id = uuidService.generate();

        Registration registration = new Registration(id, registrationDto);

        repository.save(registration);

        return id;
    }

}
