package com.project.hirescopebackend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "E001", "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E002", "서버 내부 오류입니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "U002", "이미 사용 중인 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "U003", "비밀번호가 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "U004", "로그인이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "U005", "접근 권한이 없습니다."),

    // Resume
    RESUME_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "이력서를 찾을 수 없습니다."),

    // Job
    JOB_NOT_FOUND(HttpStatus.NOT_FOUND, "J001", "채용공고를 찾을 수 없습니다."),
    INVALID_WEIGHT(HttpStatus.BAD_REQUEST, "J002", "가중치 합계가 100이 되어야 합니다."),

    // Application
    APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "지원 정보를 찾을 수 없습니다."),
    DUPLICATE_APPLICATION(HttpStatus.CONFLICT, "A002", "이미 지원한 공고입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
