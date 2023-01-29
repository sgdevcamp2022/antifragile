package com.sgdevcamp.followservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomExceptionStatus {
    USERNAMEALREADYEXISTSEXCEPTION(false, 6001, "이미 존재하는 유저입니다."),
    USERNAMENOTEXISTSEXCEPTION(false, 6002, "존재하지 않는 유저입니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;
}