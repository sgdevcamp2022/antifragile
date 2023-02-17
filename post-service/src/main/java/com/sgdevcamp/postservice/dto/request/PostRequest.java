package com.sgdevcamp.postservice.dto.request;

import com.sgdevcamp.postservice.model.Image;
import com.sgdevcamp.postservice.model.Post;
import lombok.Data;

import java.util.List;

@Data
public class PostRequest {
    private String username;

    private List<Image> images;

    private String content;

    private List<String> hashTags;

    public Post toEntity() {
        return Post.builder()
                .username(username)
                .images(images)
                .content(content)
                .hashTags(hashTags)
                .build();
    }
}
