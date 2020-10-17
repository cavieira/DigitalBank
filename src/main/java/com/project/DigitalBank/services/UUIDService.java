package com.project.DigitalBank.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UUIDService {

    String generate() {
        return UUID.randomUUID().toString();
    }
}
