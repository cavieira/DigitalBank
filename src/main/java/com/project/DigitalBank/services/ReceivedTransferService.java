package com.project.DigitalBank.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.DigitalBank.dtos.ReceivedTransferInformationDto;
import com.project.DigitalBank.exceptions.EntityNotFound;
import com.project.DigitalBank.exceptions.UnprocessableOperation;
import com.project.DigitalBank.models.ReceivedTransfer;
import com.project.DigitalBank.repositories.ReceivedTransferRepository;
import com.project.DigitalBank.senders.ReceivedTransferSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
@Transactional
public class ReceivedTransferService {

    private final ReceivedTransferSender receivedTransferSender;

    private final ReceivedTransferRepository repository;

    private final AccountService accountService;

    Logger logger = Logger.getLogger(ReceivedTransferService.class.getName());

    public void processAndSaveNewTransfer(ReceivedTransferInformationDto receivedTransferInformationDto) throws JsonProcessingException {
        receivedTransferSender.sendMessage(receivedTransferInformationDto);
    }

    public void processAndSaveReceivedTransfer(ReceivedTransferInformationDto receivedTransferInformationDto) {
        ReceivedTransfer receivedTransfer = repository.findOneByTransferUniqueCode(receivedTransferInformationDto.getTransferUniqueCode());

        if (receivedTransfer != null) {
            logger.info("Transferência já processada.");
            return;
        }

        try {
            accountService.validateAndSaveBalance(receivedTransferInformationDto);
        } catch (EntityNotFound | UnprocessableOperation e) {
            logger.info(e.getMessage());
            return;
        }

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
