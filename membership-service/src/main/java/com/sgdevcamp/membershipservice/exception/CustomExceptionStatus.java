package com.sgdevcamp.membershipservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomExceptionStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2100 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2100, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2101, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2102, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2103,"권한이 없는 유저의 접근입니다."),
    NOT_AUTHENTICATED_ACCOUNT(false, 2104, "로그인이 필요합니다."),
    NOT_USER_ROLE_ACCOUNT(false, 2105, "가입 후 최초 한 번의 이메일 인증이 필요합니다."),
    INVALID_KEY(false, 2106, "유효하지 않는 key 값입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2110, "유저 아이디 값을 확인해주세요."),
    ACCOUNT_NOT_FOUND(false, 2111, "사용자를 찾을 수 없습니다."),
    ACCOUNT_NOT_VALID(false, 2112, "유효한 사용자가 아닙니다."),
    ACCOUNT_NOT_VALID_ROLE(false, 2113, "유효한 Role 형식이 아닙니다."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2120, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2121, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2122,"중복된 이메일입니다."),
    POST_USERS_NOT_EQUAL_EMAIL(false, 2123, "가입된 이메일이 아닙니다."),
    POST_USERS_EMPTY_NAME(false, 2124, "네임을 입력해주세요."),
    POST_USERS_INVALID_NAME(false, 2125, "네임 형식을 확인해주세요."),
    POST_USERS_EMPTY_USERNAME(false, 2126, "아이디를 입력해주세요."),
    POST_USERS_EXISTS_USERNAME(false,2127,"중복된 아이디입니다."),
    POST_USERS_INVALID_USERNAME(false,2128,"아이디 형식을 확인해주세요."),
    POST_USERS_INVALID_PASSWORD(false, 2129, "비밀번호 형식을 확인해주세요."),
    POST_USERS_EMPTY_PASSWORD(false, 2130, "비밀번호를 입력해주세요"),
    POST_USERS_WRONG_PASSWORD(false, 2131, "비밀번호를 다시 입력해주세요"),

    // Role
    ACCOUNT_ACCESS_DENIED(false, 2150, "권한이 없습니다."),

    // friend
    FRIEND_NOT_FOUND(false, 2170, "친구 목록을 찾을 수 없습니다."),

    // email
    NOT_VALID_CODE(false,2180,"유효하지 않은 인증번호입니다."),

    /**
     * 2200 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 2200, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 2210, "중복된 이메일입니다."),
    DUPLICATED_NICKNAME(false, 2211, "중복된 닉네임입니다."),
    DUPLICATED_NICKNAME_SELF(false, 2212, "원래의 닉네임과 중복됩니다."),
    FAILED_TO_LOGIN(false,2213,"없는 아이디이거나 비밀번호가 틀렸습니다."),
    ALREADY_CERTIFICATION_ACCOUNT(false,2214,"이미 인증된 유저입니다."),
    FAILED_TO_CERTIFICATION(false,2215,"유효한 토큰 값이 아닙니다."),
    FAILED_TO_RECEPTION(false,2216,"유효한 수신 번호가 아닙니다."),
    DUPLICATED_USERNAME(false, 2217, "중복된 아이디입니다."),

    /**
     * 2300 : Database, Server 오류
     */
    DATABASE_ERROR(false, 2300, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 2301, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,2310,"유저네임 수정 실패"),

    // File
    FILE_CONVERT_FAIL(false, 1300, "변환할 수 없는 파일입니다.");

    // 5000

    // 6000


    private final boolean isSuccess;
    private final int code;
    private final String message;
}
