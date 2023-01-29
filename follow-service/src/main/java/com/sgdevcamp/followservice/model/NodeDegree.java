package com.sgdevcamp.followservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NodeDegree {
    long outDegree;
    long inDegree;
}
