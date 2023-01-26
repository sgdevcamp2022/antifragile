package com.sgdevcamp.membershipservice.dto.response;

import com.sgdevcamp.membershipservice.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NameAndPhotoResponse {
    private String name;
    private Profile profile;
    private String instruction;
}
