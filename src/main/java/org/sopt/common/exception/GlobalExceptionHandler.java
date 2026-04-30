package org.sopt.common.exception;

import org.sopt.common.exception.customError.InvalidInputException;
import org.sopt.common.exception.customError.PostNotFoundException;
import org.sopt.common.exception.customError.UserNotFoundException;
import org.sopt.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // PostNotFoundException -> 404
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePostNotFound(PostNotFoundException e){
        return ResponseEntity
                .status(ErrorCode.POST_NOT_FOUND.getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }

    // UserNotFoundException -> 404
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFound(UserNotFoundException e){
        return ResponseEntity
                .status(ErrorCode.USER_NOT_FOUND.getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }

    // 유효성 검증 실패 -> 400
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(InvalidInputException e){
        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT.getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }

    // 예상치 못한 모든 예외 -> 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e){
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
