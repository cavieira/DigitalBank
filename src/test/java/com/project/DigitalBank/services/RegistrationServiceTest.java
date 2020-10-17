package com.project.DigitalBank.services;

import com.project.DigitalBank.dtos.RegistrationDto;
import com.project.DigitalBank.models.Registration;
import com.project.DigitalBank.repositories.RegistrationRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ValidationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class RegistrationServiceTest {

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
    private RegistrationRepository registrationRepository;

    @Mock
    private UUIDService uuidService;

    @InjectMocks
    private RegistrationService registrationService;

    @Nested
    class ValidateAndSaveIdentificationInformation {

        @Test
        void shouldThrowValidationExceptionWhenCpfIsNotUnique() {
            when(registrationRepository.findOneByCpf(CPF)).thenReturn(REGISTRATION);

            assertThrows(ValidationException.class,
                    () -> registrationService.validateAndSaveIdentificationInformation(REGISTRATION_DTO));

            verify(registrationRepository).findOneByCpf(CPF);
        }

        @Test
        void shouldThrowValidationExceptionWhenEmailIsNotUnique() {
            when(registrationRepository.findOneByCpf(CPF)).thenReturn(null);
            when(registrationRepository.findOneByEmail(EMAIL)).thenReturn(REGISTRATION);

            assertThrows(ValidationException.class,
                    () -> registrationService.validateAndSaveIdentificationInformation(REGISTRATION_DTO));

            verify(registrationRepository).findOneByCpf(CPF);
            verify(registrationRepository).findOneByEmail(EMAIL);
        }

        @Test
        void shouldSaveWhenArgumentsAreValid() {
            when(uuidService.generate()).thenReturn(ID);
            when(registrationRepository.save(REGISTRATION)).thenReturn(REGISTRATION);
            when(registrationRepository.findOneByCpf(CPF)).thenReturn(null);
            when(registrationRepository.findOneByEmail(EMAIL)).thenReturn(null);

            String id = registrationService.validateAndSaveIdentificationInformation(REGISTRATION_DTO);

            assertEquals(ID, id);

            verify(uuidService).generate();
            verify(registrationRepository).save(REGISTRATION);
            verify(registrationRepository).findOneByCpf(CPF);
            verify(registrationRepository).findOneByEmail(EMAIL);
        }
    }
}