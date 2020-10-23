package com.project.DigitalBank.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.DigitalBank.dtos.ReceivedTransferInformationDto;
import com.project.DigitalBank.exceptions.EntityNotFound;
import com.project.DigitalBank.models.ReceivedTransfer;
import com.project.DigitalBank.repositories.ReceivedTransferRepository;
import com.project.DigitalBank.senders.ReceivedTransferSender;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ReceivedTransferServiceTest {

    @Mock
    private ReceivedTransferSender receivedTransferSender;

    @Mock
    private ReceivedTransferRepository repository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private ReceivedTransferService receivedTransferService;

    private static final ReceivedTransferInformationDto RECEIVED_TRANSFER_INFORMATION_DTO =
            ReceivedTransferInformationDto
                    .builder()
                    .date(LocalDateTime.parse("2020-10-22T17:01:51.858343100"))
                    .destinationBranchNumber("1234")
                    .destinationAccountNumber("12345678")
                    .destinationCPF("00011122233")
                    .sourceBankCode("123")
                    .sourceBranchNumber("1234")
                    .sourceAccountNumber("12345678")
                    .sourceCPF("11122233344")
                    .transferUniqueCode("id")
                    .value(BigDecimal.valueOf(10.0))
                    .build();

    private static final ReceivedTransfer RECEIVED_TRANSFER = ReceivedTransfer.builder()
            .date(LocalDateTime.parse("2020-10-22T17:01:51.858343100"))
            .destinationBranchNumber("1234")
            .destinationAccountNumber("12345678")
            .destinationCPF("00011122233")
            .sourceBankCode("123")
            .sourceBranchNumber("1234")
            .sourceAccountNumber("12345678")
            .sourceCPF("11122233344")
            .transferUniqueCode("id")
            .value(BigDecimal.valueOf(10.0))
            .build();

    @Nested
    class ProcessAndSaveNewTransfer {

        @Test
        void shouldCallSenderSendMessage() throws JsonProcessingException {
            doNothing().when(receivedTransferSender).sendMessage(RECEIVED_TRANSFER_INFORMATION_DTO);

            receivedTransferService.processAndSaveNewTransfer(RECEIVED_TRANSFER_INFORMATION_DTO);

            verify(receivedTransferSender).sendMessage(RECEIVED_TRANSFER_INFORMATION_DTO);
        }
    }

    @Nested
    class ProcessAndSaveReceivedTransfer {

        @Test
        public void shouldReturnWhenReceivedTransferAlreadyExists() {
            when(repository.findOneByTransferUniqueCode(RECEIVED_TRANSFER_INFORMATION_DTO.getTransferUniqueCode()))
                    .thenReturn(ReceivedTransfer.builder()
                            .value(BigDecimal.ZERO)
                            .build());

            receivedTransferService.processAndSaveReceivedTransfer(RECEIVED_TRANSFER_INFORMATION_DTO);

            verify(accountService, never()).validateAndSaveBalance(any());
        }

        @Test
        public void shouldReturnWhenThrowValidateAndSaveBalance() {
            doThrow(EntityNotFound.class).when(accountService).validateAndSaveBalance(RECEIVED_TRANSFER_INFORMATION_DTO);

            receivedTransferService.processAndSaveReceivedTransfer(RECEIVED_TRANSFER_INFORMATION_DTO);

            verify(repository, never()).save(any());
        }

        @Test
        void shouldSaveReceivedTransferWhenInformationIsValid() {
            when(repository.findOneByTransferUniqueCode(RECEIVED_TRANSFER_INFORMATION_DTO.getTransferUniqueCode()))
                    .thenReturn(null);

            doNothing().when(accountService).validateAndSaveBalance(RECEIVED_TRANSFER_INFORMATION_DTO);

            when(repository.save(RECEIVED_TRANSFER)).thenReturn(RECEIVED_TRANSFER);

            receivedTransferService.processAndSaveReceivedTransfer(RECEIVED_TRANSFER_INFORMATION_DTO);

            verify(repository).findOneByTransferUniqueCode(RECEIVED_TRANSFER_INFORMATION_DTO.getTransferUniqueCode());
            verify(accountService).validateAndSaveBalance(RECEIVED_TRANSFER_INFORMATION_DTO);
            verify(repository).save(RECEIVED_TRANSFER);
        }
    }
}