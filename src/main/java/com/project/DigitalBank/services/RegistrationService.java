package com.project.DigitalBank.services;

import com.project.DigitalBank.dtos.RegistrationAddressDto;
import com.project.DigitalBank.dtos.RegistrationDocumentDto;
import com.project.DigitalBank.dtos.RegistrationDto;
import com.project.DigitalBank.models.Registration;
import com.project.DigitalBank.models.RegistrationAddress;
import com.project.DigitalBank.models.RegistrationDocument;
import com.project.DigitalBank.repositories.RegistrationAddressRepository;
import com.project.DigitalBank.repositories.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ValidationException;
import java.io.FileOutputStream;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepository repository;
    private final RegistrationAddressRepository registrationAddressRepository;

    private final UUIDService uuidService;

    @Value(value = "${image.path:./img}")
    private String imagePath;

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

    public void validateAndSaveAddressInformation(String id, RegistrationAddressDto registrationAddressDto) {
        var registrationOptional = repository.findById(id);

        if (registrationOptional.isEmpty()) {
            throw new ValidationException("registrationId not found");
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
            throw new ValidationException("registrationId not found");
        }

        var registration = registrationOptional.get();

        if (registration.getRegistrationAddress() == null) {
            throw new ValidationException("registrationAdress not found");
        }


        //Decode the String which is encoded by using Base64 class
        byte[] imageByte = Base64.decodeBase64(registrationDocumentDto.getDocument());

        //Create the file name where the image will be save
        String document = String.format("%s%s%s", imagePath, id, ".jpg");

        //Save the image
        try {
            new FileOutputStream(document).write(imageByte);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error while processing image ", e);
        }

        RegistrationDocument registrationDocument = new RegistrationDocument(document);

        registration.setRegistrationDocument(registrationDocument);
        registrationDocument.setRegistration(registration);

        repository.save(registration);
    }
}
