package org.sopt.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    POST_NOT_FOUND("POST_001", "게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_TITLE("POST_002", "제목은 필수입니다.", HttpStatus.BAD_REQUEST),
    INVALID_CONTENT("POST_003", "내용은 필수입니다.", HttpStatus.BAD_REQUEST),

    INTERNAL_SERVER_ERROR("COMMON_001", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT("COMMON_002", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),

    USER_NOT_FOUND("USER_001", "유저가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_EMAIL("USER_002", "이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT),

    ALREADY_LIKED("LIKE_001", "이미 좋아요를 누른 게시글입니다.", HttpStatus.BAD_REQUEST),
    LIKE_NOT_FOUND("LIKE_002", "좋아요를 누르지 않은 게시글입니다.", HttpStatus.NOT_FOUND),

    INVALID_PASSWORD("AUTH_001", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_NOT_FOUND("AUTH_002", "리프레시 토큰을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("AUTH_003", "만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("AUTH_004", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("AUTH_005", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("AUTH_006", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(String code, String message, HttpStatus status){
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }


}
