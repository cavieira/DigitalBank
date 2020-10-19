package com.project.DigitalBank.services;

import com.project.DigitalBank.dtos.DocumentValidationDto;
import com.project.DigitalBank.models.Registration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentValidationService {

    public boolean externalApiValidationDocument(Registration registration) {
        int MAX_TENTATIVES = 2;
        int tentatives = 0;
        boolean isValid = false;

        DocumentValidationDto documentValidationDto = DocumentValidationDto
                .builder()
                .firstName(registration.getFirstName())
                .lastName(registration.getLastName())
                .birthDate(registration.getBirthDate())
                .cpf(registration.getCpf())
                .document(registration.getRegistrationDocument().getDocument())
                .build();

        while (!isValid && tentatives < MAX_TENTATIVES) {
            isValid = getValidationDocument(documentValidationDto);
            tentatives++;
        }

        return isValid;
    }

    private boolean getValidationDocument(DocumentValidationDto documentValidationDto) {
        // TODO: Call the external api to validate document
        return true;
    }
}

