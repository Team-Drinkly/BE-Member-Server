package com.drinkhere.drinklymember.application.oauth.service.oauth.impl;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app-id.apple")
public class AppleProperties {
    private String customer;
    private String manager;
}
