package com.project.DigitalBank.repositories;

import com.project.DigitalBank.models.ReceivedTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceivedTransferRepository extends JpaRepository<ReceivedTransfer, String> {
    ReceivedTransfer findOneByTransferUniqueCode(String transferUniqueCode);
}
