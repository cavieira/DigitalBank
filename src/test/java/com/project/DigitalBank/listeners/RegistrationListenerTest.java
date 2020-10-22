package com.project.DigitalBank.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.DigitalBank.dataproviders.ProposalAcceptationDtoProvider;
import com.project.DigitalBank.dtos.ProposalAcceptationDto;
import com.project.DigitalBank.services.RegistrationService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class RegistrationListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private RegistrationListener registrationListener;


    private static final String MESSAGE = "MESSAGE";

    private static final ProposalAcceptationDto PROPOSAL_ACCEPTATION_DTO_ACCEPTED =
            ProposalAcceptationDtoProvider.provideAccepted();

    private static final ProposalAcceptationDto PROPOSAL_ACCEPTATION_DTO_REJECTED =
            ProposalAcceptationDtoProvider.provideRejected();

    @Nested
    class ListenAccepted {

        @Test
        void shouldCallCreateAccount() throws Exception {
            when(objectMapper.readValue(MESSAGE, ProposalAcceptationDto.class))
                    .thenReturn(PROPOSAL_ACCEPTATION_DTO_ACCEPTED);

            doNothing().when(registrationService).createAccount(PROPOSAL_ACCEPTATION_DTO_ACCEPTED);

            registrationListener.listenAccepted(MESSAGE);

            verify(objectMapper).readValue(MESSAGE, ProposalAcceptationDto.class);
            verify(registrationService).createAccount(PROPOSAL_ACCEPTATION_DTO_ACCEPTED);
        }
    }

    @Nested
    class ListenRejected {

        @Test
        void shouldCallSendRejectedEmail() throws Exception {
            when(objectMapper.readValue(MESSAGE, ProposalAcceptationDto.class))
                    .thenReturn(PROPOSAL_ACCEPTATION_DTO_REJECTED);

            doNothing().when(registrationService).sendRejectedEmail(PROPOSAL_ACCEPTATION_DTO_REJECTED);

            registrationListener.listenRejected(MESSAGE);

            verify(objectMapper).readValue(MESSAGE, ProposalAcceptationDto.class);
            verify(registrationService).sendRejectedEmail(PROPOSAL_ACCEPTATION_DTO_REJECTED);
        }
    }
}