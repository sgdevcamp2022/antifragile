package com.sgdevcamp.postservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomExceptionStatus {
    // common
    SUCCESS(true, 1000, "요청에 성공하였습니다."),
    NOT_ALLOWED_USER(false, 1001, "허용된 작성자가 아닙니다."),

    // post-service
    NOT_FOUND_POST(false, 1100, "요청한 게시글을 찾을 수 없습니다."),
    NOT_FOUND_COMMENT(false, 1101, "요청한 댓글을 찾을 수 없습니다."),
    NOT_FOUND_USER(false, 1102, "유저를 찾을 수 없습니다."),
    NOT_FOUND_HASHTAG(false, 1103, "해시태그가 없습니다."),
    NOT_FOUND_POSTLIKE(false, 1104, "포스트에 좋아요를 누른 적이 없습니다."),

    ALREADY_EXIST_USER(false, 1200, "이미 존재하는 유저입니다."),

    FILE_CONVERT_FAIL(false, 1300, "변환할 수 없는 파일입니다."),

    // follow-service
    USERNAME_ALREADY_EXIST(false, 6001, "이미 존재하는 유저입니다."),
    USERNAME_NOT_EXIST(false, 6002, "존재하지 않는 유저입니다."),

    // feed-service
    UNABLE_TO_GET_USER(false, 7001, "유저 정보를 가져올 수 없습니다."),
    UNABLE_TO_GET_FOLLOWERS(false, 7002, "유저의 팔로워 정보를 가져올 수 없습니다."),
    UNABLE_TO_GET_POST(false, 7003, "포스트를 가져올 수 없습니다."),
    UNABLE_TO_GET_FEED(false, 7004, "피드를 가져올 수 없습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;
}
