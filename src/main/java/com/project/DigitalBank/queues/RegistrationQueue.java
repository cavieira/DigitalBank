package com.project.DigitalBank.queues;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationQueue {

    public static final String NAME = "registration";

    @Bean
    public Queue queue() {
        return new Queue(NAME, false);
    }
}
