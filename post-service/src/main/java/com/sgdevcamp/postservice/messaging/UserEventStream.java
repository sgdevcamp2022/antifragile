package com.sgdevcamp.postservice.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface UserEventStream {
    String INPUT = "momentsUserChanged";

    @Input(INPUT)
    MessageChannel momentsUserChanged();
}
