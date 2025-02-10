package com.drinkhere.drinklymember.application.oauth.service.oauth.impl;

import com.drinkhere.drinklymember.application.oauth.service.oauth.webclient.AppleClient;
import com.drinkhere.drinklymember.application.oauth.service.oauth.AuthHandler;
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
import java.security.Key;
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

        log.info("AppleOAuthObserver attemptLogin");
        // 토큰 서명 검증
        Jws<Claims> oidcTokenJwt = sigVerificationAndGetJws(authenticationInfo.getAccessToken());
        // 토큰 바디 파싱해서 사용자 정보 획득
        String email = (String) oidcTokenJwt.getPayload().get(APPLE_USER_INFO);
        String sub = oidcTokenJwt.getPayload().getSubject();
        return new OAuthUserInfo(sub, email, "DrinkHere");
    }

    @Override
    public boolean isAccessible(OAuthRequest authenticationInfo) {
        return OAUTH_TYPE.equals(authenticationInfo.getProvider());
    }


    private Jws<Claims> sigVerificationAndGetJws(String unverifiedToken) {
        String kid = getKidFromUnsignedTokenHeader(unverifiedToken);

        Keys keys = appleClient.getKeys();
        Keys.PubKey pubKey = keys.getKeys().stream()
                .filter((key) -> key.getKid().equals(kid))
                .findAny()
                .get();

        return getOIDCTokenJws(unverifiedToken, pubKey.getN(), pubKey.getE(), APPLE_ISS, appleProperties.getApple());
    }

    public Jws<Claims> getOIDCTokenJws(String token, String modulus, String exponent, String iss, String aud) {
        try {
            return Jwts.parser()
                    .requireIssuer(iss)
                    .requireAudience(aud)
                    .verifyWith((PublicKey) getRSAPublicKey(modulus, exponent))
                    .build().parseSignedClaims(token);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new InvalidTokenException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    private Key getRSAPublicKey(String modulus, String exponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodeN = Base64.getUrlDecoder().decode(modulus);
        byte[] decodeE = Base64.getUrlDecoder().decode(exponent);
        BigInteger n = new BigInteger(1, decodeN);
        BigInteger e = new BigInteger(1, decodeE);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
        return keyFactory.generatePublic(keySpec);
    }

    private String getKidFromUnsignedTokenHeader(String token) {
        String[] splitToken = token.split("\\.");
        if (splitToken.length != 3) throw new InvalidTokenException(AuthErrorCode.INVALID_TOKEN);
        String headerJson = new String(Base64.getUrlDecoder().decode(splitToken[0]));
        JsonObject headerObject = new Gson().fromJson(headerJson, JsonObject.class);
        return headerObject.get("kid").getAsString();
    }
}
