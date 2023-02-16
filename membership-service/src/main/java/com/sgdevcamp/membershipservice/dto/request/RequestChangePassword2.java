package com.sgdevcamp.membershipservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestChangePassword2 {
    String username;
    String password;
}
