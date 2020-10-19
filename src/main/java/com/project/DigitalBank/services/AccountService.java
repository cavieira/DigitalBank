package com.project.DigitalBank.services;

import com.project.DigitalBank.dtos.AccountInformationDto;
import com.project.DigitalBank.enumerations.RegistrationStatus;
import com.project.DigitalBank.models.Account;
import com.project.DigitalBank.models.Registration;
import com.project.DigitalBank.repositories.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final RegistrationRepository repository;

    private final EmailService emailService;

    public void createAccount(Registration registration) {
        //Criar conta
        Random random = new Random();
        String branchNumber = String.format("%04d", random.nextInt(10000));
        String accountNumber = String.format("%08d", random.nextInt(100000000));
        String bankCode = "341";

        Account account = Account.builder()
                .branchNumber(branchNumber)
                .accountNumber(accountNumber)
                .bankCode(bankCode)
                .balance(BigDecimal.ZERO)
                .build();

        registration.setAccount(account);
        account.setRegistration(registration);

        registration.setRegistrationStatus(RegistrationStatus.COMPLETO);

        repository.save(registration);

        AccountInformationDto accountInformationDto = AccountInformationDto.builder()
                .firstName(registration.getFirstName())
                .email(registration.getEmail())
                .accountNumber(accountNumber)
                .branchNumber(branchNumber)
                .build();

        // Mandar email de confirmação
        emailService.sendAccountCreated(accountInformationDto);
    }
}
