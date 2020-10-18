package com.project.DigitalBank.senders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.DigitalBank.dtos.RegistrationInformationDto;
import com.project.DigitalBank.queues.RegistrationQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationAcceptSender {

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    public void sendMessage(RegistrationInformationDto registrationInformationDto)
            throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(registrationInformationDto);
        rabbitTemplate.convertAndSend(RegistrationQueue.NAME, message);
    }
}
