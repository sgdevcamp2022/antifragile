package com.sgdevcamp.postservice.dto.feed;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SlicedResult<T> {

    private String pagingStats;
    private boolean isLast;
    private List<T> content;
}
