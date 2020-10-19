package com.project.DigitalBank.services;

import com.project.DigitalBank.dtos.AccountInformationDto;
import com.project.DigitalBank.dtos.RegistrationInformationDto;

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
}
