package com.project.DigitalBank.services;

import com.project.DigitalBank.dtos.AccountInformationDto;
import com.project.DigitalBank.dtos.RegistrationInformationDto;
import com.project.DigitalBank.dtos.UserTokenDto;

import java.util.logging.Logger;

public class FakeEmailService implements EmailService {
    Logger logger = Logger.getLogger(FakeEmailService.class.getName());

    @Override
    public void sendAccountCreated(AccountInformationDto accountInformationDto) {
        logger.info(String.valueOf(accountInformationDto));
    }

    @Override
    public void sendProposalRetry(RegistrationInformationDto registrationInformationDto) {
        logger.info(String.valueOf(registrationInformationDto));
    }

    @Override
    public void sendUserToken(UserTokenDto userTokenDto) {
        logger.info(String.valueOf(userTokenDto));
    }
}
