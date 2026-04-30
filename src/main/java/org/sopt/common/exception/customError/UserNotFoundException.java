package org.sopt.common.exception.customError;

import org.sopt.common.exception.ErrorCode;

public class UserNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public UserNotFoundException(Long id){
        super(ErrorCode.USER_NOT_FOUND.getMessage() + " id: " + id);
        this.errorCode = ErrorCode.USER_NOT_FOUND;
    }

    public ErrorCode getErrorCode(){
        return errorCode;
    }
}
