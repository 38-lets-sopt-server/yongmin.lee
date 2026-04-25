package org.sopt.common.exception.customError;

import org.sopt.common.exception.ErrorCode;

public class InvalidInputException extends RuntimeException {
    private final ErrorCode errorCode;

    public InvalidInputException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode(){
        return errorCode;
    }
}
