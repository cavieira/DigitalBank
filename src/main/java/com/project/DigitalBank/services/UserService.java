package com.project.DigitalBank.services;

import com.project.DigitalBank.dtos.UserInformationDto;
import com.project.DigitalBank.dtos.UserTokenDto;
import com.project.DigitalBank.enumerations.UserStatus;
import com.project.DigitalBank.exceptions.EntityNotFound;
import com.project.DigitalBank.models.Registration;
import com.project.DigitalBank.models.User;
import com.project.DigitalBank.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final EmailService emailService;

    public User createUser(Registration registration) {
        return User.builder()
                .firstName(registration.getFirstName())
                .cpf(registration.getCpf())
                .email(registration.getEmail())
                .userStatus(UserStatus.INATIVO)
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

        Random random = new Random();

        user.setFirstAccessToken(String.format("%06d", random.nextInt(1000000)));
        user.setUserStatus(UserStatus.TOKEN_ENVIADO);

        repository.save(user);

        emailService.sendUserToken(UserTokenDto.builder()
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .firstAccessToken(user.getFirstAccessToken())
                .build());

        return user.getId();
    }
}
