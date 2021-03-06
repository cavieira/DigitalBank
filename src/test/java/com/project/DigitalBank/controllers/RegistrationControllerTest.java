package com.project.DigitalBank.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.DigitalBank.dtos.*;
import com.project.DigitalBank.services.RegistrationService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class RegistrationControllerTest extends BaseControllerTest {

    private static final String ID = "id";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email@email.com";
    private static final LocalDate BIRTH_DATE = LocalDate.of(1970, 1, 1);
    private static final String CPF = "00000000000";

    private static final RegistrationDto REGISTRATION_DTO = RegistrationDto
            .builder()
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .email(EMAIL)
            .birthDate(BIRTH_DATE)
            .cpf(CPF)
            .build();

    private static final String CEP = "00000-000";
    private static final String RUA = "rua";
    private static final String BAIRRO = "bairro";
    private static final String COMPLEMENTO = "complemento";
    private static final String CIDADE = "cidade";
    private static final String ESTADO = "estado";

    private static final RegistrationAddressDto REGISTRATION_ADDRESS_DTO = RegistrationAddressDto
            .builder()
            .cep(CEP)
            .rua(RUA)
            .bairro(BAIRRO)
            .complemento(COMPLEMENTO)
            .cidade(CIDADE)
            .estado(ESTADO)
            .build();

    private static final String DOCUMENT = "document";

    private static final RegistrationDocumentDto REGISTRATION_DOCUMENT_DTO = RegistrationDocumentDto
            .builder()
            .document(DOCUMENT)
            .build();

    private static final RegistrationInformationDto REGISTRATION_INFORMATION_DTO = RegistrationInformationDto
            .builder()
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .email(EMAIL)
            .birthDate(BIRTH_DATE)
            .cpf(CPF)
            .cep(CEP)
            .rua(RUA)
            .bairro(BAIRRO)
            .complemento(COMPLEMENTO)
            .cidade(CIDADE)
            .estado(ESTADO)
            .build();

    private static final ProposalAcceptationDto PROPOSAL_ACCEPTATION_DTO = ProposalAcceptationDto
            .builder()
            .accept(true)
            .build();

    private static final ProposalAcceptationDto PROPOSAL_ACCEPTATION_DTO_WITH_ID = ProposalAcceptationDto
            .builder()
            .accept(true)
            .id(ID)
            .build();

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private RegistrationController registrationController;

    @Nested
    class beginRegistration {

        @Test
        void shouldReturnOkWhenArgumentsAreValid() {
            when(registrationService.validateAndSaveIdentificationInformation(REGISTRATION_DTO)).thenReturn(ID);

            ResponseEntity<String> responseEntity = registrationController.beginRegistration(REGISTRATION_DTO);

            assertEquals(ID, responseEntity.getBody());

            verify(registrationService).validateAndSaveIdentificationInformation(REGISTRATION_DTO);
        }
    }

    @Nested
    class addressRegistration {

        @Test
        void shouldReturnOkWhenArgumentsAreValid() {
            doNothing().when(registrationService).validateAndSaveAddressInformation(ID, REGISTRATION_ADDRESS_DTO);

            ResponseEntity<String> responseEntity = registrationController.addressRegistration(ID, REGISTRATION_ADDRESS_DTO);

            assertEquals(ID, responseEntity.getBody());

            verify(registrationService).validateAndSaveAddressInformation(ID, REGISTRATION_ADDRESS_DTO);
        }
    }

    @Nested
    class documentRegistration {

        @Test
        void shouldReturnOkWhenArgumentsAreValid() {
            doNothing().when(registrationService).validateAndSaveCPFFile(ID, REGISTRATION_DOCUMENT_DTO);

            ResponseEntity<String> responseEntity = registrationController.documentRegistration(ID, REGISTRATION_DOCUMENT_DTO);

            assertEquals(ID, responseEntity.getBody());

            verify(registrationService).validateAndSaveCPFFile(ID, REGISTRATION_DOCUMENT_DTO);
        }
    }

    @Nested
    class proposalInformation {

        @Test
        void shouldReturnRegistrationInformation() {
            when(registrationService.getRegistrationInfo(ID)).thenReturn(REGISTRATION_INFORMATION_DTO);

            ResponseEntity<RegistrationInformationDto> responseEntity = registrationController.proposalInformation(ID);

            assertEquals(REGISTRATION_INFORMATION_DTO, responseEntity.getBody());

            verify(registrationService).getRegistrationInfo(ID);
        }
    }

    @Nested
    class proposalAcceptation {

        @Test
        void shouldCallAcceptAndReturnOkWhenArgumentsAreValid() throws JsonProcessingException {
            doNothing().when(registrationService).acceptRegistration(PROPOSAL_ACCEPTATION_DTO_WITH_ID);

            ResponseEntity<String> responseEntity = registrationController.proposalAcceptation(ID, PROPOSAL_ACCEPTATION_DTO);

            verify(registrationService).acceptRegistration(PROPOSAL_ACCEPTATION_DTO_WITH_ID);
        }

        @Test
        void shouldCallRejectAndReturnOkWhenArgumentsAreValid() throws JsonProcessingException {
            var proposalAcceptationDto = PROPOSAL_ACCEPTATION_DTO_WITH_ID
                    .toBuilder()
                    .accept(false)
                    .build();

            doNothing().when(registrationService).rejectedRegistration(proposalAcceptationDto);

            ResponseEntity<String> responseEntity = registrationController.proposalAcceptation(ID, proposalAcceptationDto);

            verify(registrationService).rejectedRegistration(proposalAcceptationDto);
        }

    }
}