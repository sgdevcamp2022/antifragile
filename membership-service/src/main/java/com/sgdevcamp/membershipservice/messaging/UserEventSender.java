package com.sgdevcamp.membershipservice.messaging;

import com.sgdevcamp.membershipservice.model.User;
import com.sgdevcamp.membershipservice.payload.UserEventPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserEventSender {
    private UserEventStream channels;

    public UserEventSender(UserEventStream channels) {
        this.channels = channels;
    }

    public void sendUserCreated(User user) {
        log.info("sending user created event for user {}", user.getUsername());

        sendUserChangedEvent(convertTo(user, UserEventType.CREATED));
    }

    public void sendUserUpdated(User user) {
        log.info("sending user updated event for user {}", user.getUsername());
        sendUserChangedEvent(convertTo(user, UserEventType.UPDATED));
    }

    public void sendUserUpdated(User user, String oldPicUrl) {
        log.info("sending user updated (profile pic changed) event for user {}",
                user.getUsername());

        UserEventPayload payload = convertTo(user, UserEventType.UPDATED);

        sendUserChangedEvent(payload);
    }

    public void sendUserDeleted(User user){
        log.info("sending user deleted event for user {}", user.getUsername());

        sendUserChangedEvent(convertTo(user, UserEventType.DELETED));
    }

    private void sendUserChangedEvent(UserEventPayload payload) {

        Message<UserEventPayload> message =
                MessageBuilder
                        .withPayload(payload)
                        .setHeader(KafkaHeaders.MESSAGE_KEY, payload.getId())
                        .build();

        channels.momentsUserChanged().send(message);

        log.info("user event {} sent to topic {} for user {}",
                message.getPayload().getEventType().name(),
                channels.OUTPUT,
                message.getPayload().getUsername());
    }

    private UserEventPayload convertTo(User user, UserEventType eventType) {
        return UserEventPayload
                .builder()
                .eventType(eventType)
                .id(user.getId().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .profilePictureUrl(user.getProfile().getPath()).build();
    }
}
