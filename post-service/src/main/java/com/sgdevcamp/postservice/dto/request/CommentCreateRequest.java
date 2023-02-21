package com.sgdevcamp.postservice.dto.request;

import com.sgdevcamp.postservice.model.Comment;
import lombok.Data;

@Data
public class CommentCreateRequest {
    private String username;

    private String content;

    public Comment toEntity(String post_id){
        return Comment.builder()
                .username(username)
                .content(content)
                .postId(post_id)
                .build();
    }
}
