package com.drinkhere.drinklymember.domain.auth.consts;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IgnoredPathConsts {

    @Getter
    private static Map<String, HttpMethod> ignoredPath = Map.of(
        "/docs/**", HttpMethod.GET,
        "/favicon.ico", HttpMethod.GET,
        "/oauth/**", HttpMethod.GET,
        "/jwt", HttpMethod.GET,
        "/reissue",HttpMethod.POST,
        "/actuator/**", HttpMethod.GET,
        "/user/landing", HttpMethod.POST,
        "/api/v1/nice/**", HttpMethod.GET,
        "/api/v1/stores/**", HttpMethod.POST
    );

}
