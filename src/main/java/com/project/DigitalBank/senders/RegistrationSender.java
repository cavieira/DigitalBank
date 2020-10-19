package com.project.DigitalBank.senders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.DigitalBank.dtos.ProposalAcceptationDto;
import com.project.DigitalBank.dtos.RegistrationInformationDto;
import com.project.DigitalBank.queues.RegistrationQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationSender {

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    private void sendMessage(String queueName, ProposalAcceptationDto proposalAcceptationDto)
            throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(proposalAcceptationDto);
        rabbitTemplate.convertAndSend(queueName, message);
    }

    public void sendAcceptedMessage(ProposalAcceptationDto proposalAcceptationDto)
            throws JsonProcessingException {
        sendMessage(RegistrationQueue.ACCEPTED, proposalAcceptationDto);
    }

    public void sendRejectedMessage(ProposalAcceptationDto proposalAcceptationDto)
            throws JsonProcessingException {
        sendMessage(RegistrationQueue.REJECTED, proposalAcceptationDto);
    }
}
