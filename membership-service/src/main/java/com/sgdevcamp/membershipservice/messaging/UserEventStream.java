package com.sgdevcamp.membershipservice.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface UserEventStream {
    String OUTPUT = "momentsUserChanged";

    @Output
    MessageChannel momentsUserChanged();
}
