package com.project.DigitalBank.controllers;

import com.project.DigitalBank.dtos.RegistrationDto;
import com.project.DigitalBank.models.Registration;
import com.project.DigitalBank.services.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class RegistrationControllerTest {

    private static final String ID = "id";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email@email.com";
    private static final LocalDate BIRTH_DATE = LocalDate.of(1970, 1, 1);
    private static final String CPF = "00000000000";

    private static final RegistrationDto REGISTRATION_DTO = RegistrationDto
            .builder()
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .email(EMAIL)
            .birthDate(BIRTH_DATE)
            .cpf(CPF)
            .build();

    private static final Registration REGISTRATION = new Registration(ID, REGISTRATION_DTO);

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private RegistrationController registrationController;

    private MockHttpServletRequest mockRequest;

    @BeforeEach
    public void setUp() {
        mockRequest = new MockHttpServletRequest();
        mockRequest.setContextPath("/your-app-context");

        ServletRequestAttributes attrs = new ServletRequestAttributes(mockRequest);

        RequestContextHolder.setRequestAttributes(attrs);
    }

    @Nested
    class beginRegistration {

        @Test
        void shouldReturnOkWhenArgumentsAreValid() {
            when(registrationService.validateAndSaveIdentificationInformation(REGISTRATION_DTO)).thenReturn(ID);

            ResponseEntity<String> id = registrationController.beginRegistration(REGISTRATION_DTO);

            verify(registrationService).validateAndSaveIdentificationInformation(REGISTRATION_DTO);
        }
    }
}