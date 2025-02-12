package com.drinkhere.drinklymember.nice.webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class NiceWebClientConfig {
    @Bean
    public WebClient niceWebClient(WebClient.Builder builder) {
        return builder.baseUrl("https://svc.niceapi.co.kr:22001").build();
    }
}