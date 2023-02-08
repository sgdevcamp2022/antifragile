package com.example.feedservice.payload;

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
