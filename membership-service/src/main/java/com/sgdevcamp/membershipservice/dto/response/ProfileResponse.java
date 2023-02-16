package com.sgdevcamp.membershipservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfileResponse {
    private String username;

    private String name;

    private String introduction;

    private String email;
}
