package com.sgdevcamp.postservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class PostResponse {
    private String id;

    private String username;

    private String userProfilePic;

    private List<String> imageUrl;

    private String content;

    private int commentCount;

    private Long likeCount;

    private List<String> hashTags;

    private Instant createdAt;

    private Instant updatedAt;
}
