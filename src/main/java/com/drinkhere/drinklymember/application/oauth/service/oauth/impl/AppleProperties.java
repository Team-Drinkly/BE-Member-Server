package com.drinkhere.drinklymember.application.oauth.service.oauth.impl;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app-id.apple")
public class AppleProperties {
    private String customer;
    private String manager;

    @PostConstruct
    public void logAppleProperties() {
        log.info("✅ AppleProperties 로드 완료: customer={}, manager={}", customer, manager);
    }
}
