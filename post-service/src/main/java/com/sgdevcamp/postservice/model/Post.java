package com.sgdevcamp.postservice.model;

import com.sgdevcamp.postservice.dto.request.PostUpdateRequest;
import com.sgdevcamp.postservice.dto.response.PostResponse;
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
import java.util.stream.Collectors;

@Data
@Builder
@Document
public class Post {
    @Id
    private String id;

    private String userId;

    @CreatedBy
    private String username;

    @NonNull
    private List<Image> images;

    @NonNull
    private String content;

    private List<String> hashTags;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public PostResponse toResponse() {
        return PostResponse.builder()
                .id(id)
                .username(username)
                .imageUrl(images.stream().map(i -> i.getPath()).collect(Collectors.toList()))
                .content(content)
                .hashTags(hashTags)
                .createdAt(createdAt)
                .build();
    }

    public void update(PostUpdateRequest postUpdateRequest){
        this.content = postUpdateRequest.getContent();
        this.hashTags = postUpdateRequest.getHashTags();
        this.updatedAt = Instant.now();
    }
}
