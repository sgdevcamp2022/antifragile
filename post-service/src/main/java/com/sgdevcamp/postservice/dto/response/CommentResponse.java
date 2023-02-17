package com.sgdevcamp.postservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Builder
@Getter
@Setter
public class CommentResponse {
    private String id;

    private String username;

    private String content;

    private Instant createdAt;
}
