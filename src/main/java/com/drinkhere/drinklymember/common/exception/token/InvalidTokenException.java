package com.drinkhere.drinklymember.common.exception.token;

import com.drinkhere.drinklymember.common.exception.BaseErrorCode;
import com.drinkhere.drinklymember.common.exception.CustomException;

public class InvalidTokenException extends CustomException {

    public InvalidTokenException(BaseErrorCode error) {
        super(error);
    }
}
