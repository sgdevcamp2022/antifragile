package com.sgdevcamp.postservice.model;

import com.sgdevcamp.postservice.dto.response.CommentResponse;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Document
@Data
@Builder
public class Comment {
    @Id
    private String id;

    @CreatedBy
    private String username;

    @NotBlank
    private String content;

    @NotNull
    private String postId;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public CommentResponse toResponse(){
        return CommentResponse.builder()
                .id(id)
                .username(username)
                .content(content)
                .createdAt(createdAt)
                .build();
    }
}
