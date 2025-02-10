package com.drinkhere.drinklymember.application.oauth.service.oauth;

import com.drinkhere.drinklymember.common.exception.oauth.AuthErrorCode;
import com.drinkhere.drinklymember.common.exception.oauth.AuthException;
import com.drinkhere.drinklymember.domain.auth.consts.AuthConsts;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthRequest;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthResponse;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthUserInfo;
import com.drinkhere.drinklymember.domain.auth.handler.request.OAuthSuccessEvent;
import com.drinkhere.drinklymember.domain.auth.jwt.JWTProvider;
import com.drinkhere.drinklymember.domain.auth.jwt.TokenType;
import com.drinkhere.drinklymember.domain.auth.service.TokenSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuthInvoker {
    private final List<AuthHandler> authHandlerList;
    private final JWTProvider jwtProvider;
    private final TokenSaveService tokenSaveService;
    private final ApplicationEventPublisher publisher;

    public OAuthResponse execute(OAuthRequest request) {
        final OAuthUserInfo oAuthUserInfo = attemptLogin(request);
        publishEvent(oAuthUserInfo, request);
        return generateServerAuthenticationTokens(oAuthUserInfo.getSub());
    }

    private void publishEvent(OAuthUserInfo oAuthUserInfo, OAuthRequest request) {
        publisher.publishEvent(OAuthSuccessEvent.of(
                oAuthUserInfo.getNickname(),
                oAuthUserInfo.getEmail(),
                request.getProvider(),
                oAuthUserInfo.getSub()
        ));
    }

    private OAuthUserInfo attemptLogin(OAuthRequest request) {
        for (AuthHandler authHandler : authHandlerList) {
            if (authHandler.isAccessible(request)) {
                return authHandler.handle(request);
            }
        }
        throw new AuthException(AuthErrorCode.OAUTH_FAIL);
    }

    private OAuthResponse generateServerAuthenticationTokens(String sub) {
        // 토큰 생성
        final JWTProvider.Token token = jwtProvider.generateToken(sub);

        // Redis에 REFRESH_TOKEN 저장
        tokenSaveService.saveToken(token.refreshToken(), TokenType.REFRESH_TOKEN, sub);

        // OAuthResponse 생성
        return buildOAuthResponse(token);
    }

    private OAuthResponse buildOAuthResponse(JWTProvider.Token token) {
        final String accessToken = AuthConsts.AUTHENTICATION_TYPE_PREFIX + token.accessToken();
        final String refreshToken = AuthConsts.AUTHENTICATION_TYPE_PREFIX + token.refreshToken();
        return OAuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
