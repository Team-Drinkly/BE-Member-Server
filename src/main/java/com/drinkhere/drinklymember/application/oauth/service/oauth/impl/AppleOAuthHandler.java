package com.drinkhere.drinklymember.application.oauth.service.oauth.impl;

import com.drinkhere.drinklymember.application.oauth.service.oauth.AuthHandler;
import com.drinkhere.drinklymember.application.oauth.service.oauth.webclient.AppleClient;
import com.drinkhere.drinklymember.application.oauth.service.oauth.webclient.response.Keys;
import com.drinkhere.drinklymember.common.exception.oauth.AuthErrorCode;
import com.drinkhere.drinklymember.common.exception.token.InvalidTokenException;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthRequest;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthUserInfo;
import com.drinkhere.drinklymember.domain.auth.enums.Provider;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleOAuthHandler implements AuthHandler {
    private static final Provider OAUTH_TYPE = Provider.APPLE;
    private static final String APPLE_USER_INFO = "email";
    private static final String APPLE_ISS = "https://appleid.apple.com";

    private final AppleClient appleClient;
    private final AppleProperties appleProperties;

    @Override
    public OAuthUserInfo handle(OAuthRequest authenticationInfo) {
        log.info("🚀 AppleOAuthHandler - attemptLogin 시작");

        // 토큰 서명 검증
        Jws<Claims> oidcTokenJwt = sigVerificationAndGetJws(authenticationInfo.getAccessToken());
        log.info("✅ Apple JWT 서명 검증 완료");

        // 토큰 바디 파싱해서 사용자 정보 획득
        String email = (String) oidcTokenJwt.getPayload().get(APPLE_USER_INFO);
        String sub = oidcTokenJwt.getPayload().getSubject();
        log.info("✅ Apple 로그인 성공 - email: {}, sub: {}", email, sub);

        return new OAuthUserInfo(sub, email, "Drinkly");
    }

    @Override
    public boolean isAccessible(OAuthRequest authenticationInfo) {
        return OAUTH_TYPE.equals(authenticationInfo.getProvider());
    }

    private Jws<Claims> sigVerificationAndGetJws(String unverifiedToken) {
        String kid = getKidFromUnsignedTokenHeader(unverifiedToken);
        log.info("🔍 Apple JWT 헤더에서 추출한 kid: {}", kid);

        Keys keys = appleClient.getKeys();
        log.info("🔍 Apple에서 가져온 공개 키 목록: {}", keys);

        if (keys == null || keys.getKeys() == null || keys.getKeys().isEmpty()) {
            log.error("❌ Apple OIDC 키를 가져오지 못함 (keys 값이 null)");
            throw new InvalidTokenException(AuthErrorCode.INVALID_TOKEN);
        }

        Keys.PubKey pubKey = keys.getKeys().stream()
                .filter(key -> key.getKid().equals(kid))
                .findAny()
                .orElseThrow(() -> {
                    log.error("❌ Apple OIDC 공개 키 중 kid {}와 일치하는 키가 없음", kid);
                    return new InvalidTokenException(AuthErrorCode.INVALID_TOKEN);
                });

        log.info("✅ Apple 공개 키 매칭 성공 - kid: {}", kid);

        // `aud` 값 결정 (Customer vs Manager)
        String audience = determineAudience(unverifiedToken);

        return getOIDCTokenJws(unverifiedToken, pubKey.getN(), pubKey.getE(), APPLE_ISS, audience);
    }

    /**
     * Apple JWT의 aud 값을 확인하여 고객용(CUSTOMER)인지 관리자용(MANAGER)인지 결정
     */
    private String determineAudience(String token) {
        try {
            Jws<Claims> parsedToken = Jwts.parser()
                    .build()
                    .parseSignedClaims(token);

            String aud = parsedToken.getPayload().getAudience().toString();
            log.info("🔍 Apple JWT aud 값: {}", aud);

            if (appleProperties.getCustomer().equals(aud)) {
                return appleProperties.getCustomer();
            } else if (appleProperties.getManager().equals(aud)) {
                return appleProperties.getManager();
            } else {
                log.error("❌ Apple JWT aud 값이 예상과 다름: {}", aud);
                throw new InvalidTokenException(AuthErrorCode.INVALID_TOKEN);
            }
        } catch (Exception e) {
            log.error("❌ Apple JWT aud 값 확인 중 예외 발생", e);
            throw new InvalidTokenException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    public Jws<Claims> getOIDCTokenJws(String token, String modulus, String exponent, String iss, String aud) {
        try {
            log.info("🔍 Apple JWT 검증을 위한 공개 키 생성 시작");
            PublicKey publicKey = getRSAPublicKey(modulus, exponent); // ✅ 수정된 메서드 적용
            log.info("✅ Apple 공개 키 생성 완료: {}", publicKey);

            return Jwts.parser()
                    .requireIssuer(iss)
                    .requireAudience(aud)
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            log.error("❌ Apple JWT 검증 실패 - 예외 발생: {}", e.getMessage(), e);
            throw new InvalidTokenException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    private PublicKey getRSAPublicKey(String modulus, String exponent) // ✅ Key → PublicKey로 변경
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodeN = Base64.getUrlDecoder().decode(modulus);
        byte[] decodeE = Base64.getUrlDecoder().decode(exponent);
        BigInteger n = new BigInteger(1, decodeN);
        BigInteger e = new BigInteger(1, decodeE);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
        return keyFactory.generatePublic(keySpec); // ✅ 반환 타입 PublicKey로 변경
    }

    private String getKidFromUnsignedTokenHeader(String token) {
        String[] splitToken = token.split("\\.");
        if (splitToken.length != 3) {
            log.error("❌ Apple JWT 형식이 잘못됨 - 토큰 조각 개수: {}", splitToken.length);
            throw new InvalidTokenException(AuthErrorCode.INVALID_TOKEN);
        }

        String headerJson = new String(Base64.getUrlDecoder().decode(splitToken[0]));
        JsonObject headerObject = new Gson().fromJson(headerJson, JsonObject.class);
        String kid = headerObject.get("kid").getAsString();
        log.info("✅ Apple JWT 헤더에서 kid 추출 완료: {}", kid);
        return kid;
    }
}
