package com.drinkhere.drinklymember.common.exception.oauth;

import com.drinkhere.drinklymember.common.exception.BaseErrorCode;
import com.drinkhere.drinklymember.common.exception.CustomException;

public class NotExistTokenException extends CustomException {

    public NotExistTokenException(BaseErrorCode error) {
        super(error);
    }
}
