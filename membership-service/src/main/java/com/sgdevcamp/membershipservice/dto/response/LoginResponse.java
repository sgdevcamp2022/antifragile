package com.sgdevcamp.membershipservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private Long id;

    private String username;

    private String email;

    private String accessToken;

    private String refreshToken;
}
