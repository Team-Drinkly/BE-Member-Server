package com.drinkhere.drinklymember.domain.auth.jwt;

import com.drinkhere.drinklymember.common.cache.operators.ValueOperator;
import com.drinkhere.drinklymember.common.exception.oauth.AuthErrorCode;
import com.drinkhere.drinklymember.common.exception.token.ExpiredTokenException;
import com.drinkhere.drinklymember.common.exception.token.InvalidTokenException;
import com.drinkhere.drinklymember.domain.auth.dto.Token;
import com.drinkhere.drinklymember.domain.member.service.member.MemberSubscribeQueryService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JWTProvider {

    private final JWTProperties jwtProperties;
    private final ValueOperator<String, Token> tokenCacheOperator;
    private final MemberSubscribeQueryService memberSubscribeQueryService;

    /**
     * Member 전용 JWT 생성 (`member-id` 포함)
     */
    public Token generateMemberToken(Long memberId) {
        boolean isSubscribed = memberSubscribeQueryService.isMemberSubscribed(memberId);
        Long subscribeId = memberSubscribeQueryService.getSubscribeId(memberId);

        PrivateClaims privateClaims = PrivateClaims.ofMember(memberId.toString(), TokenType.ACCESS_TOKEN, isSubscribed, subscribeId);

        String accessToken = generateToken(privateClaims, jwtProperties.getAccessTokenExpirationTime());
        String refreshToken = generateToken(privateClaims, jwtProperties.getRefreshTokenExpirationTime());

        return new Token(accessToken, refreshToken);
    }

    /**
     * Owner 전용 JWT 생성 (`owner-id` 포함)
     */
    public Token generateOwnerToken(Long ownerId) {
        PrivateClaims privateClaims = PrivateClaims.ofOwner(ownerId.toString(), TokenType.ACCESS_TOKEN);

        String accessToken = generateToken(privateClaims, jwtProperties.getAccessTokenExpirationTime());
        String refreshToken = generateToken(privateClaims, jwtProperties.getRefreshTokenExpirationTime());

        return new Token(accessToken, refreshToken);
    }

    /**
     * JWT 생성 (공통 메서드)
     */
    private String generateToken(final PrivateClaims privateClaims, final Long expirationTime) {
        final Date now = new Date();
        return Jwts.builder()
                .issuer(JWTConsts.TOKEN_ISSUER)
                .claims(privateClaims.createClaimsMap()) // 동적 Claims 추가
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
    }

    public boolean existsCachedRefreshToken(String refreshToken) {
        return tokenCacheOperator.contains(refreshToken);
    }

    public Token getCachedToken(String refreshToken) {
        return tokenCacheOperator.get(refreshToken);
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

    public Long extractMemberIdFromToken(String token) {
        try {
            return initializeJwtParser(TokenType.ACCESS_TOKEN)
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("member-id", Long.class);
        } catch (Exception e) {
            throw new InvalidTokenException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    public Long extractOwnerIdFromToken(String token) {
        try {
            return initializeJwtParser(TokenType.ACCESS_TOKEN)
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("owner-id", Long.class);
        } catch (Exception e) {
            throw new InvalidTokenException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    public Long extractSubscribeIdFromToken(String token) {
        try {
            return initializeJwtParser(TokenType.ACCESS_TOKEN)
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("subscribe-id", Long.class);
        } catch (Exception e) {
            throw new InvalidTokenException(AuthErrorCode.INVALID_TOKEN);
        }
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
}
