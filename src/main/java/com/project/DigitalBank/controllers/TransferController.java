package com.project.DigitalBank.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.DigitalBank.dtos.ReceivedTransferInformationDto;
import com.project.DigitalBank.services.ReceivedTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransferController {

    private final ReceivedTransferService receivedTransferService;

    @PostMapping("/transfer/")
    public ResponseEntity<String> newTransfer(ReceivedTransferInformationDto receivedTransferInformationDto) throws JsonProcessingException {
        receivedTransferService.processAndSaveNewTransfer(receivedTransferInformationDto);

        return ResponseEntity.ok().build();
    }
}
