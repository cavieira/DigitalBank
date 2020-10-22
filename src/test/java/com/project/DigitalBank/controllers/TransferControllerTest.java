package com.project.DigitalBank.controllers;

import com.project.DigitalBank.dataproviders.ReceivedTransferInformationDtoProvider;
import com.project.DigitalBank.dtos.ReceivedTransferInformationDto;
import com.project.DigitalBank.services.ReceivedTransferService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;


@ExtendWith(SpringExtension.class)
class TransferControllerTest extends BaseControllerTest {

    @Mock
    private ReceivedTransferService receivedTransferService;

    @InjectMocks
    private TransferController transferController;

    private static final ReceivedTransferInformationDto RECEIVED_TRANSFER_INFORMATION_DTO =
            ReceivedTransferInformationDtoProvider.provide();

    @Nested
    class NewTransfer {

        @Test
        void shouldCallProcessAndSaveNewTransfer() throws Exception {
            doNothing().when(receivedTransferService)
                    .processAndSaveNewTransfer(RECEIVED_TRANSFER_INFORMATION_DTO);

            ResponseEntity<String> response = transferController.newTransfer(RECEIVED_TRANSFER_INFORMATION_DTO);

            assertEquals(ResponseEntity.ok().build(), response);

            verify(receivedTransferService).processAndSaveNewTransfer(RECEIVED_TRANSFER_INFORMATION_DTO);
        }
    }
}