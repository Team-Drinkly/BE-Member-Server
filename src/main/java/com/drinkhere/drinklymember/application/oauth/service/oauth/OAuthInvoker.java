package com.drinkhere.drinklymember.application.oauth.service.oauth;

import com.drinkhere.drinklymember.common.exception.oauth.AuthErrorCode;
import com.drinkhere.drinklymember.common.exception.oauth.AuthException;
import com.drinkhere.drinklymember.domain.auth.consts.AuthConsts;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthRequest;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthResponse;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthUserInfo;
import com.drinkhere.drinklymember.domain.auth.entity.OAuth;
import com.drinkhere.drinklymember.domain.auth.handler.request.OAuthSuccessEvent;
import com.drinkhere.drinklymember.domain.auth.jwt.JWTProvider;
import com.drinkhere.drinklymember.domain.auth.jwt.TokenType;
import com.drinkhere.drinklymember.domain.auth.service.OAuthQueryService;
import com.drinkhere.drinklymember.domain.auth.service.OAuthSaveService;
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
    private final OAuthQueryService oAuthQueryService;
    private final OAuthSaveService oAuthSaveService;

    private final ApplicationEventPublisher publisher;

    public OAuthResponse execute(OAuthRequest request) {
        final OAuthUserInfo oAuthUserInfo = attemptLogin(request);
        publishEvent(oAuthUserInfo, request);
        return generateServerAuthenticationTokens(oAuthUserInfo.getSub());
    }

    private void publishEvent(OAuthUserInfo oAuthUserInfo, OAuthRequest request) {
        // OAuth 테이블에서 OAuth ID 조회
        OAuth oAuth = oAuthQueryService.findBySub(oAuthUserInfo.getSub());

        // 존재하지 않으면 새로 저장
        if (oAuth == null) {
            oAuth = saveOAuth(oAuthUserInfo, request);
        }

        // OAuth ID를 userId로 설정하여 이벤트 발행
        publisher.publishEvent(OAuthSuccessEvent.of(
                oAuthUserInfo.getNickname(),
                oAuthUserInfo.getEmail(),
                request.getProvider(),
                oAuthUserInfo.getSub(),
                oAuth.getId()  // OAuth ID를 userId로 설정
        ));
    }

    private OAuth saveOAuth(OAuthUserInfo oAuthUserInfo, OAuthRequest request) {
        OAuth oAuth = OAuth.of(
                request.getProvider(),
                oAuthUserInfo.getSub()
        );
        return oAuthSaveService.save(oAuth);
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
        // 🛠 OAuth ID 조회
        OAuth oAuth = oAuthQueryService.findBySub(sub);

        if (oAuth == null) {
            throw new AuthException(AuthErrorCode.OAUTH_NOT_FOUND);
        }

        final JWTProvider.Token token = jwtProvider.generateToken(sub, oAuth.getId());
        tokenSaveService.saveToken(token.refreshToken(), TokenType.REFRESH_TOKEN, oAuth.getId().toString());
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
