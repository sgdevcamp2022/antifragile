package com.example.feedservice.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Post {
    private String id;

    private String username;

    private String userProfilePic;

    private List<String> imageUrl;

    private String content;

    private int commentCount;

    private Long likeCount;

    private Set<String> hashTags;

    private Instant createdAt;

    private Instant updatedAt;
}
