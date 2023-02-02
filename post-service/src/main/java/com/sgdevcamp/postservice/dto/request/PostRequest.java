package com.sgdevcamp.postservice.dto.request;

import com.sgdevcamp.postservice.model.Image;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class PostRequest {
    private String username;

    private List<Image> imageUrl;

    private String content;

    private Set<String> hashTags;
}
