package com.sgdevcamp.postservice.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Document
public class Post {
    @Id
    private String id;

    @CreatedBy
    private String username;

    @NonNull
    private List<Image> images;

    @NonNull
    private String content;

    private String profile;

    private int commentCount;

    private Long likeCount;

    private Set<String> hashTags;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
