package com.project.DigitalBank.services;

import com.project.DigitalBank.dataproviders.ReceivedTransferInformationDtoProvider;
import com.project.DigitalBank.dtos.ReceivedTransferInformationDto;
import com.project.DigitalBank.exceptions.EntityNotFound;
import com.project.DigitalBank.exceptions.UnprocessableOperation;
import com.project.DigitalBank.models.Account;
import com.project.DigitalBank.models.User;
import com.project.DigitalBank.repositories.AccountRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class AccountServiceTest {

    private static final ReceivedTransferInformationDto RECEIVED_TRANSFER_INFORMATION_DTO =
            ReceivedTransferInformationDtoProvider.provide();

    private static final Account ACCOUNT = Account
            .builder()
            .accountNumber(RECEIVED_TRANSFER_INFORMATION_DTO.getDestinationAccountNumber())
            .branchNumber(RECEIVED_TRANSFER_INFORMATION_DTO.getDestinationBranchNumber())
            .bankCode("341")
            .balance(BigDecimal.ZERO)
            .user(User
                    .builder()
                    .cpf(RECEIVED_TRANSFER_INFORMATION_DTO.getDestinationCPF())
                    .build())
            .build();

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private Random random;

    @InjectMocks
    private AccountService accountService;

    @Nested
    class CreateAccount {

        @Test
        void shouldCreateAccountWithBalanceZeroed() {
            when(random.nextInt(100000000)).thenReturn(1, 2);
            when(accountRepository.findOneByAccountNumber(anyString()))
                    .thenReturn(ACCOUNT, (Account) null);

            when(random.nextInt(10000)).thenReturn(1);

            Account response = accountService.createAccount();

            Account expected = ACCOUNT
                    .toBuilder()
                    .branchNumber("0001")
                    .accountNumber("00000002")
                    .user(null)
                    .build();

            assertEquals(expected, response);

            verify(random, times(2)).nextInt(100000000);
            verify(accountRepository).findOneByAccountNumber("00000001");
            verify(accountRepository).findOneByAccountNumber("00000002");
            verify(random).nextInt(10000);
        }
    }

    @Nested
    class ValidateAndSaveBalance {

        @Test
        void shouldThrowEntityNotFoundWhenAccountDoesNotExists() {
            when(accountRepository.findOneByAccountNumber(ACCOUNT.getAccountNumber()))
                    .thenReturn(null);

            assertThrows(EntityNotFound.class,
                    () -> accountService.validateAndSaveBalance(RECEIVED_TRANSFER_INFORMATION_DTO));

            verify(accountRepository).findOneByAccountNumber(ACCOUNT.getAccountNumber());
        }

        @Test
        void shouldThrowUnprocessableOperationWhenBranchNumberIsNotEqual() {
            when(accountRepository.findOneByAccountNumber(ACCOUNT.getAccountNumber()))
                    .thenReturn(ACCOUNT.toBuilder().branchNumber("outro").build());

            assertThrows(UnprocessableOperation.class,
                    () -> accountService.validateAndSaveBalance(RECEIVED_TRANSFER_INFORMATION_DTO));

            verify(accountRepository).findOneByAccountNumber(ACCOUNT.getAccountNumber());
        }

        @Test
        void shouldThrowUnprocessableOperationWhenCPFIsNotEqual() {
            when(accountRepository.findOneByAccountNumber(ACCOUNT.getAccountNumber()))
                    .thenReturn(ACCOUNT
                            .toBuilder()
                            .user(ACCOUNT.getUser()
                                    .toBuilder()
                                    .cpf("outro")
                                    .build())
                            .build());

            assertThrows(UnprocessableOperation.class,
                    () -> accountService.validateAndSaveBalance(RECEIVED_TRANSFER_INFORMATION_DTO));

            verify(accountRepository).findOneByAccountNumber(ACCOUNT.getAccountNumber());
        }

        @Test
        void shouldCallUpdateAndSaveWhenInformationIsValid() {
            Account account = ACCOUNT
                    .toBuilder()
                    .balance(BigDecimal.ZERO)
                    .build();

            Account accountUpdated = account
                    .toBuilder()
                    .balance(RECEIVED_TRANSFER_INFORMATION_DTO.getValue())
                    .build();

            when(accountRepository.findOneByAccountNumber(ACCOUNT.getAccountNumber()))
                    .thenReturn(account);

            when(accountRepository.save(accountUpdated)).thenReturn(accountUpdated);

            accountService.validateAndSaveBalance(RECEIVED_TRANSFER_INFORMATION_DTO);

            verify(accountRepository).findOneByAccountNumber(ACCOUNT.getAccountNumber());
            verify(accountRepository).save(accountUpdated);
        }
    }
}