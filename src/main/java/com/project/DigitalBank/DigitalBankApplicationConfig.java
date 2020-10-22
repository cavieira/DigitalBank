package com.project.DigitalBank;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.security.SecureRandom;
import java.util.Random;

@Configuration
public class DigitalBankApplicationConfig {

    @Bean
    PasswordEncoder providePasswordEncoder() {
        String key = "MY_SECRET_KEY";
        int iterations = 200000;
        int hashWidth = 256;

        return new Pbkdf2PasswordEncoder(key, iterations, hashWidth);
    }

    @Bean
    Random getRandom() {
        return new Random();
    }
}
