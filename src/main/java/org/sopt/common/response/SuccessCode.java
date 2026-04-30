package org.sopt.common.response;

import org.springframework.http.HttpStatus;

public enum SuccessCode
{
    POST_CREATE_SUCCESS("POST_201", "게시글 생성 성공", HttpStatus.CREATED),
    POST_GET_SUCCESS("POST_200", "게시글 조회 성공", HttpStatus.OK),
    POST_LIST_SUCCESS("POST_200", "게시글 목록 조회 성공", HttpStatus.OK),
    POST_UPDATE_SUCCESS("POST_200", "게시글 수정 성공", HttpStatus.OK),
    POST_DELETE_SUCCESS("POST_200", "게시글 삭제 성공", HttpStatus.OK);

    private final String code;
    private final String message;
    private final HttpStatus status;

    SuccessCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public HttpStatus getStatus() { return status; }
}
