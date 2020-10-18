package com.project.DigitalBank.repositories;

import com.project.DigitalBank.models.RegistrationDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationDocumentRepository extends JpaRepository<RegistrationDocument, String> {
}
