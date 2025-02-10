package com.drinkhere.drinklymember.common.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final BaseErrorCode errorCode;

    private ErrorResponse(BaseErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public static ErrorResponse from(CustomException exception) {
        return new ErrorResponse(exception.getErrorCode());
    }
}
