package com.sgdevcamp.postservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HashtagResponse {

    private String id;

    private String tag;

    private String postId;

}
