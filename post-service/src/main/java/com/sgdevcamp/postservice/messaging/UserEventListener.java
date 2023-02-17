package com.sgdevcamp.postservice.messaging;

import com.sgdevcamp.postservice.model.User;
import com.sgdevcamp.postservice.payload.UserEventPayload;
import com.sgdevcamp.postservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final UserService userService;

    @StreamListener(UserEventStream.INPUT)
    public void onMessage(Message<UserEventPayload> message) {

        UserEventType eventType = message.getPayload().getEventType();

        log.info("received message to process user {} eventType {}",
                message.getPayload().getUsername(),
                eventType.name());

        Acknowledgment acknowledgment =
                message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);

        User user = convertTo(message.getPayload());

        switch (eventType) {
            case CREATED:
                userService.addUser(user);
                break;
            case UPDATED:
                userService.updateUser(user);
                break;
            case DELETED:
                userService.deleteUser(user);
                break;
        }

        if(acknowledgment != null) {
            acknowledgment.acknowledge();
        }
    }

    private User convertTo(UserEventPayload payload) {
        return User
                .builder()
                .username(payload.getUsername())
                .profileName(payload.getDisplayName())
                .imageUrl(payload.getProfilePictureUrl())
                .count_post(0)
                .count_follower(0)
                .count_following(0)
                .introduction("")
                .build();
    }
}
