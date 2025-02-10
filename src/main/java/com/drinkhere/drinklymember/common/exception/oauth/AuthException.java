package com.drinkhere.drinklymember.common.exception.oauth;

import com.drinkhere.drinklymember.common.exception.BaseErrorCode;
import com.drinkhere.drinklymember.common.exception.CustomException;

public class AuthException extends CustomException {

    public AuthException(BaseErrorCode error) {
        super(error);
    }
}
