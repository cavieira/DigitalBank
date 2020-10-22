package com.project.DigitalBank.repositories;

import com.project.DigitalBank.models.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepository extends JpaRepository<UserToken, String> {
}
