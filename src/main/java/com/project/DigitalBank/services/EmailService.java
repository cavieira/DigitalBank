package com.project.DigitalBank.services;

import com.project.DigitalBank.dtos.AccountInformationDto;
import com.project.DigitalBank.dtos.RegistrationInformationDto;
import com.project.DigitalBank.dtos.UserTokenDto;

public interface EmailService {

    void sendAccountCreated(AccountInformationDto accountInformationDto);

    void sendProposalRetry(RegistrationInformationDto registrationInformationDto);

    void sendUserToken(UserTokenDto userTokenDto);
}
