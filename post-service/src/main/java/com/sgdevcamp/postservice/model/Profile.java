package com.sgdevcamp.postservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class Profile {
    @Id
    private String id;

    private String username;

    private String image;
}
