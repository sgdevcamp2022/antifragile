package com.sgdevcamp.followservice.dto.request;

import lombok.Data;

@Data
public class UserDto {
    private String id;
    private String username;
    private String name;
    private String profilePicture;
}
