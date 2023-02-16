package com.sgdevcamp.membershipservice.payload;

import com.sgdevcamp.membershipservice.messaging.UserEventType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEventPayload {
    private String id;
    private String username;
    private String email;
    private String displayName;
    private String profilePictureUrl;
    private UserEventType eventType;
}
