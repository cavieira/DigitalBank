package com.project.DigitalBank.senders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.DigitalBank.dtos.ReceivedTransferInformationDto;
import com.project.DigitalBank.queues.ReceivedTransferQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReceivedTransferSender {

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    public void sendMessage(ReceivedTransferInformationDto receivedTransferInformationDto)
            throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(receivedTransferInformationDto);
        rabbitTemplate.convertAndSend(ReceivedTransferQueue.RECEIVED_TRANSFER, message);
    }

}
