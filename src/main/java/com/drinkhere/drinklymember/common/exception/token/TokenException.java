package com.drinkhere.drinklymember.common.exception.token;

import com.drinkhere.drinklymember.common.exception.BaseErrorCode;
import com.drinkhere.drinklymember.common.exception.CustomException;

public class TokenException extends CustomException {

    public TokenException(BaseErrorCode error) {
        super(error);
    }
}
