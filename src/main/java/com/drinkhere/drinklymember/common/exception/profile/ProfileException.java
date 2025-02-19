package com.drinkhere.drinklymember.common.exception.profile;

import com.drinkhere.drinklymember.common.exception.BaseErrorCode;
import com.drinkhere.drinklymember.common.exception.CustomException;

public class ProfileException extends CustomException {
    public ProfileException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}