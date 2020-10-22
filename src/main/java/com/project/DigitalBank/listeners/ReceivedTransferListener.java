package com.project.DigitalBank.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.DigitalBank.dtos.ReceivedTransferInformationDto;
import com.project.DigitalBank.queues.ReceivedTransferQueue;
import com.project.DigitalBank.services.ReceivedTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReceivedTransferListener {

    private final ObjectMapper objectMapper;

    private final ReceivedTransferService receivedTransferService;

    private ReceivedTransferInformationDto deserialize(String message) throws JsonProcessingException {
        return objectMapper.readValue(message, ReceivedTransferInformationDto.class);
    }

    @RabbitListener(queues = ReceivedTransferQueue.RECEIVED_TRANSFER)
    public void listenReceivedTransfer(String message) throws JsonProcessingException {
        receivedTransferService.processAndSaveReceivedTransfer(deserialize(message));
    }

}
