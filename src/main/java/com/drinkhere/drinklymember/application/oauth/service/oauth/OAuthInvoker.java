package com.drinkhere.drinklymember.application.oauth.service.oauth;

import com.drinkhere.drinklymember.common.exception.oauth.AuthErrorCode;
import com.drinkhere.drinklymember.common.exception.oauth.AuthException;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthRequest;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthResponse;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthUserInfo;
import com.drinkhere.drinklymember.domain.auth.entity.OAuth;
import com.drinkhere.drinklymember.domain.auth.handler.request.OAuthSuccessEvent;
import com.drinkhere.drinklymember.domain.auth.service.OAuthQueryService;
import com.drinkhere.drinklymember.domain.auth.service.OAuthSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuthInvoker {

    private final List<AuthHandler> authHandlerList;
    private final OAuthQueryService oAuthQueryService;
    private final OAuthSaveService oAuthSaveService;
    private final ApplicationEventPublisher publisher;

    public OAuthResponse execute(OAuthRequest request) {
        final OAuthUserInfo oAuthUserInfo = attemptLogin(request);
        OAuth oAuth = publishEventAndRetrieveOAuth(oAuthUserInfo, request);
        return new OAuthResponse(oAuth.getId(), oAuth.isRegistered());
    }

    private OAuth publishEventAndRetrieveOAuth(OAuthUserInfo oAuthUserInfo, OAuthRequest request) {
        OAuth oAuth = oAuthQueryService.findBySub(oAuthUserInfo.getSub());

        if (oAuth == null) {
            oAuth = saveOAuth(oAuthUserInfo, request);
        }

        publisher.publishEvent(OAuthSuccessEvent.of(
                oAuthUserInfo.getNickname(),
                oAuthUserInfo.getEmail(),
                request.getProvider(),
                oAuthUserInfo.getSub(),
                oAuth.getId()
        ));

        return oAuth;
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
}
