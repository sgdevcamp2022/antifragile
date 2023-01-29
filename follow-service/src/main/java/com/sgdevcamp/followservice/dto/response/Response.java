package com.sgdevcamp.followservice.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Response<T> extends CommonResponse {
    private T result;
}
