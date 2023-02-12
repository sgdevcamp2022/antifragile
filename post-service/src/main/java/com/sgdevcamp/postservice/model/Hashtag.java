package com.sgdevcamp.postservice.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document
public class Hashtag {
    @Id
    private String id;

    private String tag;

    @DocumentReference
    private List<Post> posts;

    @CreatedDate
    private Instant createdAt;
}
