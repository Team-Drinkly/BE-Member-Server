package com.drinkhere.drinklymember.application.oauth.service.oauth.webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient appleWebClient(WebClient.Builder builder) {
        return builder.baseUrl("https://appleid.apple.com").build();
    }

    @Bean
    public WebClient kakaoWebClient(WebClient.Builder builder) {
        return builder.baseUrl("https://kapi.kakao.com").build();
    }
}