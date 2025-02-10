package com.drinkhere.drinklymember.common.exception;

import com.drinkhere.drinklymember.common.response.ApplicationResponse;
import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

    HttpStatus getHttpStatus();

    String getMessage();

    ApplicationResponse<String> toResponseEntity();
}