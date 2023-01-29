package com.sgdevcamp.followservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

@Data
@Builder
@Node
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String userId;

    private String username;

    private String name;

    private String profilePic;

    @Relationship(type = "IS_FOLLOWING")
    private Set<Friendship> friendships;
}
