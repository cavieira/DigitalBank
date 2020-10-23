package com.project.DigitalBank.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.DigitalBank.dtos.*;
import com.project.DigitalBank.enumerations.RegistrationStatus;
import com.project.DigitalBank.exceptions.EntityNotFound;
import com.project.DigitalBank.exceptions.RegistrationDocumentValidationFailed;
import com.project.DigitalBank.exceptions.RegistrationRequiredStepNotCompleted;
import com.project.DigitalBank.exceptions.EntityStepAlreadyCompleted;
import com.project.DigitalBank.models.*;
import com.project.DigitalBank.repositories.RegistrationRepository;
import com.project.DigitalBank.senders.RegistrationSender;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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

    private static final ProposalAcceptationDto PROPOSAL_ACCEPTATION_DTO = ProposalAcceptationDto
            .builder()
            .accept(false)
            .id(ID)
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

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private UUIDService uuidService;

    @Mock
    private RegistrationDocumentService registrationDocumentService;

    @Mock
    private EmailService emailService;

    @Mock
    private DocumentValidationService documentValidationService;

    @Mock
    private RegistrationSender registrationSender;

    @Mock
    private AccountService accountService;

    @Mock
    private UserService userService;


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

            assertThrows(EntityStepAlreadyCompleted.class,
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

            assertThrows(EntityStepAlreadyCompleted.class,
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

            assertThrows(EntityStepAlreadyCompleted.class,
                    () -> registrationService.getRegistrationInfo(ID));

            verify(registrationRepository).findById(ID);
        }

        @Test
        void shouldReturnRegistrationInfoWhenArgumentsAreValid() {
            Registration registration = REGISTRATION
                    .toBuilder()
                    .registrationStatus(RegistrationStatus.PENDENTE)
                    .registrationAddress(RegistrationAddress
                            .builder()
                            .cep(CEP)
                            .rua(RUA)
                            .bairro(BAIRRO)
                            .complemento(COMPLEMENTO)
                            .cidade(CIDADE)
                            .estado(ESTADO)
                            .build())
                    .build();

            when(registrationRepository.findById(ID))
                    .thenReturn(Optional.of(registration));

            RegistrationInformationDto registrationInformationDto = registrationService.getRegistrationInfo(ID);

            verify(registrationRepository).findById(ID);

            assertEquals(REGISTRATION_INFORMATION_DTO, registrationInformationDto);
        }
    }

    @Nested
    class AcceptRegistration {

        @Test
        public void shouldThrowWhenInvalidDocument() {
            Registration registration = REGISTRATION
                    .toBuilder()
                    .registrationStatus(RegistrationStatus.PENDENTE)
                    .registrationAddress(RegistrationAddress
                            .builder()
                            .cep(CEP)
                            .rua(RUA)
                            .bairro(BAIRRO)
                            .complemento(COMPLEMENTO)
                            .cidade(CIDADE)
                            .estado(ESTADO)
                            .build())
                    .build();

            when(registrationRepository.findById(ID))
                    .thenReturn(Optional.of(registration));

            when(documentValidationService.externalApiValidationDocument(registration)).thenReturn(false);

            assertThrows(RegistrationDocumentValidationFailed.class,
                    () -> registrationService.acceptRegistration(PROPOSAL_ACCEPTATION_DTO));

            verify(registrationRepository).findById(ID);
            verify(documentValidationService).externalApiValidationDocument(registration);
        }

        @Test
        public void shouldSaveAndSendAcceptedMessageWhenArgumentsAreValid() throws JsonProcessingException {
            Registration registration = REGISTRATION
                    .toBuilder()
                    .registrationStatus(RegistrationStatus.PENDENTE)
                    .registrationAddress(RegistrationAddress
                            .builder()
                            .cep(CEP)
                            .rua(RUA)
                            .bairro(BAIRRO)
                            .complemento(COMPLEMENTO)
                            .cidade(CIDADE)
                            .estado(ESTADO)
                            .build())
                    .build();

            when(registrationRepository.findById(ID))
                    .thenReturn(Optional.of(registration));

            when(documentValidationService.externalApiValidationDocument(registration)).thenReturn(true);

            when(registrationRepository.save(registration)).thenReturn(registration);
            doNothing().when(registrationSender).sendAcceptedMessage(PROPOSAL_ACCEPTATION_DTO);

            registrationService.acceptRegistration(PROPOSAL_ACCEPTATION_DTO);

            verify(registrationRepository).save(registration);
            verify(registrationSender).sendAcceptedMessage(PROPOSAL_ACCEPTATION_DTO);
        }
    }

    @Nested
    class CreateAccount {

        @Test
        public void shouldThrowEntityNotFoundWhenProposalIsInvalid() {
            when(registrationRepository.findById(ID)).thenReturn(Optional.empty());

            assertThrows(EntityNotFound.class,
                    () -> registrationService.createAccount(PROPOSAL_ACCEPTATION_DTO));

            verify(registrationRepository).findById(ID);
        }

        @Test
        void shouldThrowRegistrationRequiredStepNotCompletedWhenRegistrationStepWasNotCompleted() {
            when(registrationRepository.findById(ID))
                    .thenReturn(Optional.of(buildRegistration(RegistrationStatus.INICIADO_INFORMACOES)));

            assertThrows(RegistrationRequiredStepNotCompleted.class,
                    () -> registrationService.createAccount(PROPOSAL_ACCEPTATION_DTO));

            verify(registrationRepository).findById(ID);
        }

        @Test
        void shouldThrowEntityStepAlreadyCompletedWhenRegistrationStepAlreadyCompleted() {
            when(registrationRepository.findById(ID))
                    .thenReturn(Optional.of(buildRegistration(RegistrationStatus.COMPLETO)));

            assertThrows(EntityStepAlreadyCompleted.class,
                    () -> registrationService.createAccount(PROPOSAL_ACCEPTATION_DTO));

            verify(registrationRepository).findById(ID);
        }

        @Test
        void shouldCreateAccountAndSendConfirmationEmail() {
            when(registrationRepository.findById(ID))
                    .thenReturn(Optional.of(buildRegistration(RegistrationStatus.ACEITO)));

            when(accountService.createAccount()).thenReturn(Account.builder()
                    .branchNumber("0001")
                    .accountNumber("00000002")
                    .user(null)
                    .build());

            when(userService.createUser(any())).thenReturn(User.builder()
                    .firstName(FIRST_NAME)
                    .cpf(CPF)
                    .email(EMAIL)
                    .build());

            registrationService.createAccount(PROPOSAL_ACCEPTATION_DTO);

            verify(registrationRepository).findById(ID);
        }
    }

    @Nested
    class RejectRegistration {

        @Test
        void shouldReturn() throws JsonProcessingException {
            doNothing().when(registrationSender).sendRejectedMessage(PROPOSAL_ACCEPTATION_DTO);

            registrationService.rejectedRegistration(PROPOSAL_ACCEPTATION_DTO);

            verify(registrationSender).sendRejectedMessage(PROPOSAL_ACCEPTATION_DTO);
        }
    }

    @Nested
    class SendRejectedEmail {

        @Test
        public void ShouldSendEmailWhenArgumentsAreValid() {
            Registration registration = REGISTRATION
                    .toBuilder()
                    .registrationStatus(RegistrationStatus.PENDENTE)
                    .registrationAddress(RegistrationAddress
                            .builder()
                            .cep(CEP)
                            .rua(RUA)
                            .bairro(BAIRRO)
                            .complemento(COMPLEMENTO)
                            .cidade(CIDADE)
                            .estado(ESTADO)
                            .build())
                    .build();


            doNothing().when(emailService).sendProposalRetry(REGISTRATION_INFORMATION_DTO);
            when(registrationRepository.findById(anyString()))
                    .thenReturn(Optional.of(registration));

            registrationService.sendRejectedEmail(PROPOSAL_ACCEPTATION_DTO);

            verify(emailService).sendProposalRetry(REGISTRATION_INFORMATION_DTO);
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