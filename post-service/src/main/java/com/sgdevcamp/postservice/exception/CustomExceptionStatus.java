package com.sgdevcamp.postservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomExceptionStatus {
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    NOT_ALLOWED_USER(false, 1001, "허용된 작성자가 아닙니다."),

    NOT_FOUND_POST(false, 1100, "요청한 게시글을 찾을 수 없습니다."),
    NOT_FOUND_COMMENT(false, 1101, "요청한 댓글을 찾을 수 없습니다."),
    NOT_FOUND_USER(false, 1102, "유저를 찾을 수 없습니다."),
    NOT_FOUND_HASHTAG(false, 1103, "해시태그가 없습니다."),
    NOT_FOUND_POSTLIKE(false, 1104, "포스트에 좋아요를 누른 적이 없습니다."),

    ALREADY_EXIST_USER(false, 1200, "이미 존재하는 유저입니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;
}
