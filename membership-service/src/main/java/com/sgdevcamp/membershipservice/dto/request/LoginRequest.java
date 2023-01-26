package com.sgdevcamp.membershipservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @Email
    private String email;

    @Length(min=5, max=12)
    private String username;

    @NotBlank
    @Length(min=8, max= 15)
    private String password;

}
