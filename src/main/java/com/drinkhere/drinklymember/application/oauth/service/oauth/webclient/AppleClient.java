package com.drinkhere.drinklymember.application.oauth.service.oauth.webclient;

import com.drinkhere.drinklymember.application.oauth.service.oauth.webclient.response.Keys;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AppleClient {

    private final WebClient webClient;

    public AppleClient(@Qualifier("appleWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    // Apple OIDC keys 가져오기
    public Keys getKeys() {
        return webClient.get()
                .uri("/auth/keys")
                .retrieve()
                .bodyToMono(Keys.class)
                .block(); // 동기 방식 사용, 필요에 따라 비동기로 변경 가능
    }

    // Apple OIDC Access Token 가져오기 (필요 시)
    public String getAccessToken() {
        return webClient.get()
                .uri("/auth/token")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}