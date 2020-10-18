package com.project.DigitalBank.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.DigitalBank.dtos.RegistrationAddressDto;
import com.project.DigitalBank.dtos.RegistrationDocumentDto;
import com.project.DigitalBank.dtos.RegistrationDto;
import com.project.DigitalBank.dtos.RegistrationInformationDto;
import com.project.DigitalBank.exceptions.RegistrationAddressNotCompleted;
import com.project.DigitalBank.exceptions.RegistrationDocumentNotCompleted;
import com.project.DigitalBank.exceptions.RegistrationNotFound;
import com.project.DigitalBank.models.Registration;
import com.project.DigitalBank.models.RegistrationAddress;
import com.project.DigitalBank.models.RegistrationDocument;
import com.project.DigitalBank.repositories.RegistrationAddressRepository;
import com.project.DigitalBank.repositories.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepository repository;
    private final RegistrationAddressRepository registrationAddressRepository;

    private final UUIDService uuidService;

    private final RegistrationDocumentService registrationDocumentService;

    @Autowired
    private ObjectMapper mapper;

    public String validateAndSaveIdentificationInformation(RegistrationDto registrationDto) {

        if (repository.findOneByCpf(registrationDto.getCpf()) != null) {
            throw new ValidationException("CPF já registrado! Favor informar o CPF correto.");
        }

        if (repository.findOneByEmail(registrationDto.getEmail()) != null) {
            throw new ValidationException("Email já registrado! Favor informar o email correto.");
        }


        final String id = uuidService.generate();

        Registration registration = new Registration(id, registrationDto);

        repository.save(registration);

        return id;
    }

    public void validateAndSaveAddressInformation(String id, RegistrationAddressDto registrationAddressDto) {
        var registrationOptional = repository.findById(id);

        if (registrationOptional.isEmpty()) {
            throw new RegistrationNotFound("Favor cadastrar as informações básicas antes de cadastrar o endereço.");
        }

        var registration = registrationOptional.get();

        RegistrationAddress registrationAddress = new RegistrationAddress(registrationAddressDto);

        registration.setRegistrationAddress(registrationAddress);
        registrationAddress.setRegistration(registration);

        repository.save(registration);
    }

    public void validateAndSaveCPFFile(String id, RegistrationDocumentDto registrationDocumentDto) {
        // Verify if registration and registrationAddress are valid
        var registrationOptional = repository.findById(id);

        if (registrationOptional.isEmpty()) {
            throw new RegistrationNotFound("Favor cadastrar as informações básicas antes de enviar o CPF.");
        }

        var registration = registrationOptional.get();

        if (registration.getRegistrationAddress() == null) {
            throw new RegistrationAddressNotCompleted("Favor cadastrar o endereço antes de enviar o CPF.");
        }


        String document = registrationDocumentService.saveDocument(id, registrationDocumentDto.getDocument());

        RegistrationDocument registrationDocument = new RegistrationDocument(document);

        registration.setRegistrationDocument(registrationDocument);
        registrationDocument.setRegistration(registration);

        repository.save(registration);
    }

    public RegistrationInformationDto getRegistrationInfo(String id) {
        // Verify if registration, registrationAddress and registrationDocument are valid
        var registrationOptional = repository.findById(id);

        if (registrationOptional.isEmpty()) {
            throw new RegistrationNotFound("Favor cadastrar as informações básicas.");
        }

        var registration = registrationOptional.get();
        var address = registration.getRegistrationAddress();

        if (address == null) {
            throw new RegistrationAddressNotCompleted("Favor cadastrar o endereço.");
        }

        if (registration.getRegistrationDocument() == null) {
            throw new RegistrationDocumentNotCompleted("Favor enviar a foto do CPF.");
        }

        return RegistrationInformationDto
                .builder()
                .firstName(registration.getFirstName())
                .lastName(registration.getLastName())
                .email(registration.getEmail())
                .birthDate(registration.getBirthDate())
                .cpf(registration.getCpf())
                .rua(address.getRua())
                .bairro(address.getBairro())
                .cep(address.getCep())
                .complemento(address.getComplemento())
                .cidade(address.getCidade())
                .estado(address.getEstado())
                .build();
    }
}
