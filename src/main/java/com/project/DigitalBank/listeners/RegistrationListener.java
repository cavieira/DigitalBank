package com.project.DigitalBank.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.DigitalBank.dtos.ProposalAcceptationDto;
import com.project.DigitalBank.queues.RegistrationQueue;
import com.project.DigitalBank.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationListener {

    private final ObjectMapper objectMapper;

    private final RegistrationService registrationService;

    private ProposalAcceptationDto deserialize(String message) throws JsonProcessingException {
        return objectMapper.readValue(message, ProposalAcceptationDto.class);
    }

    @RabbitListener(queues = RegistrationQueue.ACCEPTED)
    public void listenAccepted(String message) throws JsonProcessingException {
        registrationService.createAccount(deserialize(message));
    }

    @RabbitListener(queues = RegistrationQueue.REJECTED)
    public void listenRejected(String message) throws JsonProcessingException {
        registrationService.sendRejectedEmail(deserialize(message));
    }
}

