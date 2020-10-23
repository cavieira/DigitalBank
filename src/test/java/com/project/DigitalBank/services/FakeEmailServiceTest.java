package com.project.DigitalBank.services;

import com.project.DigitalBank.dtos.AccountInformationDto;
import com.project.DigitalBank.dtos.RegistrationDocumentDto;
import com.project.DigitalBank.dtos.RegistrationInformationDto;
import com.project.DigitalBank.dtos.UserTokenDto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class FakeEmailServiceTest {

    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email@email.com";
    private static final LocalDate BIRTH_DATE = LocalDate.of(1970, 1, 1);
    private static final String CPF = "00000000000";
    private static final String CEP = "00000-000";
    private static final String RUA = "rua";
    private static final String BAIRRO = "bairro";
    private static final String COMPLEMENTO = "complemento";
    private static final String CIDADE = "cidade";
    private static final String ESTADO = "estado";

    private static final AccountInformationDto ACCOUNT_INFORMATION_DTO = AccountInformationDto
            .builder()
            .firstName(FIRST_NAME)
            .email(EMAIL)
            .branchNumber("1234")
            .accountNumber("12345678")
            .build();



    private static final RegistrationInformationDto REGISTRATION_INFORMATION_DTO = RegistrationInformationDto
            .builder()
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .email(EMAIL)
            .birthDate(BIRTH_DATE)
            .cpf(CPF)
            .cep(CEP)
            .rua(RUA)
            .bairro(BAIRRO)
            .complemento(COMPLEMENTO)
            .cidade(CIDADE)
            .estado(ESTADO)
            .build();

    private static final UserTokenDto USER_TOKEN_DTO = UserTokenDto
            .builder()
            .firstName(FIRST_NAME)
            .email(EMAIL)
            .firstAccessToken("123456")
            .build();

    @Mock
    private Logger logger;

    @InjectMocks
    private FakeEmailService fakeEmailService;

    @Nested
    class SendAccountCreated {

        @Test
        void shouldLogWhenInformationIsValid() {
            doNothing().when(logger).info(String.valueOf(ACCOUNT_INFORMATION_DTO));

            fakeEmailService.sendAccountCreated(ACCOUNT_INFORMATION_DTO);

            verify(logger).info(String.valueOf(ACCOUNT_INFORMATION_DTO));
        }
    }

    @Nested
    class SendProposalRetry {
        @Test
        void shouldLogWhenInformationIsValid() {
            doNothing().when(logger).info(String.valueOf(REGISTRATION_INFORMATION_DTO));

            fakeEmailService.sendProposalRetry(REGISTRATION_INFORMATION_DTO);

            verify(logger).info(String.valueOf(REGISTRATION_INFORMATION_DTO));
        }
    }

    @Nested
    class SendUserToken {
        @Test
        void shouldLogWhenInformationIsValid() {
            doNothing().when(logger).info(String.valueOf(USER_TOKEN_DTO));

            fakeEmailService.sendUserToken(USER_TOKEN_DTO);

            verify(logger).info(String.valueOf(USER_TOKEN_DTO));
        }
    }
}