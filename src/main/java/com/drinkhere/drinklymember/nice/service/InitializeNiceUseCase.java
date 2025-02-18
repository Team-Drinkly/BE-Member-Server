package com.drinkhere.drinklymember.nice.service;

import com.drinkhere.drinklymember.common.exception.nice.NiceException;
import com.drinkhere.drinklymember.domain.auth.enums.Authority;
import com.drinkhere.drinklymember.nice.dto.NiceCryptoData;
import com.drinkhere.drinklymember.nice.dto.NiceRequestData;
import com.drinkhere.drinklymember.nice.dto.response.CreateNiceApiRequestDataDto;
import com.drinkhere.drinklymember.nice.dto.response.GetCryptoTokenResponse;
import com.drinkhere.drinklymember.nice.webclient.NiceClient;
import com.drinkhere.drinklymember.nice.webclient.config.NiceProperties;
import com.drinkhere.drinklymember.redis.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

import static com.drinkhere.drinklymember.common.exception.nice.NiceErrorCode.*;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Service
@RequiredArgsConstructor
public class InitializeNiceUseCase {
    private static final String REDIS_CRYPTO_KEY = "cryptoData";
    private static final String REDIS_CRYPTO_TOKEN_KEY = "cryptoToken";
    private static final Long REDIS_CRYPTO_TOKEN_EXPIRATION = 3300L;
    private static final Long REDIS_REQUEST_NO_EXPIRATION = 30L;
    private static final String HASH_ALGORITHM_SHA256 = "SHA-256"; // 단방향 해시 알고리즘
    private static final String SYMMETRIC_ENCRYPTION_ALGORITHM_AES = "AES"; // 대칭키 암호화 알고리즘
    private static final String MAC_ALGORITHM_HMAC_SHA256 = "HmacSHA256";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String RETURN_URL = "/api/v1/member/nice/call-back?id=%d&type=%s";
    private static final String REDIS_REQUEST_NO_KEY_TEMPLATE = "memberId:%d:requestNo";

    private final NiceClient niceClient;
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;
    private final NiceProperties niceProperties;

    public CreateNiceApiRequestDataDto initializeNiceApi(Authority authority, Long id) {

        String key = null;
        String iv = null;
        String hmacKey = null;
        GetCryptoTokenResponse cryptoToken;

        String storedCryptoToken = (String) redisUtil.get(REDIS_CRYPTO_TOKEN_KEY);
        if (storedCryptoToken == null) { // 암호화 토큰 만료로 새로 발급
            String reqDtim = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String reqNo = UUID.randomUUID().toString().substring(0, 30);

            // 암호화 토큰 호출
            cryptoToken = niceClient.requestCryptoToken(reqDtim, reqNo);

            String resultVal = generateResultVal(reqDtim, reqNo, cryptoToken.dataBody().tokenVal());

            // 대칭키 생성
            key = resultVal.substring(0, 16);
            iv = resultVal.substring(resultVal.length() - 16);

            // 무결성키 생성
            hmacKey = resultVal.substring(0, 32);

            // 새로 생성한 암호화 토큰, 대칭키, 무결성키 저장
            saveCryptoTokenToRedis(cryptoToken);
            saveCryptoDataToRedis(key, iv, hmacKey);
        } else { // 기존 암호화 토큰, 대칭키, 무결성키 재사용
            cryptoToken = deserialization(storedCryptoToken); // 역직렬화
            NiceCryptoData niceCryptoData = getCryptoDataFromRedis();
            key = niceCryptoData.key();
            iv = niceCryptoData.iv();
            hmacKey = niceCryptoData.hmacKey();
        }

        // 요청 데이터 생성
        String reqData = createReqDataJson(cryptoToken.dataBody().siteCode(), authority.toString(), id);

        // 요청 데이터 암호화
        String encData = encryptReqData(key, iv, reqData);

        // Hmac 무결성 체크값 생성
        String integrityValue = generateIntegrityValue(hmacKey.getBytes(), encData.getBytes());

        return new CreateNiceApiRequestDataDto(
                cryptoToken.dataBody().tokenVersionId(),
                encData,
                integrityValue
        );
    }

    /**
     * ----------------------------------------------------------------------------------------------------
     **/
    // 암호화 토큰 조회
    private GetCryptoTokenResponse deserialization(String serializaedCryptoToken) {
        try {
            return objectMapper.readValue(serializaedCryptoToken, GetCryptoTokenResponse.class); // Deserialization
        } catch (Exception e) {
            throw new NiceException(DESERIALIZATION_FAILED);
        }
    }

    // 대칭키 및 무결성키 조회
    private NiceCryptoData getCryptoDataFromRedis() {
        String storedCryptoData = (String) redisUtil.get(REDIS_CRYPTO_KEY);
        if (storedCryptoData != null) {
            try {
                return objectMapper.readValue(storedCryptoData, NiceCryptoData.class); // Deserialization
            } catch (Exception e) {
                throw new NiceException(DESERIALIZATION_FAILED);
            }
        } else {
            throw new NiceException(CRYPTO_DATA_NOT_FOUND);
        }
    }

    // 암호화 토큰 저장
    private void saveCryptoTokenToRedis(GetCryptoTokenResponse cryptoToken) {
        try {
            String jsonValue = objectMapper.writeValueAsString(cryptoToken);
            redisUtil.saveAsValue(REDIS_CRYPTO_TOKEN_KEY, jsonValue, REDIS_CRYPTO_TOKEN_EXPIRATION, SECONDS);
        } catch (JsonProcessingException e) {
            throw new NiceException(SERIALIZATION_FAILED);
        }
    }

    // 대칭키, 무결성키 생성 위한 초기 값 생성
    private String generateResultVal(String reqDtim, String reqNo, String tokenVal) {
        try {
            String value = reqDtim.trim() + reqNo.trim() + tokenVal.trim();
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM_SHA256);
            md.update(value.getBytes());
            byte[] arrHashValue = md.digest();
            return Base64.getEncoder().encodeToString(arrHashValue);
        } catch (NoSuchAlgorithmException e) {
            throw new NiceException(HASH_ALGORITHM_NOT_FOUND);
        }
    }

    // 대칭키, 무결성키 저장
    private void saveCryptoDataToRedis(String key, String iv, String hmacKey) {
        try {
            NiceCryptoData niceCryptoData = NiceCryptoData.of(key, iv, hmacKey);
            String cryptoDataJson = objectMapper.writeValueAsString(niceCryptoData);
            redisUtil.saveWithoutExpiration(REDIS_CRYPTO_KEY, cryptoDataJson);
        } catch (Exception e) {
            throw new NiceException(SERIALIZATION_FAILED);
        }
    }

    // 요청 데이터 생성
    private String createReqDataJson(String siteCode, String type, Long id) {
        try {
            String requestNo = UUID.randomUUID().toString().substring(0, 30);
            NiceRequestData niceRequestData = new NiceRequestData(
                    requestNo,
                    niceProperties.getCallbackUrl() + RETURN_URL + id + type,
                    siteCode,
                    "Y"
            );

            String typeId = id.toString() + type;
            String requestNoKey = String.format(REDIS_REQUEST_NO_KEY_TEMPLATE, typeId);
            redisUtil.saveAsValue(requestNoKey, requestNo, REDIS_REQUEST_NO_EXPIRATION, MINUTES);

            return objectMapper.writeValueAsString(niceRequestData);
        } catch (JsonProcessingException e) {
            throw new NiceException(SERIALIZATION_FAILED);
        }
    }

    // 요청 데이터 암호화
    public String encryptReqData(String key, String iv, String reqData) {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), SYMMETRIC_ENCRYPTION_ALGORITHM_AES);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv.getBytes()));
            byte[] encrypted = cipher.doFinal(reqData.trim().getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new NiceException(INVALID_CIPHER_ALGORITHM);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new NiceException(INVALID_CIPHER_PARAMETERS);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new NiceException(CIPHER_DECRYPTION_FAILED);
        }
    }

    // 무결성 체크값 생성
    private String generateIntegrityValue(byte[] secretKey, byte[] encData) {
        try {
            Mac mac = Mac.getInstance(MAC_ALGORITHM_HMAC_SHA256);
            SecretKeySpec sks = new SecretKeySpec(secretKey, MAC_ALGORITHM_HMAC_SHA256);
            mac.init(sks);
            byte[] hmac256 = mac.doFinal(encData);
            return Base64.getEncoder().encodeToString(hmac256);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new NiceException(CREATE_INTEGRITY_VALUE_FAILED);
        }
    }
}
