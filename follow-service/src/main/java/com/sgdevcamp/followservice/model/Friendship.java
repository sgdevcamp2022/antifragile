package com.sgdevcamp.followservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

@Builder
@Data
@RelationshipProperties
public class Friendship {
    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private User user;
}
