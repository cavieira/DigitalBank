package com.project.DigitalBank.services;

import com.project.DigitalBank.dtos.AccountInformationDto;
import com.project.DigitalBank.dtos.RegistrationInformationDto;

public interface EmailService {

    void sendAccountCreated(AccountInformationDto accountInformationDto);

    void sendProposalRetry(RegistrationInformationDto registrationInformationDto);
}
