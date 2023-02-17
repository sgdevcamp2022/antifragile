package com.sgdevcamp.postservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfileResponse {

    String username;

    int postCnt;

    String profileName;

    String imageUrl;

    Long followerCnt;

    Long followingCnt;
}
