package com.project.DigitalBank.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.DigitalBank.dtos.RegistrationInformationDto;
import com.project.DigitalBank.queues.RegistrationQueue;
import com.project.DigitalBank.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationAcceptListener {

    private final ObjectMapper objectMapper;

    private final RegistrationService registrationService;

    @RabbitListener(queues = RegistrationQueue.NAME)
    public void listen(String message) throws JsonProcessingException {
        RegistrationInformationDto registrationInformationDto =
                objectMapper.readValue(message, RegistrationInformationDto.class);
        registrationService.createAccount(registrationInformationDto);
    }
}
