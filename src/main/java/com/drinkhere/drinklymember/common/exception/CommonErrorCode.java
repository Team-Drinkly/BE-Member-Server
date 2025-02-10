package com.drinkhere.drinklymember.common.exception;

import com.drinkhere.drinklymember.common.response.ApplicationResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 공통 예외 처리
 */
@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements BaseErrorCode {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "입력값에 대한 검증에 실패했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public ApplicationResponse<String> toResponseEntity() {
        return ApplicationResponse.server(message);
    }
}
