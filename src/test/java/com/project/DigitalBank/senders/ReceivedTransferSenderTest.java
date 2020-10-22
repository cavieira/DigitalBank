package com.project.DigitalBank.senders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.DigitalBank.dataproviders.ReceivedTransferInformationDtoProvider;
import com.project.DigitalBank.dtos.ReceivedTransferInformationDto;
import com.project.DigitalBank.listeners.ReceivedTransferListener;
import com.project.DigitalBank.queues.ReceivedTransferQueue;
import com.project.DigitalBank.services.ReceivedTransferService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ReceivedTransferSenderTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ReceivedTransferSender receivedTransferSender;


    private static final String MESSAGE = "MESSAGE";

    private static final ReceivedTransferInformationDto RECEIVED_TRANSFER_INFORMATION_DTO =
            ReceivedTransferInformationDtoProvider.provide();

    @Nested
    class ListenReceivedTransfer {

        @Test
        void shouldCallSerializeConvertAndSend() throws Exception {
            when(objectMapper.writeValueAsString(RECEIVED_TRANSFER_INFORMATION_DTO))
                    .thenReturn(MESSAGE);

            doNothing().when(rabbitTemplate)
                    .convertAndSend(ReceivedTransferQueue.RECEIVED_TRANSFER, MESSAGE);

            receivedTransferSender.sendMessage(RECEIVED_TRANSFER_INFORMATION_DTO);

            verify(objectMapper).writeValueAsString(RECEIVED_TRANSFER_INFORMATION_DTO);
            verify(rabbitTemplate).convertAndSend(ReceivedTransferQueue.RECEIVED_TRANSFER, MESSAGE);
        }
    }
}