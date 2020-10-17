package com.project.DigitalBank.repositories;

import com.project.DigitalBank.models.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, String> {

    Registration findOneByCpf(String cpf);

    Registration findOneByEmail(String email);

}
