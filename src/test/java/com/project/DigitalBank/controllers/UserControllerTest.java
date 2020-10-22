package com.project.DigitalBank.controllers;

import com.project.DigitalBank.dtos.TokenInformationDto;
import com.project.DigitalBank.dtos.UserInformationDto;
import com.project.DigitalBank.services.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.project.DigitalBank.utils.URIUtil.createURI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class UserControllerTest extends BaseControllerTest {

    private static final String ID = "id";

    private static final UserInformationDto USER_INFORMATION_DTO = UserInformationDto
            .builder()
            .cpf("00011122233")
            .email("teste@email.com")
            .build();

    private static final TokenInformationDto TOKEN_INFORMATION_DTO = TokenInformationDto
            .builder()
            .password("1aA#1234")
            .token("123456")
            .build();

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Nested
    class FirstAccess {

        @Test
        void shouldCallValidateAndSendToken() throws Exception {
            when(userService.validateAndSendToken(USER_INFORMATION_DTO))
                    .thenReturn(ID);

            ResponseEntity<String> response = userController.firstAccess(USER_INFORMATION_DTO);

            ResponseEntity<String> expected = createURI("/firstaccess/{id}/", ID, ID);

            assertEquals(expected, response);

            verify(userService).validateAndSendToken(USER_INFORMATION_DTO);
        }
    }

    @Nested
    class FirstAccessWithToken {

        @Test
        void shouldCallProcessAndSaveNewTransfer() throws Exception {
            doNothing().when(userService).validateAndCreatePassword(ID, TOKEN_INFORMATION_DTO);

            ResponseEntity<String> response = userController.firstAccessWithToken(ID, TOKEN_INFORMATION_DTO);

            ResponseEntity<String> expected = ResponseEntity.ok().build();

            assertEquals(expected, response);

            verify(userService).validateAndCreatePassword(ID, TOKEN_INFORMATION_DTO);
        }
    }
}