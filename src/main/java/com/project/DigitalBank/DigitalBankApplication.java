package com.project.DigitalBank;

import com.project.DigitalBank.services.EmailService;
import com.project.DigitalBank.services.FakeEmailService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DigitalBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankApplication.class, args);
    }

    @Bean
    public EmailService emailService() {
        return new FakeEmailService();
    }

}
