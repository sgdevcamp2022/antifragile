package com.sgdevcamp.membershipservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    @Length(max= 190)
    private String introduction;
}
