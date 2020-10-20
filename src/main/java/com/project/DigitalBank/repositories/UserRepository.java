package com.project.DigitalBank.repositories;

import com.project.DigitalBank.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findOneByCpf(String cpf);
}
