package com.project.DigitalBank.queues;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationQueue {

    public static final String ACCEPTED = "registration-accepted";
    public static final String REJECTED = "registration-rejected";

    @Bean
    public Queue acceptedQueue() {
        return new Queue(ACCEPTED, false);
    }

    @Bean
    public Queue rejectedQueue() {
        return new Queue(REJECTED, false);
    }
}
