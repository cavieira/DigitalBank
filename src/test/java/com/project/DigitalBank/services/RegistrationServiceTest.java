package com.project.DigitalBank.services;

import com.project.DigitalBank.dtos.RegistrationAddressDto;
import com.project.DigitalBank.dtos.RegistrationDocumentDto;
import com.project.DigitalBank.dtos.RegistrationDto;
import com.project.DigitalBank.enumerations.RegistrationStatus;
import com.project.DigitalBank.exceptions.EntityNotFound;
import com.project.DigitalBank.exceptions.RegistrationRequiredStepNotCompleted;
import com.project.DigitalBank.exceptions.RegistrationStepAlreadyCompleted;
import com.project.DigitalBank.models.Registration;
import com.project.DigitalBank.models.RegistrationAddress;
import com.project.DigitalBank.models.RegistrationDocument;
import com.project.DigitalBank.repositories.RegistrationRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class RegistrationServiceTest {

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

    private static final Registration REGISTRATION = new Registration(ID, REGISTRATION_DTO);

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

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private UUIDService uuidService;

    @Mock
    private RegistrationDocumentService registrationDocumentService;


    @InjectMocks
    private RegistrationService registrationService;

    @Nested
    class ValidateAndSaveIdentificationInformation {

        @Test
        void shouldThrowValidationExceptionWhenCpfIsNotUnique() {
            Registration registration = buildRegistration();
            when(registrationRepository.findOneByCpf(CPF)).thenReturn(registration);

            assertThrows(ValidationException.class,
                    () -> registrationService.validateAndSaveIdentificationInformation(REGISTRATION_DTO));

            verify(registrationRepository).findOneByCpf(CPF);
        }

        @Test
        void shouldThrowValidationExceptionWhenEmailIsNotUnique() {
            Registration registration = buildRegistration();
            when(registrationRepository.findOneByCpf(CPF)).thenReturn(null);
            when(registrationRepository.findOneByEmail(EMAIL)).thenReturn(registration);

            assertThrows(ValidationException.class,
                    () -> registrationService.validateAndSaveIdentificationInformation(REGISTRATION_DTO));

            verify(registrationRepository).findOneByCpf(CPF);
            verify(registrationRepository).findOneByEmail(EMAIL);
        }

        @Test
        void shouldSaveWhenArgumentsAreValid() {
            Registration registration = buildRegistration();
            when(uuidService.generate()).thenReturn(ID);
            when(registrationRepository.save(registration)).thenReturn(registration);
            when(registrationRepository.findOneByCpf(CPF)).thenReturn(null);
            when(registrationRepository.findOneByEmail(EMAIL)).thenReturn(null);

            String id = registrationService.validateAndSaveIdentificationInformation(REGISTRATION_DTO);

            assertEquals(ID, id);

            verify(uuidService).generate();
            verify(registrationRepository).save(registration
                    .toBuilder()
                    .registrationStatus(RegistrationStatus.INICIADO_INFORMACOES)
                    .build());
            verify(registrationRepository).findOneByCpf(CPF);
            verify(registrationRepository).findOneByEmail(EMAIL);
        }
    }

    @Nested
    class ValidateAndSaveAddressInformation {

        @Test
        void shouldThrowValidationExceptionWhenRegistrationWasNotFound() {
            when(registrationRepository.findById(ID)).thenReturn(Optional.empty());

            assertThrows(EntityNotFound.class,
                    () -> registrationService.validateAndSaveAddressInformation(ID, REGISTRATION_ADDRESS_DTO));

            verify(registrationRepository).findById(ID);
        }

        @Test
        void shouldThrowRegistrationStepAlreadyCompletedExceptionWhenRegistrationAddressStepAlreadyCompleted() {
            when(registrationRepository.findById(ID)).thenReturn(Optional.of(buildRegistration(RegistrationStatus.INICIADO_ENDERECO)));

            assertThrows(RegistrationStepAlreadyCompleted.class,
                    () -> registrationService.validateAndSaveAddressInformation(ID, REGISTRATION_ADDRESS_DTO));

            verify(registrationRepository).findById(ID);
        }

        @Test
        void shouldSaveWhenArgumentsAreValid() {
            RegistrationAddress registrationAddress = new RegistrationAddress(REGISTRATION_ADDRESS_DTO);

            Registration registrationWithAddress = buildRegistration(RegistrationStatus.INICIADO_INFORMACOES)
                    .toBuilder()
                    .registrationAddress(registrationAddress)
                    .build();

            registrationAddress.setRegistration(registrationWithAddress);


            when(registrationRepository.findById(ID)).thenReturn(Optional.of(buildRegistration()));
            when(registrationRepository.save(registrationWithAddress)).thenReturn(registrationWithAddress);

            registrationService.validateAndSaveAddressInformation(ID, REGISTRATION_ADDRESS_DTO);

            verify(registrationRepository).findById(ID);
            verify(registrationRepository).save(registrationWithAddress
                    .toBuilder()
                    .registrationStatus(RegistrationStatus.INICIADO_ENDERECO)
                    .build());
        }
    }

    @Nested
    class ValidateAndSaveCPFFile {

        @Test
        void shouldThrowValidationExceptionWhenRegistrationWasNotFound() {
            when(registrationRepository.findById(ID)).thenReturn(Optional.empty());

            assertThrows(EntityNotFound.class,
                    () -> registrationService.validateAndSaveCPFFile(ID, REGISTRATION_DOCUMENT_DTO));

            verify(registrationRepository).findById(ID);
        }

        @Test
        void shouldThrowRegistrationRequiredStepNotCompletedWhenRegistrationAddressStepWasNotCompleted() {
            when(registrationRepository.findById(ID))
                    .thenReturn(Optional.of(buildRegistration(RegistrationStatus.INICIADO_INFORMACOES)));

            assertThrows(RegistrationRequiredStepNotCompleted.class,
                    () -> registrationService.validateAndSaveCPFFile(ID, REGISTRATION_DOCUMENT_DTO));

            verify(registrationRepository).findById(ID);
        }

        @Test
        void shouldThrowRegistrationStepAlreadyCompletedWhenRegistrationDocumentStepWasCompleted() {
            when(registrationRepository.findById(ID))
                    .thenReturn(Optional.of(buildRegistration(RegistrationStatus.PENDENTE)));

            assertThrows(RegistrationStepAlreadyCompleted.class,
                    () -> registrationService.validateAndSaveCPFFile(ID, REGISTRATION_DOCUMENT_DTO));

            verify(registrationRepository).findById(ID);
        }

        @Test
        void shouldSaveWhenArgumentsAreValid() {
            RegistrationAddress registrationAddress = new RegistrationAddress(REGISTRATION_ADDRESS_DTO);

            Registration registrationWithAddress = buildRegistration(RegistrationStatus.INICIADO_ENDERECO)
                    .toBuilder()
                    .registrationAddress(registrationAddress)
                    .build();

            registrationAddress.setRegistration(registrationWithAddress);


            when(registrationRepository.findById(ID)).thenReturn(Optional.of(registrationWithAddress));

            when(registrationDocumentService.saveDocument(ID, DOCUMENT)).thenReturn(DOCUMENT);

            RegistrationDocument registrationDocument = new RegistrationDocument(DOCUMENT);
            registrationDocument.setRegistration(registrationWithAddress);

            when(registrationRepository.save(registrationWithAddress)).thenReturn(registrationWithAddress);

            registrationService.validateAndSaveCPFFile(ID, REGISTRATION_DOCUMENT_DTO);

            verify(registrationRepository).findById(ID);
            verify(registrationDocumentService).saveDocument(ID, DOCUMENT);
            verify(registrationRepository).save(registrationWithAddress);
        }
    }

    @Nested
    class GetRegistrationInfo {

        @Test
        void shouldThrowValidationExceptionWhenRegistrationWasNotFound() {
            when(registrationRepository.findById(ID)).thenReturn(Optional.empty());

            assertThrows(EntityNotFound.class,
                    () -> registrationService.getRegistrationInfo(ID));

            verify(registrationRepository).findById(ID);
        }

        @Test
        void shouldThrowRegistrationRequiredStepNotCompletedWhenRegistrationDocumentStepWasNotCompleted() {
            when(registrationRepository.findById(ID)).thenReturn(Optional.of(buildRegistration(RegistrationStatus.INICIADO_ENDERECO)));

            assertThrows(RegistrationRequiredStepNotCompleted.class,
                    () -> registrationService.getRegistrationInfo(ID));

            verify(registrationRepository).findById(ID);
        }

        @Test
        void shouldThrowRegistrationStepAlreadyCompletedWhenRegistrationProposalStepWasCompleted() {
            when(registrationRepository.findById(ID)).thenReturn(Optional.of(buildRegistration(RegistrationStatus.ACEITO)));

            assertThrows(RegistrationStepAlreadyCompleted.class,
                    () -> registrationService.getRegistrationInfo(ID));

            verify(registrationRepository).findById(ID);
        }
    }

    private Registration buildRegistration() {
        return REGISTRATION.toBuilder().build();
    }

    private Registration buildRegistration(RegistrationStatus registrationStatus) {
        return REGISTRATION
                .toBuilder()
                .registrationStatus(registrationStatus)
                .build();
    }
}