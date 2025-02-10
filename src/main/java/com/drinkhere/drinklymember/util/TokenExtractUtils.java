package com.drinkhere.drinklymember.util;

import com.drinkhere.drinklymember.common.exception.oauth.AuthErrorCode;
import com.drinkhere.drinklymember.common.exception.token.InvalidAuthorizationTypeException;
import com.drinkhere.drinklymember.domain.auth.consts.AuthConsts;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenExtractUtils {

    public static String extractToken(final String tokenHeader) {
        if (!StringUtils.hasText(tokenHeader)) {
            throw new InvalidAuthorizationTypeException(AuthErrorCode.EMPTY_AUTHORIZATION_HEADER);
        }

        final String[] splitToken = tokenHeader.split(" ");
        final String authorizationType = splitToken[0];
        final String token = splitToken[1];
        if (!Objects.equals(authorizationType, AuthConsts.AUTHENTICATION_TYPE)) {
            throw new InvalidAuthorizationTypeException(AuthErrorCode.INVALID_AUTHORIZATION_TYPE);
        }
        return token;
    }
}
