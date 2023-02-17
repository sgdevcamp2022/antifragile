package com.sgdevcamp.postservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class User {
    @Id
    private String id;

    private String username;

    private String profileName;

    private String imageUrl;

    private int count_post;

    private int count_follower;

    private int count_following;

    private String introduction;
}
