package com.project.DigitalBank.services;

import com.project.DigitalBank.enumerations.UserStatus;
import com.project.DigitalBank.exceptions.EntityNotFound;
import com.project.DigitalBank.exceptions.RegistrationStepUnprocessable;
import com.project.DigitalBank.exceptions.TokenInvalid;
import com.project.DigitalBank.models.UserToken;
import com.project.DigitalBank.repositories.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserTokenService {

    @Value(value = "${tokenExpirationTime}")
    private long tokenExpirationTime; // Expiration time in seconds

    public UserToken createOrUpdateUserToken(UserToken userToken) {
        Random random = new Random();

        if(userToken == null) {
            userToken = new UserToken();
        }

        userToken.setFirstAccessToken(String.format("%06d", random.nextInt(1000000)));
        userToken.setDateOfCreation(LocalDateTime.now());
        userToken.setUserStatus(UserStatus.TOKEN_ENVIADO);

        return userToken;
    }

    public void validateToken(UserToken userToken, String tokenSent) {
        if (userToken == null || !userToken.getUserStatus().equals(UserStatus.TOKEN_ENVIADO) || !userToken.getFirstAccessToken().equals(tokenSent)) {
            throw new TokenInvalid("Token inválido.");
        }

        if (Duration.between(userToken.getDateOfCreation(), LocalDateTime.now()).toSeconds() > tokenExpirationTime) {
            throw new TokenInvalid("Token espirado.");
        }

        userToken.setUserStatus(UserStatus.ATIVO);

        //return userToken;
    }
}
