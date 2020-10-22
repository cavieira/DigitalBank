package com.project.DigitalBank.queues;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReceivedTransferQueue {

    public static final String RECEIVED_TRANSFER = "received-transfer";

    @Bean
    public Queue receiveTransferQueue() {
        return new Queue(RECEIVED_TRANSFER, false);
    }
}
