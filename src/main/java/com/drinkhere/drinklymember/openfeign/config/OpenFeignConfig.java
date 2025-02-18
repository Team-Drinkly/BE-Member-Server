package com.drinkhere.drinklymember.openfeign.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.drinkhere.drinklymember")
public class OpenFeignConfig {
}
