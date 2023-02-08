package com.sgdevcamp.followservice.payload;

import com.sgdevcamp.followservice.messaging.UserEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEventPayload {
    private String id;
    private String username;
    private String email;
    private String profilePictureUrl;
    private UserEventType eventType;
}