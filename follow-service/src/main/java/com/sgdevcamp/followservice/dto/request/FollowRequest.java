package com.sgdevcamp.followservice.dto.request;

import lombok.Data;

@Data
public class FollowRequest {
    UserDto follower;
    UserDto following;
}
