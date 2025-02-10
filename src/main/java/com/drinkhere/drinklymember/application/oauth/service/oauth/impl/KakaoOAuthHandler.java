package com.drinkhere.drinklymember.application.oauth.service.oauth.impl;

import com.drinkhere.drinklymember.application.oauth.service.oauth.AuthHandler;
import com.drinkhere.drinklymember.application.oauth.service.oauth.webclient.KakaoClient;
import com.drinkhere.drinklymember.application.oauth.service.oauth.webclient.response.KakaoUserInfo;
import com.drinkhere.drinklymember.common.exception.oauth.AuthErrorCode;
import com.drinkhere.drinklymember.common.exception.token.InvalidTokenException;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthRequest;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthUserInfo;
import com.drinkhere.drinklymember.domain.auth.enums.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoOAuthHandler implements AuthHandler {
    private static final Provider OAUTH_TYPE = Provider.KAKAO;
    private final KakaoClient kakaoClient;
    private static final String KAKAO_AUTHORIZATION_BEARER = "Bearer ";

    @Override
    public OAuthUserInfo handle(OAuthRequest authenticationInfo) {

        // Access Token을 사용하여 사용자 정보 가져오기
        final KakaoUserInfo kakaoUserInfo = getKaKaoUserInfo(authenticationInfo.getAccessToken());
        if (kakaoUserInfo == null) {
            throw new InvalidTokenException(AuthErrorCode.INVALID_TOKEN);
        }

        // 사용자 정보 반환
        return new OAuthUserInfo(kakaoUserInfo.getSub(), kakaoUserInfo.getEmail(), kakaoUserInfo.getNickname());
    }

    @Override
    public boolean isAccessible(OAuthRequest authInfo) {
        return OAUTH_TYPE.equals(authInfo.getProvider());
    }

    private KakaoUserInfo getKaKaoUserInfo(String accessToken) {
        // REST API Key만으로 AccessToken을 통해 사용자 정보 가져오기
        return kakaoClient.getKakaoUserInfo(accessToken);
    }
}
