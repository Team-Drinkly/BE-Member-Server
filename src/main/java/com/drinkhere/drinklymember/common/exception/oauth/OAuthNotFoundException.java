package com.drinkhere.drinklymember.common.exception.oauth;

import com.drinkhere.drinklymember.common.exception.BaseErrorCode;

public class OAuthNotFoundException extends OAuthException {
    public OAuthNotFoundException(BaseErrorCode error) {
        super(error);
    }
}
