package com.drinkhere.drinklymember.common.cache.config;

import com.drinkhere.drinklymember.common.cache.CacheTemplate;
import com.drinkhere.drinklymember.common.cache.DefaultCacheManger;
import com.drinkhere.drinklymember.common.cache.DefaultCacheTemplate;
import com.drinkhere.drinklymember.common.cache.operators.SetOperator;
import com.drinkhere.drinklymember.common.cache.operators.ValueOperator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheTemplate<?, ?> cacheTemplate() {
        return new DefaultCacheTemplate<>();
    }

    @Bean
    public SetOperator<?> setOperator() {
        return cacheTemplate().opsForSet();
    }

    @Bean
    public ValueOperator<?, ?> valueOperator() {
        return cacheTemplate().opsForValue();
    }

    @Bean
    @Primary
    public CacheManager cacheManager() {
        final DefaultCacheManger defaultCacheManger = new DefaultCacheManger();
        defaultCacheManger.setupTimeToLive(10, TimeUnit.SECONDS);
        return defaultCacheManger;
    }
}
