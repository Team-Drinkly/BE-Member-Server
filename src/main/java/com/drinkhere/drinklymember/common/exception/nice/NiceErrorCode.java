package com.drinkhere.drinklymember.common.exception.nice;

import com.drinkhere.drinklymember.common.exception.BaseErrorCode;
import com.drinkhere.drinklymember.common.response.ApplicationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NiceErrorCode implements BaseErrorCode {

    // 인증 관련 오류
    CRYPTO_TOKEN_REQUEST_FAILED("NICE API 암호화 토큰 요청에 실패했습니다. 다시 시도해주세요.", 1001, HttpStatus.BAD_REQUEST),
    NOT_AN_ADULT("성인 인증에 실패했습니다. 민 19세 미만입니다.", 1002, HttpStatus.FORBIDDEN),
    DUPLICATE_ACCOUNT("이미 가입한 회원입니다. 가입된 계정으로 로그인해주세요.", 1003, HttpStatus.FORBIDDEN),
    NAME_DECODING_FAILED("이름 디코딩에 실패해습니다.", 1004, HttpStatus.INTERNAL_SERVER_ERROR),

    // 암호화/복호화 관련 오류
    HASH_ALGORITHM_NOT_FOUND("요청하신 해시 알고리즘을 찾을 수 없습니다.", 1006, HttpStatus.BAD_REQUEST),

    INVALID_CIPHER_ALGORITHM("지원되지 않는 암호화 알고리즘 또는 패딩 방식입니다.", 1019, HttpStatus.BAD_REQUEST),
    INVALID_CIPHER_PARAMETERS("복호화 키 또는 초기화 벡터가 유효하지 않습니다.", 1018, HttpStatus.BAD_REQUEST),
    CIPHER_DECRYPTION_FAILED("복호화에 실패했습니다. 패딩 오류 또는 블록 크기 오류.", 1016, HttpStatus.BAD_REQUEST),

    CREATE_INTEGRITY_VALUE_FAILED("무결성 체크값 생성에 실패했습니다: HMAC 알고리즘 초기화 실패 또는 키 유효성 오류", 1020, HttpStatus.BAD_REQUEST),

    // Redis 관련 오류
    REDIS_SAVE_FAILED("Redis에 데이터를 저장하는 데 실패했습니다. 다시 시도해주세요.", 1005, HttpStatus.INTERNAL_SERVER_ERROR),

    CRYPTO_DATA_NOT_FOUND("Redis에 CryptoData를 찾을 수 없습니다. 본인 인증을 다시 진행해주세요.", 1002, HttpStatus.NOT_FOUND),
    REQUEST_NO_NOT_FOUND("Redis에 RequestNo를 찾을 수 없습니다. 본인 인증을 다시 진행해주세요.", 1001, HttpStatus.NOT_FOUND),
    REQUEST_NO_MISMATCH("RequestNo가 일치하지 않습니다. 본인 인증을 다시 진행해주세요.", 1002, HttpStatus.BAD_REQUEST),

    // 직렬화 / 역직렬화 처리 관련 오류
    SERIALIZATION_FAILED("직렬화(객체 to 문자열)에 실패했습니다.", 1007, HttpStatus.INTERNAL_SERVER_ERROR),
    DESERIALIZATION_FAILED("역직렬화(문자열 to 객체)에 실패했습니다.", 1006, HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final int errorCode;
    private final HttpStatus httpStatus;

    @Override
    public ApplicationResponse<String> toResponseEntity() {
        return ApplicationResponse.server(message);
    }
}
