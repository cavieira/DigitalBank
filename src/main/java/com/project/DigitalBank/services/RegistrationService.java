package com.project.DigitalBank.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.DigitalBank.dtos.*;
import com.project.DigitalBank.enumerations.RegistrationStatus;
import com.project.DigitalBank.exceptions.RegistrationDocumentValidationFailed;
import com.project.DigitalBank.exceptions.EntityNotFound;
import com.project.DigitalBank.exceptions.RegistrationRequiredStepNotCompleted;
import com.project.DigitalBank.exceptions.RegistrationStepAlreadyCompleted;
import com.project.DigitalBank.models.*;
import com.project.DigitalBank.repositories.RegistrationRepository;
import com.project.DigitalBank.senders.RegistrationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UUIDService uuidService;

    private final RegistrationRepository repository;

    private final RegistrationDocumentService registrationDocumentService;

    private final DocumentValidationService documentValidationService;

    private final RegistrationSender registrationSender;

    private final EmailService emailService;

    private final AccountService accountService;

    private final UserService userService;

    public String validateAndSaveIdentificationInformation(RegistrationDto registrationDto) {
        if (repository.findOneByCpf(registrationDto.getCpf()) != null) {
            throw new ValidationException("CPF já registrado! Favor informar o CPF correto.");
        }

        if (repository.findOneByEmail(registrationDto.getEmail()) != null) {
            throw new ValidationException("Email já registrado! Favor informar o email correto.");
        }

        final String id = uuidService.generate();

        Registration registration = new Registration(id, registrationDto);

        registration.setRegistrationStatus(RegistrationStatus.INICIADO_INFORMACOES);

        repository.save(registration);

        return id;
    }

    public void validateAndSaveAddressInformation(String id, RegistrationAddressDto registrationAddressDto) {
        Registration registration = getRegistrationWhenIdentificationInformationSent(id);
        RegistrationAddress registrationAddress = new RegistrationAddress(registrationAddressDto);

        registration.setRegistrationAddress(registrationAddress);

        registration.setRegistrationStatus(RegistrationStatus.INICIADO_ENDERECO);
        registrationAddress.setRegistration(registration);

        repository.save(registration);
    }

    public void validateAndSaveCPFFile(String id, RegistrationDocumentDto registrationDocumentDto) {
        Registration registration = getRegistrationWhenAddressSent(id);

        String document = registrationDocumentService.saveDocument(id, registrationDocumentDto.getDocument());

        RegistrationDocument registrationDocument = new RegistrationDocument(document);

        registration.setRegistrationDocument(registrationDocument);
        registrationDocument.setRegistration(registration);

        registration.setRegistrationStatus(RegistrationStatus.PENDENTE);

        repository.save(registration);
    }

    public RegistrationInformationDto getRegistrationInfo(String id) {
        Registration registration = getRegistrationWhenIdentificationDocumentSent(id);
        var address = registration.getRegistrationAddress();

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

    public void acceptRegistration(ProposalAcceptationDto proposalAcceptationDto)
            throws JsonProcessingException {
        Registration registration = getRegistrationWhenIdentificationDocumentSent(proposalAcceptationDto.getId());

        // Verifica se o sistema externo aceita as informações do CPF
        if (!documentValidationService.externalApiValidationDocument(registration)) {
            registration.setRegistrationStatus(RegistrationStatus.PENDENTE_VALIDACAO);

            repository.save(registration);

            throw new RegistrationDocumentValidationFailed("Validação de CPF pendente.");
        }

        registration.setRegistrationStatus(RegistrationStatus.ACEITO);

        repository.save(registration);

        registrationSender.sendAcceptedMessage(proposalAcceptationDto);
    }

    public void createAccount(ProposalAcceptationDto proposalAcceptationDto) {
        //Busca proposta
        Registration registration = getRegistrationWhenProposalAccepted(proposalAcceptationDto.getId());

        Account account = accountService.createAccount(registration);
        User user = userService.createUser(registration);

        user.setAccount(account);

        account.setUser(user);
        account.setRegistration(registration);

        registration.setAccount(account);
        registration.setRegistrationStatus(RegistrationStatus.COMPLETO);

        repository.save(registration);

        // Mandar email de confirmação
        emailService.sendAccountCreated(AccountInformationDto.builder()
                .firstName(registration.getFirstName())
                .email(registration.getEmail())
                .accountNumber(account.getAccountNumber())
                .branchNumber(account.getBranchNumber())
                .build());
    }

    public void rejectedRegistration(ProposalAcceptationDto proposalAcceptationDto)
            throws JsonProcessingException {
        // Mandar email insistindo
        registrationSender.sendRejectedMessage(proposalAcceptationDto);
    }

    public void sendRejectedEmail(ProposalAcceptationDto proposalAcceptationDto) {
        // Mandar email implorando para aceitar proposta
        RegistrationInformationDto registrationInformationDto = getRegistrationInfo(proposalAcceptationDto.getId());

        emailService.sendProposalRetry(registrationInformationDto);
    }

    private Registration getRegistrationWhenIdentificationInformationSent(String id) {
        var queryResult = repository.findById(id);

        if (queryResult.isEmpty()) {
            throw new EntityNotFound("Favor cadastrar as informações básicas antes de cadastrar o endereço.");
        }

        var registration = queryResult.get();

        if (registration.getRegistrationStatus().after(RegistrationStatus.INICIADO_INFORMACOES)) {
            throw new RegistrationStepAlreadyCompleted("Você já cadastrou o endereço anteriormente");
        }

        return registration;
    }

    private Registration getRegistrationWhenAddressSent(String id) {
        var queryResult = repository.findById(id);

        if (queryResult.isEmpty()) {
            throw new EntityNotFound("Favor cadastrar as informações básicas antes de enviar o CPF.");
        }

        var registration = queryResult.get();

        if (registration.getRegistrationStatus().before(RegistrationStatus.INICIADO_ENDERECO)) {
            throw new RegistrationRequiredStepNotCompleted("Você precisa cadastrar suas informações pessoais");
        }

        if (registration.getRegistrationStatus().after(RegistrationStatus.INICIADO_ENDERECO)) {
            throw new RegistrationStepAlreadyCompleted("Você já enviou seu CPF");
        }

        return registration;
    }

    private Registration getRegistrationWhenIdentificationDocumentSent(String id) {
        // Verify if registration, registrationAddress and registrationDocument are valid
        var queryResult = repository.findById(id);

        if (queryResult.isEmpty()) {
            throw new EntityNotFound("Favor cadastrar as informações básicas.");
        }

        var registration = queryResult.get();

        if (registration.getRegistrationStatus().before(RegistrationStatus.PENDENTE)) {
            throw new RegistrationRequiredStepNotCompleted("Você precisa enviar seu documento");
        }

        if (registration.getRegistrationStatus().after(RegistrationStatus.PENDENTE)) {
            throw new RegistrationStepAlreadyCompleted("Você já aceitou a proposta");
        }

        return registration;
    }

    private Registration getRegistrationWhenProposalAccepted(String id) {
        // Verify if registration, registrationAddress and registrationDocument are valid
        var queryResult = repository.findById(id);

        if (queryResult.isEmpty()) {
            throw new EntityNotFound("Favor cadastrar as informações básicas.");
        }

        var registration = queryResult.get();

        if (registration.getRegistrationStatus().before(RegistrationStatus.ACEITO)) {
            throw new RegistrationRequiredStepNotCompleted("Você precisa aceitar a proposta");
        }

        if (registration.getRegistrationStatus().after(RegistrationStatus.ACEITO)) {
            throw new RegistrationStepAlreadyCompleted("Você já completou a sua proposta");
        }

        return registration;
    }
}
