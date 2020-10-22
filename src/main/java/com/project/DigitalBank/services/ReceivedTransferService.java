package com.project.DigitalBank.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.DigitalBank.dtos.ReceivedTransferInformationDto;
import com.project.DigitalBank.exceptions.EntityStepAlreadyCompleted;
import com.project.DigitalBank.models.Account;
import com.project.DigitalBank.models.ReceivedTransfer;
import com.project.DigitalBank.repositories.ReceivedTransferRepository;
import com.project.DigitalBank.senders.ReceivedTransferSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReceivedTransferService {

    private final ReceivedTransferSender receivedTransferSender;

    private final ReceivedTransferRepository repository;

    private final AccountService accountService;

    public void processAndSaveNewTransfer(ReceivedTransferInformationDto receivedTransferInformationDto) throws JsonProcessingException {
        receivedTransferSender.sendMessage(receivedTransferInformationDto);

    }

    public void processAndSaveReceivedTransfer(ReceivedTransferInformationDto receivedTransferInformationDto) {
        ReceivedTransfer receivedTransfer = repository.findOneByTransferUniqueCode(receivedTransferInformationDto.getTransferUniqueCode());

        if (receivedTransfer != null) {
            return;
        }

        accountService.validateAndSaveBalance(receivedTransferInformationDto);

        receivedTransfer = ReceivedTransfer.builder()
                .date(receivedTransferInformationDto.getDate())
                .destinationAccountNumber(receivedTransferInformationDto.getDestinationAccountNumber())
                .destinationBranchNumber(receivedTransferInformationDto.getDestinationBranchNumber())
                .destinationCPF(receivedTransferInformationDto.getDestinationCPF())
                .sourceAccountNumber(receivedTransferInformationDto.getSourceAccountNumber())
                .sourceBankCode(receivedTransferInformationDto.getSourceBankCode())
                .sourceBranchNumber(receivedTransferInformationDto.getSourceBranchNumber())
                .sourceCPF(receivedTransferInformationDto.getSourceCPF())
                .transferUniqueCode(receivedTransferInformationDto.getTransferUniqueCode())
                .value(receivedTransferInformationDto.
                        getValue())
                .build();

        repository.save(receivedTransfer);
    }
}
