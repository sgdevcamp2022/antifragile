package com.sgdevcamp.postservice.dto.follow.request;

import lombok.Data;

@Data
public class FollowRequest {
    UserDto follower;
    UserDto following;
}