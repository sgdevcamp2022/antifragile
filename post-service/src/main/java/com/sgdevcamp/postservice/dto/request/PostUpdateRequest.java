package com.sgdevcamp.postservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostUpdateRequest {

    private String content;

    private List<String> hashTags;
}
