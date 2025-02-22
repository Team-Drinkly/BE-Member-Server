package com.drinkhere.drinklymember.common.exception.profile;

import com.drinkhere.drinklymember.common.exception.BaseErrorCode;
import com.drinkhere.drinklymember.common.response.ApplicationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProfileErrorCode implements BaseErrorCode {

    // 인증 관련 오류
    MEMBER_NOT_FOUND("존재하지 않는 멤버입니다. 다시 요청해주세요.", 1301, HttpStatus.NOT_FOUND),
    MEMBER_SUBSCRIBE_NOT_FOUND("해당 멤버의 현재 구독 정보가 없습니다.", 1302, HttpStatus.NOT_FOUND)
    ;

    private final String message;
    private final int errorCode;
    private final HttpStatus httpStatus;

    @Override
    public ApplicationResponse<String> toResponseEntity() {
        return ApplicationResponse.server(message);
    }
}
