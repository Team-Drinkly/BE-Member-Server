package com.drinkhere.drinklymember.domain.auth.jwt;

import com.drinkhere.drinklymember.common.cache.operators.ValueOperator;
import com.drinkhere.drinklymember.common.exception.oauth.AuthErrorCode;
import com.drinkhere.drinklymember.common.exception.token.ExpiredTokenException;
import com.drinkhere.drinklymember.common.exception.token.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JWTProvider {
    private final JWTProperties jwtProperties;
    private final ValueOperator<String, Token> tokenCacheOperator;

    public String generateAccessToken(final String sub) {
        return generateToken(PrivateClaims.of(sub, TokenType.ACCESS_TOKEN), jwtProperties.getAccessTokenExpirationTime());
    }

    public String generateRefreshToken(final String sub) {
        return generateToken(PrivateClaims.of(sub, TokenType.REFRESH_TOKEN), jwtProperties.getRefreshTokenExpirationTime());
    }

    public Token generateToken(String sub, Long userId) {
        // OAuth ID를 `user-id`로 저장하여 토큰 생성
        final PrivateClaims privateClaims = PrivateClaims.of(userId.toString(), TokenType.ACCESS_TOKEN);
        final String accessToken = generateToken(privateClaims, jwtProperties.getAccessTokenExpirationTime());

        final PrivateClaims refreshClaims = PrivateClaims.of(userId.toString(), TokenType.REFRESH_TOKEN);
        final String refreshToken = generateToken(refreshClaims, jwtProperties.getRefreshTokenExpirationTime());

        return new Token(accessToken, refreshToken);
    }


    public Token reIssueToken(final String refreshToken) {
        validateToken(refreshToken, TokenType.REFRESH_TOKEN);
        final String sub = extractSubFromToken(refreshToken, TokenType.REFRESH_TOKEN);
        final String newAccessToken = generateAccessToken(sub);
        final String newRefreshToken = generateRefreshToken(sub);

        tokenCacheOperator.setWithExpire(refreshToken, new Token(newAccessToken, newRefreshToken), 1, TimeUnit.MINUTES);

        return new Token(newAccessToken, newRefreshToken);
    }

    public String extractSubFromToken(String token, TokenType tokenType) {
        try {
            return initializeJwtParser(tokenType)
                    .parseSignedClaims(token)
                    .getPayload()
                    .get(JWTConsts.USER_CLAIMS, String.class);
        } catch (IllegalArgumentException | JwtException e) {
            throw new InvalidTokenException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    public Long extractUserIdFromToken(String token) {
        try {
            // JWT 토큰에서 OAuth ID (user-id)를 가져옴
            String userId = extractSubFromToken(token, TokenType.ACCESS_TOKEN);
            return Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new InvalidTokenException(AuthErrorCode.INVALID_TOKEN);
        }
    }


    public boolean existsCachedRefreshToken(String refreshToken) {
        return tokenCacheOperator.contains(refreshToken);
    }

    public Token getCachedToken(String refreshToken) {
        return tokenCacheOperator.get(refreshToken);
    }

    private String generateToken(final PrivateClaims privateClaims, final Long expirationTime) {
        final Date now = new Date();
        return Jwts.builder()
                .issuer(JWTConsts.TOKEN_ISSUER)
                .claim(JWTConsts.USER_CLAIMS, privateClaims.getSub())  // OAuth ID를 user-id로 설정
                .claim(JWTConsts.TOKEN_TYPE, privateClaims.getTokenType().name())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }


    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
    }

    public void validateToken(final String token, final TokenType tokenType) {
        final JwtParser jwtParser = initializeJwtParser(tokenType);
        try {
            jwtParser.parse(token);
        } catch (MalformedJwtException | IncorrectClaimException | IllegalArgumentException e) {
            throw new InvalidTokenException(AuthErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException(AuthErrorCode.EXPIRED_TOKEN);
        }
    }

    private JwtParser initializeJwtParser(final TokenType tokenType) {
        return Jwts.parser()
                .json(new JacksonDeserializer<>(PrivateClaims.getClaimsTypeDetailMap()))
                .verifyWith(getSigningKey())
                .requireIssuer(JWTConsts.TOKEN_ISSUER)
                .require(JWTConsts.TOKEN_TYPE, tokenType)
                .build();
    }

    public record Token(String accessToken, String refreshToken) {
    }
}

