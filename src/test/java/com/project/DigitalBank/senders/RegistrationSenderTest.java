package com.project.DigitalBank.senders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.DigitalBank.dataproviders.ProposalAcceptationDtoProvider;
import com.project.DigitalBank.dataproviders.ReceivedTransferInformationDtoProvider;
import com.project.DigitalBank.dtos.ProposalAcceptationDto;
import com.project.DigitalBank.dtos.ReceivedTransferInformationDto;
import com.project.DigitalBank.queues.ReceivedTransferQueue;
import com.project.DigitalBank.queues.RegistrationQueue;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class RegistrationSenderTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RegistrationSender registrationSender;


    private static final String MESSAGE = "MESSAGE";

    private static final ProposalAcceptationDto PROPOSAL_ACCEPTATION_DTO_TRUE =
            ProposalAcceptationDtoProvider.provideAccepted();

    private static final ProposalAcceptationDto PROPOSAL_ACCEPTATION_DTO_FALSE =
            ProposalAcceptationDtoProvider.provideAccepted();

    @Nested
    class SendAcceptedMessage {

        @Test
        void shouldCallSerializeConvertAndSend() throws Exception {
            when(objectMapper.writeValueAsString(PROPOSAL_ACCEPTATION_DTO_TRUE))
                    .thenReturn(MESSAGE);

            doNothing().when(rabbitTemplate)
                    .convertAndSend(RegistrationQueue.ACCEPTED, MESSAGE);

            registrationSender.sendAcceptedMessage(PROPOSAL_ACCEPTATION_DTO_TRUE);

            verify(objectMapper).writeValueAsString(PROPOSAL_ACCEPTATION_DTO_TRUE);
            verify(rabbitTemplate).convertAndSend(RegistrationQueue.ACCEPTED, MESSAGE);
        }
    }

    @Nested
    class SendRejectedMessage {

        @Test
        void shouldCallSerializeConvertAndSend() throws Exception {
            when(objectMapper.writeValueAsString(PROPOSAL_ACCEPTATION_DTO_FALSE))
                    .thenReturn(MESSAGE);

            doNothing().when(rabbitTemplate)
                    .convertAndSend(RegistrationQueue.REJECTED, MESSAGE);

            registrationSender.sendRejectedMessage(PROPOSAL_ACCEPTATION_DTO_FALSE);

            verify(objectMapper).writeValueAsString(PROPOSAL_ACCEPTATION_DTO_FALSE);
            verify(rabbitTemplate).convertAndSend(RegistrationQueue.REJECTED, MESSAGE);
        }
    }
}