package com.drinkhere.drinklymember.common.exception.token;

import com.drinkhere.drinklymember.common.exception.BaseErrorCode;
import com.drinkhere.drinklymember.common.exception.CustomException;

public class InvalidAuthorizationTypeException extends CustomException {

    public InvalidAuthorizationTypeException(BaseErrorCode error) {
        super(error);
    }
}
