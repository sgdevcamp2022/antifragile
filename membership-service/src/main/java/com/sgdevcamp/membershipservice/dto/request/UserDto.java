package com.sgdevcamp.membershipservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sgdevcamp.membershipservice.model.Profile;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min=5, max=12)
    @Pattern(regexp = "^[a-zA-Z0-9_-]{6,12}$")
    private String username;

    @NotBlank
    @Length(min=8, max=15)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank
    @Length(min=2, max=15)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9_-]{2,20}$")
    private String name;

    private Profile profile;

    @Length(max=190)
    private String introduction;
}
