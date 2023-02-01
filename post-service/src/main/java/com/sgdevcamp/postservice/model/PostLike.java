package com.sgdevcamp.postservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class PostLike {
    private String postId;

    @CreatedBy
    private String username;
}
