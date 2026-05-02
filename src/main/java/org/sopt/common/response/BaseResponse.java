package org.sopt.common.response;

import org.sopt.common.exception.ErrorCode;

public record BaseResponse<T>(
        boolean success,
        String code,
        String message,
        T data
) {
    // 성공
    public static <T> BaseResponse<T> success(SuccessCode successCode, T data){
        return new BaseResponse<>(
                true,
                successCode.getCode(),
                successCode.getMessage(),
                data
        );
    }

    // 실패
    public static <T> BaseResponse<T> error(ErrorCode errorCode){
        return new BaseResponse<>(
                false,
                errorCode.getCode(),
                errorCode.getMessage(),
                null);
    }
}
