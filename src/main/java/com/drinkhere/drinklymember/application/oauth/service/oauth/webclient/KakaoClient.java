package com.drinkhere.drinklymember.application.oauth.service.oauth.webclient;

import com.drinkhere.drinklymember.application.oauth.service.oauth.webclient.response.KakaoUserInfo;
import com.drinkhere.drinklymember.application.oauth.service.oauth.webclient.response.TokenInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoClient {

    private final WebClient webClient;

    public KakaoClient(@Qualifier("kakaoWebClient") WebClient kakaoWebClient) {
        this.webClient = kakaoWebClient;
    }

    // Kakao 사용자 정보 가져오기
    public KakaoUserInfo getKakaoUserInfo(String token) {
        return webClient.get()
                .uri("/v2/user/me")
                .header("Authorization", "Bearer " + token)
//                .header("Authorization", "Bearer 1Z9tLHaZUQtrfxliqPhIqCH8N2amSNYRAAAAAQo9c5oAAAGT5Lgi2qQkDeVh20ZZ")
                .retrieve()
                .bodyToMono(KakaoUserInfo.class)
                .block(); // 동기 호출, 필요에 따라 비동기로 변경 가능
    }

    // Kakao Access Token 정보 가져오기
    public TokenInfo getKakaoTokenInfo(String token) {
        return webClient.get()
                .uri("/v1/user/access_token_info")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(TokenInfo.class)
                .block(); // 동기 호출
    }
}