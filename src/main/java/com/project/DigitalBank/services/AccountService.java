package com.project.DigitalBank.services;

import com.project.DigitalBank.dtos.ReceivedTransferInformationDto;
import com.project.DigitalBank.exceptions.EntityNotFound;
import com.project.DigitalBank.exceptions.UnprocessableOperation;
import com.project.DigitalBank.models.Account;
import com.project.DigitalBank.models.Registration;
import com.project.DigitalBank.repositories.AccountRepository;
import com.project.DigitalBank.repositories.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository repository;

    private final Random random;

    private String createAccountNumber() {
        String accountNumber = String.format("%08d", random.nextInt(100000000));

        if (repository.findOneByAccountNumber(accountNumber) == null) {
            return accountNumber;
        }

        return createAccountNumber();
    }

    public Account createAccount() {
        String accountNumber = createAccountNumber();
        String branchNumber = String.format("%04d", random.nextInt(10000));
        String bankCode = "341";

        return Account.builder()
                .branchNumber(branchNumber)
                .accountNumber(accountNumber)
                .bankCode(bankCode)
                .balance(BigDecimal.ZERO)
                .build();
    }

    public void validateAndSaveBalance(ReceivedTransferInformationDto receivedTransferInformationDto) {
        Account account = repository.findOneByAccountNumber(receivedTransferInformationDto.getDestinationAccountNumber());

        if (account == null) {
            throw new EntityNotFound("Conta não existente");
        }

        if (!account.getBranchNumber().equals(receivedTransferInformationDto.getDestinationBranchNumber())) {
            throw new UnprocessableOperation("Agência não é a mesma do cadastro");
        }

        if (!account.getUser().getCpf().equals(receivedTransferInformationDto.getDestinationCPF())) {
            throw new UnprocessableOperation("CPF não é o mesmo do cadastro");
        }

        account.setBalance(account.getBalance().add(receivedTransferInformationDto.getValue()));

        repository.save(account);
    }
}
