package com.drinkhere.drinklymember.common.exception.token;

import com.drinkhere.drinklymember.common.exception.BaseErrorCode;
import com.drinkhere.drinklymember.common.exception.CustomException;

public class ExpiredTokenException extends CustomException {

    public ExpiredTokenException(BaseErrorCode error) {
        super(error);
    }
}
