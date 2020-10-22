package com.project.DigitalBank.services;

import com.project.DigitalBank.dtos.TokenInformationDto;
import com.project.DigitalBank.dtos.UserInformationDto;
import com.project.DigitalBank.dtos.UserTokenDto;
import com.project.DigitalBank.exceptions.EntityNotFound;
import com.project.DigitalBank.models.Registration;
import com.project.DigitalBank.models.User;
import com.project.DigitalBank.models.UserToken;
import com.project.DigitalBank.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final EmailService emailService;

    private final UserTokenService userTokenService;

    private final PasswordEncoder passwordEncoder;

    public User createUser(Registration registration) {
        return User.builder()
                .firstName(registration.getFirstName())
                .cpf(registration.getCpf())
                .email(registration.getEmail())
                .build();
    }

    public String validateAndSendToken(UserInformationDto userInformationDto) {
        User user = repository.findOneByCpf(userInformationDto.getCpf());

        if (user == null) {
            throw new EntityNotFound("Usuário não encontrado.");
        }

        if (!user.getEmail().equals(userInformationDto.getEmail())) {
            throw new EntityNotFound("Email ou CPF inválido.");
        }

        UserToken userToken = userTokenService.createOrUpdateUserToken(user.getUserToken());

        userToken.setUser(user);
        user.setUserToken(userToken);

        repository.save(user);

        emailService.sendUserToken(UserTokenDto.builder()
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .firstAccessToken(user.getUserToken().getFirstAccessToken())
                .build());

        return user.getId();
    }

    public void validateAndCreatePassword(String id, TokenInformationDto tokenInformationDto) {
        User user = repository.findOneById(id);

        if (user == null) {
            throw new EntityNotFound("Usuário não encontrado.");
        }

        userTokenService.validateToken(user.getUserToken(), tokenInformationDto.getToken());

        user.setPassword(passwordEncoder.encode(tokenInformationDto.getPassword()));

        repository.save(user);
    }
}
