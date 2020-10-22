package com.project.DigitalBank.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.DigitalBank.dataproviders.ReceivedTransferInformationDtoProvider;
import com.project.DigitalBank.dtos.ReceivedTransferInformationDto;
import com.project.DigitalBank.services.ReceivedTransferService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ReceivedTransferListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ReceivedTransferService receivedTransferService;

    @InjectMocks
    private ReceivedTransferListener receivedTransferListener;


    private static final String MESSAGE = "MESSAGE";

    private static final ReceivedTransferInformationDto RECEIVED_TRANSFER_INFORMATION_DTO =
            ReceivedTransferInformationDtoProvider.provide();

    @Nested
    class ListenReceivedTransfer {

        @Test
        void shouldCallProcessAndSaveReceivedTransfer() throws Exception {
            when(objectMapper.readValue(MESSAGE, ReceivedTransferInformationDto.class))
                    .thenReturn(RECEIVED_TRANSFER_INFORMATION_DTO);

            doNothing().when(receivedTransferService)
                    .processAndSaveReceivedTransfer(RECEIVED_TRANSFER_INFORMATION_DTO);

            receivedTransferListener.listenReceivedTransfer(MESSAGE);

            verify(objectMapper).readValue(MESSAGE, ReceivedTransferInformationDto.class);
            verify(receivedTransferService).processAndSaveReceivedTransfer(RECEIVED_TRANSFER_INFORMATION_DTO);
        }
    }
}