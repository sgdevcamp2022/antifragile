package com.sgdevcamp.postservice.dto.request;

import lombok.Data;

@Data
public class CommentCreateRequest {
    private String username;

    private String content;
}
