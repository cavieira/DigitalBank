package com.project.DigitalBank.controllers;

import com.project.DigitalBank.dtos.TokenInformationDto;
import com.project.DigitalBank.dtos.UserInformationDto;
import com.project.DigitalBank.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.project.DigitalBank.utils.URIUtil.createURI;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/firstaccess/")
    public ResponseEntity<String> firstAccess(
            @RequestBody @Valid @NotNull UserInformationDto userInformationDto) {
        String id = userService.validateAndSendToken(userInformationDto);

        return createURI("/firstaccess/{id}/", id, id);
    }

    @PostMapping("/firstaccess/{id}/")
    public ResponseEntity<String> firstAccessWithToken(
            @PathVariable String id,
            @RequestBody @Valid @NotNull TokenInformationDto tokenInformationDto) {

        userService.validateAndCreatePassword(id, tokenInformationDto);

        return ResponseEntity.ok().build();
    }
}
