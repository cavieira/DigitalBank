package com.project.DigitalBank.dataproviders;

import com.project.DigitalBank.dtos.ReceivedTransferInformationDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReceivedTransferInformationDtoProvider {

    public static ReceivedTransferInformationDto provide() {
        return ReceivedTransferInformationDto
                .builder()
                .date(LocalDateTime.now())
                .destinationBranchNumber("1234")
                .destinationAccountNumber("12345678")
                .destinationCPF("00011122233")
                .sourceBankCode("123")
                .sourceBranchNumber("1234")
                .sourceAccountNumber("12345678")
                .sourceCPF("11122233344")
                .transferUniqueCode("id")
                .value(BigDecimal.valueOf(10.0))
                .build();
    }
}
