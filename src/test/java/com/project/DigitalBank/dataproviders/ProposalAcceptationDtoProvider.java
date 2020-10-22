package com.project.DigitalBank.dataproviders;

import com.project.DigitalBank.dtos.ProposalAcceptationDto;

public class ProposalAcceptationDtoProvider {

    private static ProposalAcceptationDto provide(boolean accept) {
        return ProposalAcceptationDto
                .builder()
                .id("id")
                .accept(accept)
                .build();
    }

    public static ProposalAcceptationDto provideAccepted() {
        return provide(true);
    }
    public static ProposalAcceptationDto provideRejected() {
        return provide(false);
    }
}
