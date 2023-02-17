package com.sgdevcamp.postservice.dto.request;

import lombok.Data;

@Data
public class CommentUpdateRequest {

    private String id;

    private String username;

    private String content;

}
