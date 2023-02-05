package com.sgdevcamp.followservice.payload;

import com.sgdevcamp.followservice.messaging.UserEventType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEventPayload {
    private String id;
    private String username;
    private String email;
    private String profilePictureUrl;
    private UserEventType eventType;
}