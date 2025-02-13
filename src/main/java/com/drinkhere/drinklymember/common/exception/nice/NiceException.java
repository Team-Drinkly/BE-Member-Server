package com.drinkhere.drinklymember.common.exception.nice;


import com.drinkhere.drinklymember.common.exception.BaseErrorCode;
import com.drinkhere.drinklymember.common.exception.CustomException;

public class NiceException extends CustomException {
    public NiceException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
