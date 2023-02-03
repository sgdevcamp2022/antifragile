package com.sgdevcamp.membershipservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    @NotBlank
    @Length(min=2, max=15)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9_-]{2,20}$")
    private String name;

    @Length(max= 190)
    private String introduction;
}
