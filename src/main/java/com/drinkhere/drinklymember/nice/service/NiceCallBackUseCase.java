package com.drinkhere.drinklymember.nice.service;

import com.drinkhere.drinklymember.common.annotation.ApplicationService;
import com.drinkhere.drinklymember.common.exception.nice.NiceException;
import com.drinkhere.drinklymember.domain.member.dto.GetNiceApiResultResponse;
import com.drinkhere.drinklymember.domain.member.service.MemberCommandService;
import com.drinkhere.drinklymember.domain.member.service.MemberQueryService;
import com.drinkhere.drinklymember.nice.dto.NiceCryptoData;
import com.drinkhere.drinklymember.nice.dto.NiceDecryptedData;
import com.drinkhere.drinklymember.redis.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import static com.drinkhere.drinklymember.common.exception.nice.NiceErrorCode.*;


@ApplicationService
@RequiredArgsConstructor
public class NiceCallBackUseCase {
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;
    private final MemberQueryService memberQueryService;
    private static final String REDIS_CRYPTO_KEY = "cryptoData";
    private static final String REDIS_REQUEST_NO_KEY_TEMPLATE = "memberId:%d:requestNo";
    private static final String SYMMETRIC_ENCRYPTION_ALGORITHM_AES = "AES";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    public GetNiceApiResultResponse processCallback(Long memberId, String encData)
    {
        // 대칭키 조회
        NiceCryptoData niceCryptoData = getCryptoDataFromRedisAndValidate();

        // encData 복호화
        NiceDecryptedData niceDecryptedData = decryptAndParseData(encData, niceCryptoData.key(), niceCryptoData.iv());
        // Redis에서 requestno 조회 후 복호화 결과의 requestno 비교
        getRequestNoFromRedisAndValidate(memberId, niceDecryptedData.requestNo());
        // 성인 인증 및 DI 값으로 중복 계정 체크
        validateAdult(niceDecryptedData.birthDate());
        checkDuplicateAccountByDI(niceDecryptedData.di());
        String decodedName = decodingName(niceDecryptedData.utf8Name());

        return GetNiceApiResultResponse.from(memberId, niceDecryptedData, decodedName);
    }
    /**-----------------------------------METHOD들------------------------------------------**/
    // Redis에서 대칭키 조회 후 역직렬화
    private NiceCryptoData getCryptoDataFromRedisAndValidate() {
        String cryptoDataJson = (String) redisUtil.get(REDIS_CRYPTO_KEY);

        if (cryptoDataJson == null) {
            throw new NiceException(CRYPTO_DATA_NOT_FOUND);
        }

        try {
            return objectMapper.readValue(cryptoDataJson, NiceCryptoData.class); // Deserialization
        } catch (JsonProcessingException e) {
            throw new NiceException(DESERIALIZATION_FAILED);
        }
    }


    // 복호화 후 객체 반환
    private NiceDecryptedData decryptAndParseData(String encData, String key, String iv) {
        try {
            String decodedEncData = URLDecoder.decode(encData, StandardCharsets.UTF_8); // URL 디코딩
            SecretKey secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), SYMMETRIC_ENCRYPTION_ALGORITHM_AES );
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));

            byte[] decodedData = Base64.getDecoder().decode(decodedEncData);
            byte[] decryptedData = cipher.doFinal(decodedData);

            String decryptedString = new String(decryptedData, StandardCharsets.UTF_8);

            return objectMapper.readValue(decryptedString, NiceDecryptedData.class);// Deserialization
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new NiceException(INVALID_CIPHER_ALGORITHM);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new NiceException(INVALID_CIPHER_PARAMETERS);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new NiceException(CIPHER_DECRYPTION_FAILED);
        } catch (JsonProcessingException e) {
            throw new NiceException(DESERIALIZATION_FAILED);
        }
    }

    // requestno 조회 및 검증
    private String getRequestNoFromRedisAndValidate(Long memberId, String niceDecryptedRequestNo) {
        String requestNoKey = String.format(REDIS_REQUEST_NO_KEY_TEMPLATE, memberId);
        String requestNoFromRedis = (String) redisUtil.get(requestNoKey);
        if (requestNoFromRedis != null) { // 레디스에 해당 요청번호가 존재하는지
            if (!niceDecryptedRequestNo.equals(requestNoFromRedis)) { // 요청 번호가 일치하는지
                throw new NiceException(REQUEST_NO_MISMATCH);
            }
        } else {
            throw new NiceException(REQUEST_NO_NOT_FOUND);
        }

        return requestNoFromRedis;
    }

    private void validateAdult(String birthDate) {
        // 생년월일 파싱 (yyyyMMdd 형식)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate birthDateLocal = LocalDate.parse(birthDate, formatter);

        // 만 19년이 되는 해의 1월 1일
        LocalDate adultStartDate = birthDateLocal.plusYears(19).withDayOfYear(1);

        // 현재 날짜
        LocalDate today = LocalDate.now();

        // 만 19년이 되는 해의 1월 1일이 오늘 날짜 이후라면 예외 발생
        if (today.isBefore(adultStartDate)) {
            throw new NiceException(NOT_AN_ADULT);
        }
    }

    private void checkDuplicateAccountByDI(String di) {
        // MemberCommandService에서 DI 값으로 회원 조회
        boolean isDuplicate = memberQueryService.existsByDi(di);

        if (isDuplicate) {
            throw new NiceException(DUPLICATE_ACCOUNT);
        }
    }

    private String decodingName(String encodedName) {
        try {
            return URLDecoder.decode(encodedName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new NiceException(NAME_DECODING_FAILED);
        }
    }
}
