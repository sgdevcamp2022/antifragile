package com.sgdevcamp.postservice.dto.request;

import com.sgdevcamp.postservice.dto.response.PostResponse;
import com.sgdevcamp.postservice.model.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PostUpdateRequest {

    private String content;

    private List<String> hashTags;
}
    public List<PostResponse> postsByUserIdIn(List<String> ids){

        return postRepository.findByUserIdInOrderByCreatedAtDesc(ids)
                .stream()
                .map(Post::toResponse)
                .collect(Collectors.toList());
    }