package com.example.feedservice.payload;

import com.example.feedservice.messaging.PostEventType;
import com.example.feedservice.model.Image;
import lombok.Builder;
import lombok.Data;


import java.time.Instant;
import java.util.List;

@Data
@Builder
public class PostEventPayload {
    private String id;
    private String username;
    private List<Image> images;
    private String content;
    private PostEventType eventType;
    private Instant createdAt;
    private Instant updatedAt;
}
