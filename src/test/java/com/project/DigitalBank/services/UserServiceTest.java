package com.project.DigitalBank.services;

import com.project.DigitalBank.dtos.TokenInformationDto;
import com.project.DigitalBank.dtos.UserInformationDto;
import com.project.DigitalBank.dtos.UserTokenDto;
import com.project.DigitalBank.exceptions.EntityNotFound;
import com.project.DigitalBank.models.Registration;
import com.project.DigitalBank.models.User;
import com.project.DigitalBank.models.UserToken;
import com.project.DigitalBank.repositories.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private EmailService emailService;

    @Mock
    private UserTokenService userTokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private static final UserInformationDto USER_INFORMATION_DTO = UserInformationDto
            .builder()
            .email("email@email.com")
            .cpf("11122233344")
            .build();

    private static final UserToken USER_TOKEN = UserToken.builder()
            .id("abc")
            .dateOfCreation(LocalDateTime.now())
            .firstAccessToken("123456")
            .build();

    private static final String ID = "id";
    private static final String FIRST_NAME = "firstName";
    private static final String EMAIL = "email@email.com";
    private static final String CPF = "00000000000";

    private static final User USER = User.builder()
            .id(ID)
            .firstName(FIRST_NAME)
            .email(EMAIL)
            .cpf(CPF)
            .password("abcdefgh")
            .userToken(USER_TOKEN)
            .build();

    private static final TokenInformationDto TOKEN_INFORMATION_DTO = TokenInformationDto.builder()
            .token("123456")
            .password("abcdefgh")
            .build();


    @Nested
    class CreateUser {

        @Test
        public void shouldReturn() {
            Registration registration = Registration
                    .builder()
                    .firstName(FIRST_NAME)
                    .email(EMAIL)
                    .cpf(CPF)
                    .build();

            User user = userService.createUser(registration);

            assertEquals(registration.getFirstName(), user.getFirstName());
            assertEquals(registration.getEmail(), user.getEmail());
            assertEquals(registration.getCpf(), user.getCpf());
        }
    }

    @Nested
    class ValidateAndSendToken {

        @Test
        public void shouldThrowWhenUserNotFound() {
            when(repository.findOneByCpf(USER_INFORMATION_DTO.getCpf()))
                    .thenReturn(null);

            assertThrows(EntityNotFound.class,
                    () -> userService.validateAndSendToken(USER_INFORMATION_DTO));

            verify(repository).findOneByCpf(USER_INFORMATION_DTO.getCpf());
        }

        @Test
        public void shouldThrowWhenInvalidEmail() {
            USER.setEmail("email2@email.com");

            when(repository.findOneByCpf(USER_INFORMATION_DTO.getCpf()))
                    .thenReturn(USER);

            assertThrows(EntityNotFound.class,
                    () -> userService.validateAndSendToken(USER_INFORMATION_DTO));

            verify(repository).findOneByCpf(USER_INFORMATION_DTO.getCpf());
        }

        @Test
        public void shouldSaveAndSendUserTokenWhenArgumentsAreValid() {
            USER.setEmail("email@email.com");

            when(repository.findOneByCpf(USER_INFORMATION_DTO.getCpf()))
                    .thenReturn(USER);

            when(userTokenService.createOrUpdateUserToken(USER.getUserToken())).thenReturn(USER_TOKEN);

            userService.validateAndSendToken(USER_INFORMATION_DTO);

            verify(repository).findOneByCpf(USER_INFORMATION_DTO.getCpf());
            verify(userTokenService).createOrUpdateUserToken(USER.getUserToken());
            verify(repository).save(USER);
        }
    }

    @Nested
    class ValidateAndCreatePassword {

        @Test
        public void shouldThrowWhenUserNotFound() {
            when(repository.findOneById(ID))
                    .thenReturn(null);

            assertThrows(EntityNotFound.class,
                    () -> userService.validateAndCreatePassword(ID, TOKEN_INFORMATION_DTO));

            verify(repository).findOneById(ID);
        }

        @Test
        public void shouldSaveUserIfArgumentsAreValid() {
            when(repository.findOneById(ID))
                    .thenReturn(USER);

            doNothing().when(userTokenService).validateToken(USER.getUserToken(), TOKEN_INFORMATION_DTO.getToken());

            userService.validateAndCreatePassword(ID, TOKEN_INFORMATION_DTO);

            verify(repository).findOneById(ID);
            verify(userTokenService).validateToken(USER.getUserToken(), TOKEN_INFORMATION_DTO.getToken());
            verify(repository).save(USER);
        }
    }
}