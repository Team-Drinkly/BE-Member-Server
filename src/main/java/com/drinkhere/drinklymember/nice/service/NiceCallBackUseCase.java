package com.drinkhere.drinklymember.nice.service;

import com.drinkhere.drinklymember.common.annotation.ApplicationService;
import com.drinkhere.drinklymember.common.exception.nice.NiceException;
import com.drinkhere.drinklymember.domain.member.dto.signup.GetNiceApiResultResponse;
import com.drinkhere.drinklymember.domain.member.service.member.MemberQueryService;
import com.drinkhere.drinklymember.domain.member.service.owner.OwnerQueryService;
import com.drinkhere.drinklymember.nice.dto.NiceCryptoData;
import com.drinkhere.drinklymember.nice.dto.NiceDecryptedData;
import com.drinkhere.drinklymember.redis.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
    private final OwnerQueryService ownerQueryService;

    private static final String REDIS_REQUEST_NO_KEY_TEMPLATE = "memberId:%d:type:%s:requestNo";
    private static final String SYMMETRIC_ENCRYPTION_ALGORITHM_AES = "AES";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    public GetNiceApiResultResponse processCallback(Long id, String type, String encData) {

        // 대칭키 조회
        NiceCryptoData niceCryptoData = getCryptoDataFromRedisAndValidate();

        // encData 복호화
        NiceDecryptedData niceDecryptedData = decryptAndParseData(encData, niceCryptoData.key(), niceCryptoData.iv());

        // Redis에서 requestNo 조회 후 비교
        getRequestNoFromRedisAndValidate(id, type, niceDecryptedData.requestNo());

        // 성인 인증 및 DI 값으로 중복 계정 체크
        validateAdult(niceDecryptedData.birthDate());
        checkDuplicateAccountByDI(niceDecryptedData.di(), type);

        // 이름 디코딩 (StandardCharsets.UTF_8 사용)
        String decodedName = decodingName(niceDecryptedData.utf8Name());

        return GetNiceApiResultResponse.from(id, type, niceDecryptedData, decodedName);
    }

    /**
     * -----------------------------------METHOD들------------------------------------------
     **/

    private NiceCryptoData getCryptoDataFromRedisAndValidate() {
        String cryptoDataJson = (String) redisUtil.get("cryptoData");

        if (cryptoDataJson == null) {
            throw new NiceException(CRYPTO_DATA_NOT_FOUND);
        }

        try {
            return objectMapper.readValue(cryptoDataJson, NiceCryptoData.class); // Deserialization
        } catch (JsonProcessingException e) {
            throw new NiceException(DESERIALIZATION_FAILED);
        }
    }

    private NiceDecryptedData decryptAndParseData(String encData, String key, String iv) {
        try {
            String decodedEncData = URLDecoder.decode(encData, StandardCharsets.UTF_8);
            SecretKey secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), SYMMETRIC_ENCRYPTION_ALGORITHM_AES);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));

            byte[] decodedData = Base64.getDecoder().decode(decodedEncData);
            byte[] decryptedData = cipher.doFinal(decodedData);

            return objectMapper.readValue(new String(decryptedData, StandardCharsets.UTF_8), NiceDecryptedData.class);
        } catch (Exception e) {
            throw new NiceException(CIPHER_DECRYPTION_FAILED);
        }
    }

    private void getRequestNoFromRedisAndValidate(Long id, String type, String niceDecryptedRequestNo) {
        // 🔹 수정된 Redis Key 포맷
        String requestNoKey = String.format(REDIS_REQUEST_NO_KEY_TEMPLATE, id, type);
        Object redisData = redisUtil.get(requestNoKey);

        if (redisData == null) {
            throw new NiceException(REQUEST_NO_NOT_FOUND);
        }

        String requestNoFromRedis = redisData.toString();

        if (!niceDecryptedRequestNo.equals(requestNoFromRedis)) {
            throw new NiceException(REQUEST_NO_MISMATCH);
        }
    }

    private void validateAdult(String birthDate) {
        LocalDate birthDateLocal = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate adultStartDate = birthDateLocal.plusYears(19).withDayOfYear(1);
        LocalDate today = LocalDate.now();

        if (today.isBefore(adultStartDate)) {
            throw new NiceException(NOT_AN_ADULT);
        }
    }

    private void checkDuplicateAccountByDI(String di, String type) {
        boolean isDuplicate = "owner".equalsIgnoreCase(type) ? ownerQueryService.existsByDi(di) : memberQueryService.existsByDi(di);

        if (isDuplicate) {
            throw new NiceException(DUPLICATE_ACCOUNT);
        }
    }

    private String decodingName(String encodedName) {
        try {
            return URLDecoder.decode(encodedName, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new NiceException(NAME_DECODING_FAILED);
        }
    }
}
