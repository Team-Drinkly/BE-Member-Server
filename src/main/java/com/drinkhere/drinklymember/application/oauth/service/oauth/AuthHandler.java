package com.drinkhere.drinklymember.application.oauth.service.oauth;

import com.drinkhere.drinklymember.domain.auth.dto.OAuthRequest;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthUserInfo;

public interface AuthHandler{

    OAuthUserInfo handle(OAuthRequest authenticationInfo);

    default boolean isAccessible(OAuthRequest authenticationInfo){
        return false;
    }
}
