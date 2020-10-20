package com.project.DigitalBank.services;

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

    public Account createAccount(Registration registration) {
        //Criar conta
        Random random = new Random();
        String branchNumber = String.format("%04d", random.nextInt(10000));
        String accountNumber = String.format("%08d", random.nextInt(100000000));
        String bankCode = "341";

        return Account.builder()
                .branchNumber(branchNumber)
                .accountNumber(accountNumber)
                .bankCode(bankCode)
                .balance(BigDecimal.ZERO)
                .build();
    }
}
