package org.sopt.common.exception.customError;

import org.sopt.common.exception.ErrorCode;

public class PostNotFoundException extends RuntimeException{
    private final ErrorCode errorCode;

    public PostNotFoundException(Long id){
        super("게시글을 찾을 수 없습니다. id:" + id);
        this.errorCode = ErrorCode.POST_NOT_FOUND;
    }

    public ErrorCode getErrorCode(){
        return errorCode;
    }
}
