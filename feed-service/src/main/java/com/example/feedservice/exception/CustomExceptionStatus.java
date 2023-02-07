package com.example.feedservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomExceptionStatus {
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    REQUEST_ERROR(false, 2100, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2101, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2102, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2103,"권한이 없는 유저의 접근입니다."),
    NOT_AUTHENTICATED_ACCOUNT(false, 2104, "로그인이 필요합니다."),
    NOT_USER_ROLE_ACCOUNT(false, 2105, "가입 후 최초 한 번의 이메일 인증이 필요합니다."),
    INVALID_KEY(false, 2106, "유효하지 않는 key 값입니다."),

    UNABLE_TO_GET_USER(false, 7001, "유저 정보를 가져올 수 없습니다."),
    UNABLE_TO_GET_FOLLOWERS(false, 7002, "유저의 팔로워 정보를 가져올 수 없습니다."),
    UNABLE_TO_GET_POST(false, 7003, "포스트를 가져올 수 없습니다."),
    UNABLE_TO_GET_FEED(false, 7004, "피드를 가져올 수 없습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

}
