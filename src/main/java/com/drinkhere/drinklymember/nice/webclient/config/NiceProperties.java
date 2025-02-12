package com.drinkhere.drinklymember.nice.webclient.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "nice-api")
public class NiceProperties {
    private String organizationToken;
    private String clientId;
    private String clientSecret;
    private String productId;
    private String callbackUrl;
}