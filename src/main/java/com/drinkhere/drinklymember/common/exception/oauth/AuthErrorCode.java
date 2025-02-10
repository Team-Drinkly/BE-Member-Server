package com.drinkhere.drinklymember.common.exception.oauth;

import com.drinkhere.drinklymember.common.exception.BaseErrorCode;
import com.drinkhere.drinklymember.common.response.ApplicationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    INVALID_TOKEN("유효하지 않은 토큰입니다.", 1000, HttpStatus.BAD_REQUEST),
    EXPIRED_TOKEN("만료된 토큰입니다.", 1001, HttpStatus.BAD_REQUEST),
    NOT_EXIST_TOKEN("토큰이 존재하지 않습니다.", 1002, HttpStatus.BAD_REQUEST),
    INVALID_AUTHORIZATION_TYPE("유효하지 않은 Authorization Type 입니다.", 1003, HttpStatus.BAD_REQUEST),
    EMPTY_AUTHORIZATION_HEADER("Authorization Header가 비어있습니다.", 1004, HttpStatus.BAD_REQUEST),
    OAUTH_FAIL("OAuth 인증에 실패하였습니다.", 1005, HttpStatus.BAD_REQUEST),
    OAUTH_NOT_FOUND("OAuth 정보를 찾을 수 없습니다.", 1006, HttpStatus.NOT_FOUND),
    INVALID_OAUTH_REQUEST("다른 소셜로그인으로 가입된 계정이 존재합니다.", 2002, HttpStatus.BAD_REQUEST);

    private final String message;
    private final int errorCode;
    private final HttpStatus httpStatus;

    @Override
    public ApplicationResponse<String> toResponseEntity() {
        return ApplicationResponse.server(message);
    }

}
