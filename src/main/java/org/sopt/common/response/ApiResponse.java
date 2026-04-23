package org.sopt.common.response;

import org.sopt.common.exception.ErrorCode;

public record ApiResponse<T>(
        boolean success,
        String code,
        String message,
        T data
) {
    // 성공
    public static <T> ApiResponse<T> success(String code, String message, T data){
        return new ApiResponse<>(
                true,
                code,
                message,
                data);
    }

    // 실패
    public static <T> ApiResponse<T> error(ErrorCode errorCode){
        return new ApiResponse<>(
                false,
                errorCode.getCode(),
                errorCode.getMessage(),
                null);
    }
}
